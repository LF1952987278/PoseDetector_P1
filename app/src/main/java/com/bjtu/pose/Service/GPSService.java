package com.bjtu.pose.Service;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class GPSService {

    private static final String TAG = "GPSService";
    private Context context;
    private Activity activity;

    private LocationManager locationManager;

    //参数
    private String gpsTime;
    private String longitude;
    private String latitude;
    private String speed;
    private String bearing;
    private String accuracy;

    public GPSService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
//        openGPSSetting();
        startGPS();
    }

    private void startGPS() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 监听状态
            locationManager.registerGnssStatusCallback(mGNSSCallback);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        // TODO: 权限获取成功判断
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            gpsTime = String.valueOf(location.getTime());
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            if (location.hasBearing())
                bearing = String.valueOf(location.getBearing());
            else
                bearing = null;
            if (location.hasSpeed())
                speed = String.valueOf(location.getSpeed());
            else
                speed = null;
            if (location.hasAccuracy())
                accuracy = String.valueOf(location.getAccuracy());
            else
                accuracy = null;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    // 状态监听，获取设备搜索到的卫星个数
    private GnssStatus.Callback mGNSSCallback = new GnssStatus.Callback() {
        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            super.onSatelliteStatusChanged(status);
            if (status != null) {
                // get satellite count
                int satelliteCount = status.getSatelliteCount();
                Log.i(TAG, "搜索到: " + satelliteCount + "颗卫星");

                // 可以查看指定类型卫星的个数
                /*int BDSatelliteCount = 0;
                int sInFix = 0;
                if (satelliteCount > 0) {
                    for (int i = 0; i < satelliteCount; i++) {
                        // 获取卫星类型
                        int type = status.getConstellationType(i);
                        if (GnssStatus.CONSTELLATION_BEIDOU == type) {
                            // 北斗卫星
                            BDSatelliteCount++;
                        }
                        // 判断是否用于定位
                        if (status.usedInFix(i)) {
                            sInFix++;
                        }
                    }

                    Log.d(TAG, "共收到卫星信号：" + satelliteCount + "个；"
                            + "北斗卫星信号个数：" + BDSatelliteCount + "个；"
                            + "用于定位的卫星个数："+ sInFix + "\n");
                }*/
            } else {
                Toast.makeText(context, "卫星状态变化为空！", Toast.LENGTH_SHORT).show();
            }
        }

        // 定位启动
        @Override
        public void onStarted() {
            super.onStarted();
            Toast.makeText(context, "定位启动！", Toast.LENGTH_SHORT).show();
        }

        // 定位结束

        @Override
        public void onStopped() {
            super.onStopped();
            Toast.makeText(context, "定位结束！", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 跳转到系统的gps设置界面
     **/
//    private void openGPSSetting() {
//        if (locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Looper.prepare();
//            Toast.makeText(context, "GPS模块正常", Toast.LENGTH_SHORT).show();
//            Looper.loop();
//            return;
//        }
//        Looper.prepare();
//        Toast.makeText(context, "请开启GPS！", Toast.LENGTH_SHORT).show();
//        Looper.loop();
//        // 跳转到GPS的设置页面
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        activity.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
//    }

    public String getDataString() {
        return longitude + "," + latitude + "," + speed + "," + bearing + "," + gpsTime + "," + accuracy ;
    }

    public void removeUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    public void unRegisterGnssStatusCallback() {
        locationManager.unregisterGnssStatusCallback(mGNSSCallback);
    }
}
