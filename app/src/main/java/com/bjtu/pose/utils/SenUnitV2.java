package com.bjtu.pose.utils;

public class SenUnitV2 {
    public String line;
    public long ts_ms;
    public float laccx;
    public float laccy;
    public float laccz;
    public float lacc_accu;
    public float gvtx;
    public float gvty;
    public float gvtz;
    public float gvt_accu;
    public float gyrx;
    public float gyry;
    public float gyrz;
    public float gyr_accu;
    public float accx;
    public float accy;
    public float accz;
    public float acc_accu;
    public float magx;
    public float magy;
    public float magz;
    public float mag_accu;
    public float ori;
    public float rvx;
    public float rvy;
    public float rvz;
    public float rvs;
    public float rv_head_acc;
    public float rv_accu;
    public float gamervx;
    public float gamervy;
    public float gamervz;
    public float gamervs;
    public float gamerv_accu;
    public float lon;
    public float lat;
    public float speed;
    public float bearing;
    public float gps_accu;
    public long ts;
    public float step;

    public SenUnitV2(String line){
        String[] data = line.split(",");

        this.line = line;
        ts_ms = Long.valueOf(data[0]);
        laccx = Math.round(Float.valueOf(data[1]) * 1e4) * 0.0001f;
        laccy = Math.round(Float.valueOf(data[2]) * 1e4) * 0.0001f;
        laccz = Math.round(Float.valueOf(data[3]) * 1e4) * 0.0001f;
        lacc_accu = Math.round(Float.valueOf(data[4]) * 1e2) * 0.01f;
        gvtx = Math.round(Float.valueOf(data[5]) * 1e4) * 0.0001f;
        gvty = Math.round(Float.valueOf(data[6]) * 1e4) * 0.0001f;
        gvtz = Math.round(Float.valueOf(data[7]) * 1e4) * 0.0001f;
        gvt_accu = Math.round(Float.valueOf(data[8]) * 1e2) * 0.01f;
        gyrx = Math.round(Float.valueOf(data[9]) * 1e4) * 0.0001f;
        gyry = Math.round(Float.valueOf(data[10]) * 1e4) * 0.0001f;
        gyrz = Math.round(Float.valueOf(data[11]) * 1e4) * 0.0001f;
        gyr_accu = Math.round(Float.valueOf(data[12]) * 1e2) * 0.01f;
        accx = Math.round(Float.valueOf(data[13]) * 1e4) * 0.0001f;
        accy = Math.round(Float.valueOf(data[14]) * 1e4) * 0.0001f;
        accz = Math.round(Float.valueOf(data[15]) * 1e4) * 0.0001f;
        acc_accu = Math.round(Float.valueOf(data[16]) * 1e2) * 0.01f;
        magx = Math.round(Float.valueOf(data[17]) * 1e4) * 0.0001f;
        magy = Math.round(Float.valueOf(data[18]) * 1e4) * 0.0001f;
        magz = Math.round(Float.valueOf(data[19]) * 1e4) * 0.0001f;
        mag_accu = Math.round(Float.valueOf(data[20]) * 1e2) * 0.01f;
        ori = Math.round(Float.valueOf(data[21]) * 1e4) * 0.0001f;
        rvx = Math.round(Float.valueOf(data[22]) * 1e4) * 0.0001f;
        rvy = Math.round(Float.valueOf(data[23]) * 1e4) * 0.0001f;
        rvz = Math.round(Float.valueOf(data[24]) * 1e4) * 0.0001f;
        rvs = Math.round(Float.valueOf(data[25]) * 1e4) * 0.0001f;
        rv_head_acc = Math.round(Float.valueOf(data[26]) * 1e2) * 0.01f;
        rv_accu = Math.round(Float.valueOf(data[27]) * 1e2) * 0.01f;
        gamervx = Math.round(Float.valueOf(data[28]) * 1e4) * 0.0001f;
        gamervy = Math.round(Float.valueOf(data[29]) * 1e4) * 0.0001f;
        gamervz = Math.round(Float.valueOf(data[30]) * 1e4) * 0.0001f;
        gamervs = Math.round(Float.valueOf(data[31]) * 1e4) * 0.0001f;
        gamerv_accu = Math.round(Float.valueOf(data[32]) * 1e2) * 0.01f;
        if(data[33].equals("null") || data[34].equals("null")){
            lon = 0.0f;
            lat = 0.0f;
            speed = 0.0f;
            bearing = 0.0f;
        }else{
            lon = Math.round(Float.valueOf(data[33]) * 1e7) * 0.0000001f;
            lat = Math.round(Float.valueOf(data[34]) * 1e7) * 0.0000001f;
            speed = Math.round(Float.valueOf(data[35]) * 1e4) *0.0001f;
            bearing = Math.round(Float.valueOf(data[36]) * 1e4) * 0.0001f;
        }
        ts = Long.valueOf(data[37])/1000;
        if(data.length > 39){
            if(data[38].equals("null")){
                gps_accu = 999.0f;
            }else gps_accu = Math.round(Float.valueOf(data[38]) * 1e1)* 0.1f;
            step = Math.round(Float.valueOf(data[39]) * 1e1) * 0.1f;
        }
        else if(data.length > 38){
            step = Math.round(Float.valueOf(data[38]) * 1e1) * 0.1f;
            gps_accu = 999.0f;
        }else{
            step = 0;
            gps_accu = 999.0f;
        }
    }

    public SenUnitV2(){}
}
