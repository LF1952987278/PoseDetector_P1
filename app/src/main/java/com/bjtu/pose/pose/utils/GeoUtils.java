package com.bjtu.pose.pose.utils;

/**
 * @author Li Haitao
 * @version 1.0
 * Description: 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
 */
public class GeoUtils {

    private final static double EARTH_RADIUS = 6371.0;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lat1 第一个坐标的维度
     * @param lat2 第二个坐标的纬度
     * @param lon1 第一个坐标的精度
     * @param lon2 第二个坐标的精度
     * Description:根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public static double haversine_formula(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = (s * 10000) / 10;
        return s;
    }


    public static void main(String[] args) {
        double s = haversine_formula(39.951582,116.331979,39.951554, 116.331773);
        System.out.println(s); // 17.853435012679107
    }

}

