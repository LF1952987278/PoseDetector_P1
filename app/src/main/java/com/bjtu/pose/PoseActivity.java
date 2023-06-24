package com.bjtu.pose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bjtu.pose.Service.GPSService;
import com.bjtu.pose.Service.SensorService;
import com.bjtu.pose.pose.entity.SenUnitV2;
import com.bjtu.pose.pose.predict.Predict;
import com.bjtu.pose.utils.FileOperation;
import com.bjtu.pose.utils.Step;
import com.bjtu.pose.utils.Step6;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PoseActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/DataCollect/";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String TAG = "MainActivity";
    private static int COLLECT_INTERVAL = 20;
    private static int INFERENCE_INTERVAL = 50;
    private static boolean isRefuse = false;

    private boolean stopThreads = false;
    private Step6 isWalk_step;
    private boolean isWalk = true;

    private float[] acc = new float[3];
    private float[] mag = new float[3];
    private float[] gyr = new float[3];
    private float[] gra = new float[3];
    private float[] l_acc = new float[3];
    private float[] rot = new float[4];
    private float[] g_rot = new float[3];
    private float ori;
    private Step stepCounter;
    private int step = 0;

    private TextView textView = null;
    private TextView poseView = null;
    private Button endButton = null;
    private Button startButton = null;

    private SurfaceHolder surfaceHolder = null;

    private boolean recordStart = false;
    private String fileName;
    private SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
    private static final String path = Environment.getExternalStorageDirectory().getPath() + "/DataCollect/data_gps/";

    private List<List<String>> contents = new ArrayList<>();
    private int inferenceSigh = 0;
    private List<List<String>> poseContents = new ArrayList<>();
    private int poseInferenceSigh = 0;
    private Predict predict_pose;
    private String pose = "Flat";
    private String poseEval = "Pose";

    private SensorService sensorService;
    private GPSService gpsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pose);

        // 固定竖屏方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 判断是否有读写文件的权限
        verifyStoragePermissions(this);

        sensorService = new SensorService(this);
        gpsService = new GPSService(this,this);

        initView();

        if(ContextCompat.checkSelfPermission(PoseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //进行权限申请,200是标识码
            ActivityCompat.requestPermissions(PoseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            // TODO: 权限获取成功判断
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        sensorService.registerSensor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsService.removeUpdates();
        gpsService.unRegisterGnssStatusCallback();
        sensorService.unregisterSensor();
    }

    private void initView(){
        textView = findViewById(R.id.textView);
//        textView.setMovementMethod(new ScrollingMovementMethod());

        poseView = findViewById(R.id.poseTextView);

        endButton = findViewById(R.id.endButton);
        endButton.setOnClickListener(this);
        endButton.setEnabled(false);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                textView.setText("开始采集");
//                sensorService.registerSensor();

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                startButton.setEnabled(false);
                endButton.setEnabled(true);

                predict_pose = new Predict();
//                predict_isWalk = new PredictV0();
//                isWalk_step = new Step();
//                isWalk_step = new Step2();
//                isWalk_step = new Step6();
//                toast = Toast.makeText(IndoorActivity.this, "Detect drop-off point!", Toast.LENGTH_LONG);

//                surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
                callModel("flat", 1,1, "v1.0");
                break;
            case R.id.endButton:
                recordStart = false;
                sensorService.unregisterSensor();
                gpsService.removeUpdates();
                gpsService.unRegisterGnssStatusCallback();

                poseView.setText("");
                String tempInfo = "采集结束";
                textView.setText(tempInfo);
                textView.scrollTo(0,0);
//                //save wifi
//                wifiManager.startScan();
//                tempInfo += "\n出口Wifi信息已保存";
//                LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 239,"Entrance WiFi have been saved!");


                //opt
//                inferenceDriftFixByContents();
//                if(need_fix){
//                    drift_fix();
//                }
//                LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 247,"Final drift fixing finished!");

//                float final_straight_brng = 0.0f;
//                if(pred_bp_list.size() > 5){
//                    final_straight_brng = lineFitting.line_fitting(X_list,Y_list,pred_brng_list);
//                    LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 251,"Line fitting finished!");
//                }

//                for(int i = 0; i < Math.min(3,X_list.size()); i++) {
//                    X_list.remove(X_list.size()- 1 - i);
//                    Y_list.remove(Y_list.size()- 1 - i);
//                    pred_bp_list.remove(pred_bp_list.size()- 1 - i);
//                }

//                Canvas c = surfaceHolder.lockCanvas();
//                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                paint.setColor(Color.RED);
//                paint.setStrokeWidth(10);
//                for(int i = 1;i < X_list.size();i++){
//                    c.drawLine(init_x+X_list.get(i-1)*proportion,init_y-Y_list.get(i-1)*proportion,init_x+X_list.get(i)*proportion,init_y-Y_list.get(i)*proportion,paint);
//                }
//                c.drawBitmap(zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.marker), 50, 50),init_x-25,init_y-50,paint);
//                surfaceHolder.unlockCanvasAndPost(c);
//
//                for(int i =0; i < pred_bp_list.size();i++){
//                    pred_bp_list.get(i).x = X_list.get(i);
//                    pred_bp_list.get(i).y = Y_list.get(i);
//                }
//                tempInfo += "\n已使用Line-fitting算法优化轨迹";
//
//                //save map
//                if(this.pred_bp_list != null && this.pred_bp_list.size() > 0){
//                    pred_bp_list.get(pred_bp_list.size()-1).bearing = final_straight_brng;
//
//                    Trajectory2Map t2map = new Trajectory2Map();
//
//                    t2map.build_road_map(this.pred_bp_list);
//                    t2map.build_mirror_symmetry_map();
//                    LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 282,"successful find pred_list");
////                    Log.println(1,"debug","success find pred_list");
//                    t2map.save_road_file();
//                }else{
//                    LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 286,"Fail find pred_list");
////                    Log.println(1,"debug","fail find pred_list");
//                }
//                tempInfo += "\n您的轨迹已经保存\n您可以退出App了";
//                textView.setText(tempInfo);
//                textView.scrollTo(0,0);
                startButton.setEnabled(true);
                endButton.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void Inference(String phone_pose, int TRAIN_SWQ_LEN, int EVAL_SEQ_LEN, String ftr_version,List<String> content){
        content = recoveryFileOperation(content);

        List<Float> sec_gyx_list = new ArrayList<>();
        List<Float> sec_gyy_list = new ArrayList<>();
        List<Float> sec_gyz_list = new ArrayList<>();
        List<Float> sec_gvtx_list = new ArrayList<>();
        List<Float> sec_gvty_list = new ArrayList<>();
        List<Float> sec_gvtz_list = new ArrayList<>();
        float[] gvt_list;
        float gyrz_res;

        //原bearing列表获取部分
//        for(SenUnitV2 unit: ts_data){
//            sec_gyx_list.add(unit.gyrx);
//            sec_gyy_list.add(unit.gyry);
//            sec_gyz_list.add(unit.gyrz);
//        }

//        for(SenUnitV2 unit: ts_data){
//            gvt_list = gvt_process(unit.gvtx,unit.gvty,unit.gvtz);
//            gyrz_res = numpy.dot_product(unit.gyrx,unit.gyry,unit.gyrz,gvt_list[6],gvt_list[7],gvt_list[8]);
//            sec_gyz_list.add(gyrz_res);
//        }
//
//        float sec_brng_diff = Integrate_step_heading(sec_gyz_list);
//        pred_brng_diff_list.add(sec_brng_diff);
//        pred_brng_list.add((init_brng + sec_brng_diff) % 360);
//
//        LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 1035, "inte brng diff:"+ sec_brng_diff +", crnt brng:"+ (init_brng + sec_brng_diff) % 360);
//
//        float pred_spd = pred_spd_list.get(pred_spd_list.size()-1),pred_brng = pred_brng_list.get(pred_brng_list.size()-1);
//        if(pred_brng < 0){
//            pred_brng = pred_brng + 360;
//        }
//
//        Pair<Float,Float> x_y_pair;
//        float pred_x,pred_y;
//        if(pred_bp_list.size()==0){
//            x_y_pair = indoorGeoUtils.position_from_spd_brng(base_bp.x,base_bp.y,pred_spd,pred_brng);
//            pred_x = x_y_pair.first;
//            pred_y = x_y_pair.second;
//        }else{
//            x_y_pair = indoorGeoUtils.position_from_spd_brng(pred_bp_list.get(pred_bp_list.size()-1).x,
//                    pred_bp_list.get(pred_bp_list.size()-1).y,
//                    pred_spd,pred_brng);
//            pred_x = x_y_pair.first;
//            pred_y = x_y_pair.second;
//        }

//        X_list.add(pred_x);
//        Y_list.add(pred_y);
//        LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 1058, "pred x:"+ pred_x +", pred y:"+ pred_y);

//        IndoorBasicPoint pred_bp = new IndoorBasicPoint(0,pred_x,pred_y,pred_spd,pred_brng);
        //添加轨迹预测数据
//        pred_bp_list.add(pred_bp);

//        eval = "Predict x:" + pred_x +  "\n"+
//                "Predict y:" + pred_y +  "\n"+
//                "Predict spd:" + pred_spd + "\n"+
//                "Predict brng:" + pred_brng + "\n";

//        Canvas c = surfaceHolder.lockCanvas();
//        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(10);
//        c.drawBitmap(zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.marker), 50, 50),init_x-25,init_y-50,paint);
//        for(int i = 1;i < X_list.size();i++){
//            if(init_x+X_list.get(i)*proportion>surfaceView.getWidth() || init_x+X_list.get(i)*proportion < 0 ||
//                    init_y-Y_list.get(i)*proportion > surfaceView.getHeight() || init_y-Y_list.get(i)*proportion < 0){
//                proportion /= 2.0f;
//            }
//            c.drawLine(init_x+X_list.get(i-1)*proportion,init_y-Y_list.get(i-1)*proportion,init_x+X_list.get(i)*proportion,init_y-Y_list.get(i)*proportion,paint);
//        }
//        surfaceHolder.unlockCanvasAndPost(c);

        poseEval = "POSE: "+ pose;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!recordStart){
                    return;
                }
                switch (pose){
                    case "Flat":
                        poseView.setText(poseEval);
                        poseView.setTextColor(Color.parseColor("#00ffff"));
                        break;
                    case "Pocket":
                        poseView.setText(poseEval);
                        poseView.setTextColor(Color.parseColor("#0000ff"));
                        break;
                    case "Calling":
                        poseView.setText(poseEval);
                        poseView.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case "Bag":
                        poseView.setText(poseEval);
                        poseView.setTextColor(Color.parseColor("#00ff00"));
                        break;
                }
            }
        });
        //TODO: 画图这里是否也需要加log, 之前测试这部分有时会报错?
    }

    private synchronized void inferenceByContent(){
        if(contents.size()!=0){
            Inference("flat", 1,1, "v1.0",contents.get(0));
            contents.remove(0);
            inferenceSigh--;
        }else {
//            LogUtils.printLog("WARN",df1.format(new Date()), "IndoorActivity.java", 334,"contents is empty!");
        }
    }

    private void callModel(String phone_pose, int TRAIN_SWQ_LEN, int EVAL_SEQ_LEN, String ftr_version){
        recordStart = true;

        fileName = df1.format(new Date()) + ".csv";
        File file = FileOperation.makeFilePath(path, fileName);
        String filePath = file.getAbsolutePath();
        String dataFormat = "Sys_time,laccx,y,z,lacc_accu,grax,y,z,gra_accu,gyrx,y,z,gyr_accu,accx,y,z,acc_accu,magx,y,z,mag_accu,ori,rot_x,rot_y,rot_z,rot_s,rot_head_acc,rot_accu,grot_x,grot_y,grot_z,g_rot_s,g_rot_accu," +
                "lon,lat,speed,bearing,gps_time,gps_accu,step";
        String[] contents = {dataFormat, filePath};
        WriteWork writeWork = new WriteWork();
        writeWork.execute(contents);

        stepCounter = new Step();

        String tmp_pose;

        new Thread(new Runnable() {
            @Override
            public void run() {
                sensorService.registerSensor();
                List<String> content = new ArrayList<>();
                int count = 0;
                while (recordStart) {
                    try {
                        Thread.sleep(COLLECT_INTERVAL - 2);
                        Map<String, float[]> sensorValue = sensorService.getValue();
                        l_acc = (float[]) sensorValue.get("lacc");
                        gra = (float[]) sensorValue.get("gra");
                        gyr = (float[]) sensorValue.get("gyr");
                        acc = (float[]) sensorValue.get("acc");
                        mag = (float[]) sensorValue.get("mag");
                        rot = (float[]) sensorValue.get("rot");
                        g_rot = (float[]) sensorValue.get("g_rot");
                        float[] ori_temp = (float[]) sensorValue.get("ori");
                        if (ori_temp != null) {
                            ori = ori_temp[0];
                        }
                        step = stepCounter.stepCounter(acc);

                        //包含GPS信息
                        String dataString = System.currentTimeMillis() + ","
                                + l_acc[0] + "," + l_acc[1] + "," + l_acc[2] + "," + l_acc[3] + ","
                                + gra[0] + "," + gra[1] + "," + gra[2] + "," + gra[3] + ","
                                + gyr[0] + "," + gyr[1] + "," + gyr[2] + "," + gyr[3] + ","
                                + acc[0] + "," + acc[1] + "," + acc[2] + "," + acc[3] + ","
                                + mag[0] + "," + mag[1] + "," + mag[2] + "," + mag[3] + ","
                                + ori + ","
                                + rot[0] + "," + rot[1] + "," + rot[2] + "," + rot[3] + "," + rot[4] + "," + rot[5] + ","
                                + g_rot[0] + "," + g_rot[1] + "," + g_rot[2] + "," + g_rot[3] + "," + g_rot[4] + ","
                                + gpsService.getDataString() + "," + step;
//                                String data = sensorService.getDataString() + gpsService.getDataString();
                        content.add(dataString);
                        String[] contents = {dataString, filePath};
                        WriteWork writeWork = new WriteWork();
                        writeWork.execute(contents);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
//                        LogUtils.printLog("ERR",df1.format(new Date()), "IndoorActivity.java", 575,"Thread error");
                    }
                    if (++count == INFERENCE_INTERVAL) {
                        if(!isWalk){
//                            addisWalkContents(content);
                        }else{
//                            if (need_fix){
//                                addAllContents(content);
//                            }
                            addContents(content);
                            addPoseContents(content);
                        }
                        count = 0;
                        content = new ArrayList<>();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stopThreads){
                    if(isWalk){
                        while(inferenceSigh > 0){
                            inferenceByContent();
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stopThreads){
                    if(isWalk){
                        while(poseInferenceSigh > 0){
                            poseInferenceByContent();
                        }
                    }
                }
            }
        }).start();

    }

    public static class WriteWork extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            FileOperation fileOperation = new FileOperation();
            fileOperation.writeCsv(strings[0], strings[1]);
            return null;
        }
    }

    private synchronized void addContents(List<String> content){
        this.contents.add(content);
        inferenceSigh++;
    }

    private synchronized void addPoseContents(List<String> content){
        this.poseContents.add(content);
        poseInferenceSigh++;
    }

    private synchronized void poseInferenceByContent(){
        if(poseContents.size()!=0){
            poseInference(poseContents.get(0));
            poseContents.remove(0);
            poseInferenceSigh--;
        }else {
//            LogUtils.printLog("WARN",df1.format(new Date()), "IndoorActivity.java", 349,"poseContents is empty!");
        }

    }

    public static List<String> recoveryFileOperation(List<String> content){
        List<String> res = new ArrayList<>();
        List<String> init_data = new ArrayList<>();
        List<String> filtered_sen_data = new ArrayList<>();

        for(int i = 0; i < content.size();i++){
            if(i == 0){
                filtered_sen_data = new ArrayList<>();
            }else if(i <= 50){
                init_data.add(content.get(i));
            }else{
                if(content.get(i).contains("null")){
                    String tmp = content.get(i).replace("null","0");
                    filtered_sen_data.add(tmp);
                }
            }
        }

        for(String str:content){
//          res.add(str.replace("null","0") + ",0.0");
            res.add(str.replace("null","0"));
        }

        return res;
    }

    private void poseInference(List<String> content){
        content = recoveryFileOperation(content);

        // svm方法
        ArrayList<SenUnitV2> lines = new ArrayList<>();
        for (String line: content) {
            lines.add(new SenUnitV2(line));
        }

        pose = predict_pose.predictMoment(lines).first;  // Pair<pose_name, accuracy>
        Log.i("Predict pose", pose);
//        LogUtils.printLog("INFO",df1.format(new Date()), "IndoorActivity.java", 380,"Predict pose:"+pose);

        // 当前状态
//        pose = isWalk_step.getStatus();
        String temp_pose = pose;
        if(pose.equals("unknown")){
            temp_pose = "Flat";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    // Do the task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "无权读写文件!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) {
            Toast.makeText(this, "已获得访问所有文件的权限", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
        }
    }

}