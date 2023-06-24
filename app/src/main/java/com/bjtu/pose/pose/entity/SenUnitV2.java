package com.bjtu.pose.pose.entity;

import java.text.DecimalFormat;

/**
 * * Author: Liu Feng
 * * Date:2023/4/22 10:46
 * * Description 表示csv的一行数据
 *  *            注意本类可继承
 */
public class SenUnitV2 {

    public String line;

    private String ts_ms; // Change to string to save   such as: 1649317829537
    private float laccx;
    private float laccy;
    private float laccz;
    private float lacc_accu;
    private float gvtx;
    private float gvty;
    private float gvtz;
    private float gvt_accu;
    private float gyrx;
    private float gyry;
    private float gyrz;
    private float gyr_accu;
    private float accx;
    private float accy;
    private float accz;
    private float acc_accu;
    private float magx;
    private float magy;
    private float magz;
    private float mag_accu;
    private float ori;
    private float rvx;
    private float rvy;
    private float rvz;
    private float rvs;
    private float rv_head_acc;
    private float rv_accu;
    private float gamervx;
    private float gamervy;
    private float gamervz;
    private float gamervs;
    private float gamerv_accu;
    private float lon;
    private float lat;
    private float speed;
    private float bearing;
    private Long ts;
    private float gps_accu;
    private float step;

    public String getTs_ms() {
        return ts_ms;
    }

    public void setTs_ms(String ts_ms) {
        this.ts_ms = ts_ms;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public float getLaccx() {
        return laccx;
    }

    public void setLaccx(float laccx) {
        this.laccx = laccx;
    }

    public float getLaccy() {
        return laccy;
    }

    public void setLaccy(float laccy) {
        this.laccy = laccy;
    }

    public float getLaccz() {
        return laccz;
    }

    public void setLaccz(float laccz) {
        this.laccz = laccz;
    }

    public float getLacc_accu() {
        return lacc_accu;
    }

    public void setLacc_accu(float lacc_accu) {
        this.lacc_accu = lacc_accu;
    }

    public float getGvtx() {
        return gvtx;
    }

    public void setGvtx(float gvtx) {
        this.gvtx = gvtx;
    }

    public float getGvty() {
        return gvty;
    }

    public void setGvty(float gvty) {
        this.gvty = gvty;
    }

    public float getGvtz() {
        return gvtz;
    }

    public void setGvtz(float gvtz) {
        this.gvtz = gvtz;
    }

    public float getGvt_accu() {
        return gvt_accu;
    }

    public void setGvt_accu(float gvt_accu) {
        this.gvt_accu = gvt_accu;
    }

    public float getGyrx() {
        return gyrx;
    }

    public void setGyrx(float gyrx) {
        this.gyrx = gyrx;
    }

    public float getGyry() {
        return gyry;
    }

    public void setGyry(float gyry) {
        this.gyry = gyry;
    }

    public float getGyrz() {
        return gyrz;
    }

    public void setGyrz(float gyrz) {
        this.gyrz = gyrz;
    }

    public float getGyr_accu() {
        return gyr_accu;
    }

    public void setGyr_accu(float gyr_accu) {
        this.gyr_accu = gyr_accu;
    }

    public float getAccx() {
        return accx;
    }

    public void setAccx(float accx) {
        this.accx = accx;
    }

    public float getAccy() {
        return accy;
    }

    public void setAccy(float accy) {
        this.accy = accy;
    }

    public float getAccz() {
        return accz;
    }

    public void setAccz(float accz) {
        this.accz = accz;
    }

    public float getAcc_accu() {
        return acc_accu;
    }

    public void setAcc_accu(float acc_accu) {
        this.acc_accu = acc_accu;
    }

    public float getMagx() {
        return magx;
    }

    public void setMagx(float magx) {
        this.magx = magx;
    }

    public float getMagy() {
        return magy;
    }

    public void setMagy(float magy) {
        this.magy = magy;
    }

    public float getMagz() {
        return magz;
    }

    public void setMagz(float magz) {
        this.magz = magz;
    }

    public float getMag_accu() {
        return mag_accu;
    }

    public void setMag_accu(float mag_accu) {
        this.mag_accu = mag_accu;
    }

    public float getOri() {
        return ori;
    }

    public void setOri(float ori) {
        this.ori = ori;
    }

    public float getRvx() {
        return rvx;
    }

    public void setRvx(float rvx) {
        this.rvx = rvx;
    }

    public float getRvy() {
        return rvy;
    }

    public void setRvy(float rvy) {
        this.rvy = rvy;
    }

    public float getRvz() {
        return rvz;
    }

    public void setRvz(float rvz) {
        this.rvz = rvz;
    }

    public float getRvs() {
        return rvs;
    }

    public void setRvs(float rvs) {
        this.rvs = rvs;
    }

    public float getRv_head_acc() {
        return rv_head_acc;
    }

    public void setRv_head_acc(float rv_head_acc) {
        this.rv_head_acc = rv_head_acc;
    }

    public float getRv_accu() {
        return rv_accu;
    }

    public void setRv_accu(float rv_accu) {
        this.rv_accu = rv_accu;
    }

    public float getGamervx() {
        return gamervx;
    }

    public void setGamervx(float gamervx) {
        this.gamervx = gamervx;
    }

    public float getGamervy() {
        return gamervy;
    }

    public void setGamervy(float gamervy) {
        this.gamervy = gamervy;
    }

    public float getGamervz() {
        return gamervz;
    }

    public void setGamervz(float gamervz) {
        this.gamervz = gamervz;
    }

    public float getGamervs() {
        return gamervs;
    }

    public void setGamervs(float gamervs) {
        this.gamervs = gamervs;
    }

    public float getGamerv_accu() {
        return gamerv_accu;
    }

    public void setGamerv_accu(float gamerv_accu) {
        this.gamerv_accu = gamerv_accu;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public float getGps_accu() {
        return gps_accu;
    }

    public void setGps_accu(float gps_accu) {
        this.gps_accu = gps_accu;
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public SenUnitV2(String line) {
        if (line.contains("null")) {
            String[] items = line.trim().split(",");
            this.line = line;
            this.ts_ms = items[0];
            this.laccx = Math.round(Float.valueOf(items[1]) * 1e4) * 0.0001f;
            this.laccy = Math.round(Float.valueOf(items[2]) * 1e4) * 0.0001f;
            this.laccz = Math.round(Float.valueOf(items[3]) * 1e4) * 0.0001f;
            this.lacc_accu = Math.round(Float.valueOf(items[4]) * 1e2) * 0.01f;
            this.gvtx = Math.round(Float.valueOf(items[5]) * 1e4) * 0.0001f;
            this.gvty = Math.round(Float.valueOf(items[6]) * 1e4) * 0.0001f;
            this.gvtz = Math.round(Float.valueOf(items[7]) * 1e4) * 0.0001f;
            this.gvt_accu = Math.round(Float.valueOf(items[8]) * 1e2) * 0.01f;
            this.gyrx = Math.round(Float.valueOf(items[9]) * 1e4) * 0.0001f;
            this.gyry = Math.round(Float.valueOf(items[10]) * 1e4) * 0.0001f;
            this.gyrz = Math.round(Float.valueOf(items[11]) * 1e4) * 0.0001f;
            this.gyr_accu = Math.round(Float.valueOf(items[12]) * 1e2) * 0.01f;
            this.accx = Math.round(Float.valueOf(items[13]) * 1e4) * 0.0001f;
            this.accy = Math.round(Float.valueOf(items[14]) * 1e4) * 0.0001f;
            this.accz = Math.round(Float.valueOf(items[15]) * 1e4) * 0.0001f;
            this.acc_accu = Math.round(Float.valueOf(items[16]) * 1e2) * 0.01f;
            this.magx = Math.round(Float.valueOf(items[17]) * 1e4) * 0.0001f;
            this.magy = Math.round(Float.valueOf(items[18]) * 1e4) * 0.0001f;
            this.magz = Math.round(Float.valueOf(items[19]) * 1e4) * 0.0001f;
            this.mag_accu = Math.round(Float.valueOf(items[20]) * 1e2) * 0.01f;
            this.ori = Math.round(Float.valueOf(items[21]) * 1e4) * 0.0001f;
            this.rvx = Math.round(Float.valueOf(items[22]) * 1e4) * 0.0001f;
            this.rvy = Math.round(Float.valueOf(items[23]) * 1e4) * 0.0001f;
            this.rvz = Math.round(Float.valueOf(items[24]) * 1e4) * 0.0001f;
            this.rvs = Math.round(Float.valueOf(items[25]) * 1e4) * 0.0001f;
            this.rv_head_acc = Math.round(Float.valueOf(items[26]) * 1e2) * 0.01f;
            this.rv_accu = Math.round(Float.valueOf(items[27]) * 1e2) * 0.01f;
            this.gamervx = Math.round(Float.valueOf(items[28]) * 1e4) * 0.0001f;
            this.gamervy = Math.round(Float.valueOf(items[29]) * 1e4) * 0.0001f;
            this.gamervz = Math.round(Float.valueOf(items[30]) * 1e4) * 0.0001f;
            this.gamervs = Math.round(Float.valueOf(items[31]) * 1e4) * 0.0001f;
            this.gamerv_accu = Math.round(Float.valueOf(items[32]) * 1e2) * 0.01f;
            if(items[33].equals("null") || items[34].equals("null")){
                this.lon = 0.0f;
                this.lat = 0.0f;
                this.speed = 0.0f;
                this.bearing = 0.0f;
            } else {
                this.lon = Math.round(Float.valueOf(items[33]) * 1e7) * 0.0000001f;
                this.lat = Math.round(Float.valueOf(items[34]) * 1e7) * 0.0000001f;
                this.speed = Math.round(Float.valueOf(items[35]) * 1e4) *0.0001f;
                this.bearing = Math.round(Float.valueOf(items[36]) * 1e4) * 0.0001f;
            }
//            this.lon = 0.0;
//            this.lat = 0.0;
//            this.speed = 0.0;
//            this.bearing = 0.0;
            this.ts = Long.valueOf(items[37])/1000;
            if(items.length > 39){
                if(items[38].equals("null")){
                    this.gps_accu = 999.0f;
                }else this.gps_accu = Math.round(Float.valueOf(items[38]) * 1e1)* 0.1f;
                this.step = Math.round(Float.valueOf(items[39]) * 1e1) * 0.1f;
            }
            else if(items.length > 38){
                this.step = Math.round(Float.valueOf(items[38]) * 1e1) * 0.1f;
                this.gps_accu = 999.0f;
            }else{
                this.step = 0;
                this.gps_accu = 999.0f;
            }


//            this.gps_accu = 0.0f;
//            this.step = 0.0f;
//                ts = floor(BigDecimal(BigInteger(items[37])).divide(BigDecimal(1000)).toDouble()).toLong()
//            if (items[0].length() == 13) {
//                ts = Long.valueOf(items[0]) / 1000;
//            } else if (items[0].length() == 10) {
//                ts = Long.valueOf(items[0]);
//            } else {
//                try {
//                    throw new Exception("LiuFeng: System time is not correct!");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
        } else {
            String[] items = line.trim().split(",");//
            this.line = line;
            ts_ms = items[0];
            this.laccx = Math.round(Float.valueOf(items[1]) * 1e4) * 0.0001f;
            this.laccy = Math.round(Float.valueOf(items[2]) * 1e4) * 0.0001f;
            this.laccz = Math.round(Float.valueOf(items[3]) * 1e4) * 0.0001f;
            this.lacc_accu = Math.round(Float.valueOf(items[4]) * 1e2) * 0.01f;
            this.gvtx = Math.round(Float.valueOf(items[5]) * 1e4) * 0.0001f;
            this.gvty = Math.round(Float.valueOf(items[6]) * 1e4) * 0.0001f;
            this.gvtz = Math.round(Float.valueOf(items[7]) * 1e4) * 0.0001f;
            this.gvt_accu = Math.round(Float.valueOf(items[8]) * 1e2) * 0.01f;
            this.gyrx = Math.round(Float.valueOf(items[9]) * 1e4) * 0.0001f;
            this.gyry = Math.round(Float.valueOf(items[10]) * 1e4) * 0.0001f;
            this.gyrz = Math.round(Float.valueOf(items[11]) * 1e4) * 0.0001f;
            this.gyr_accu = Math.round(Float.valueOf(items[12]) * 1e2) * 0.01f;
            this.accx = Math.round(Float.valueOf(items[13]) * 1e4) * 0.0001f;
            this.accy = Math.round(Float.valueOf(items[14]) * 1e4) * 0.0001f;
            this.accz = Math.round(Float.valueOf(items[15]) * 1e4) * 0.0001f;
            this.acc_accu = Math.round(Float.valueOf(items[16]) * 1e2) * 0.01f;
            this.magx = Math.round(Float.valueOf(items[17]) * 1e4) * 0.0001f;
            this.magy = Math.round(Float.valueOf(items[18]) * 1e4) * 0.0001f;
            this.magz = Math.round(Float.valueOf(items[19]) * 1e4) * 0.0001f;
            this.mag_accu = Math.round(Float.valueOf(items[20]) * 1e2) * 0.01f;
            this.ori = Math.round(Float.valueOf(items[21]) * 1e4) * 0.0001f;
            this.rvx = Math.round(Float.valueOf(items[22]) * 1e4) * 0.0001f;
            this.rvy = Math.round(Float.valueOf(items[23]) * 1e4) * 0.0001f;
            this.rvz = Math.round(Float.valueOf(items[24]) * 1e4) * 0.0001f;
            this.rvs = Math.round(Float.valueOf(items[25]) * 1e4) * 0.0001f;
            this.rv_head_acc = Math.round(Float.valueOf(items[26]) * 1e2) * 0.01f;
            this.rv_accu = Math.round(Float.valueOf(items[27]) * 1e2) * 0.01f;
            this.gamervx = Math.round(Float.valueOf(items[28]) * 1e4) * 0.0001f;
            this.gamervy = Math.round(Float.valueOf(items[29]) * 1e4) * 0.0001f;
            this.gamervz = Math.round(Float.valueOf(items[30]) * 1e4) * 0.0001f;
            this.gamervs = Math.round(Float.valueOf(items[31]) * 1e4) * 0.0001f;
            this.gamerv_accu = Math.round(Float.valueOf(items[32]) * 1e2) * 0.01f;
            this.lon = Math.round(Float.valueOf(items[33]) * 1e2) * 0.01f;
            this.lat = Math.round(Float.valueOf(items[34]) * 1e2) * 0.01f;
            this.speed = Math.round(Float.valueOf(items[35]) * 1e2) * 0.01f;
            this.bearing = Math.round(Float.valueOf(items[36]) * 1e2) * 0.01f;
//                ts = floor(BigDecimal(BigInteger(items[37])).divide(BigDecimal(1000)).toDouble()).toLong()
            this.ts = Long.valueOf(items[37])/1000;
            if(items.length > 39){
                if(items[38].equals("null")){
                    this.gps_accu = 999.0f;
                }else this.gps_accu = Math.round(Float.valueOf(items[38]) * 1e1)* 0.1f;
                this.step = Math.round(Float.valueOf(items[39]) * 1e1) * 0.1f;
            }
            else if(items.length > 38){
                this.step = Math.round(Float.valueOf(items[38]) * 1e1) * 0.1f;
                this.gps_accu = 999.0f;
            }else{
                this.step = 0;
                this.gps_accu = 999.0f;
            }


//            if (items[0].length() == 13) {
//                ts = Long.valueOf(items[0]) / 1000;
//            } else if (items[0].length() == 10) {
//                ts = Long.valueOf(items[0]);
//            } else {
//                try {
//                    throw new Exception("LiuFeng: System time is not correct!" + items[0].length());
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }

//            switch (items.length) {
//                case 38:        // step and gps_accu is not the last
//                    this.gps_accu = 0.0f;
//                    this.step = 0.0f;
//                    break;
//                case 39:        // Only Step last
//                    gps_accu = 0.0f;
//                    step = Double.parseDouble(new DecimalFormat(".0").format(Double.parseDouble(items[38])));
//                    break;
//                case 40:        // gps_accu and step last
//                    gps_accu = Double.parseDouble(new DecimalFormat(".0").format(Double.parseDouble(items[38])));
//                    step = Double.parseDouble(new DecimalFormat(".0").format(Double.parseDouble(items[39])));
//                    break;
//                default:
//                    try {
//                        throw new Exception("LiuFeng: The cols num of csv files is not 38/39/40!");
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//            }
        }

    }

    public SenUnitV2() {
    }
}
