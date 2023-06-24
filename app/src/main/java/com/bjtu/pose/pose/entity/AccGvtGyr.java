package com.bjtu.pose.pose.entity;

/**
 * * Author: Liu Feng
 * * Date:2023/4/20 15:29
 * Description: 9轴数据
 */
public class AccGvtGyr {

    // 加速度计
    private double accx;
    private double accy;
    private double accz;

    // 重力计
    private double gvtx;
    private double gvty;
    private double gvtz;

    // 陀螺仪
    private double gyrx;
    private double gyry;
    private double gyrz;

    public AccGvtGyr(double accx, double accy, double accz, double gvtx, double gvty, double gvtz, double gyrx, double gyry, double gyrz) {
        this.accx = accx;
        this.accy = accy;
        this.accz = accz;
        this.gvtx = gvtx;
        this.gvty = gvty;
        this.gvtz = gvtz;
        this.gyrx = gyrx;
        this.gyry = gyry;
        this.gyrz = gyrz;
    }

    public double getAccx() {
        return accx;
    }

    public void setAccx(double accx) {
        this.accx = accx;
    }

    public double getAccy() {
        return accy;
    }

    public void setAccy(double accy) {
        this.accy = accy;
    }

    public double getAccz() {
        return accz;
    }

    public void setAccz(double accz) {
        this.accz = accz;
    }

    public double getGvtx() {
        return gvtx;
    }

    public void setGvtx(double gvtx) {
        this.gvtx = gvtx;
    }

    public double getGvty() {
        return gvty;
    }

    public void setGvty(double gvty) {
        this.gvty = gvty;
    }

    public double getGvtz() {
        return gvtz;
    }

    public void setGvtz(double gvtz) {
        this.gvtz = gvtz;
    }

    public double getGyrx() {
        return gyrx;
    }

    public void setGyrx(double gyrx) {
        this.gyrx = gyrx;
    }

    public double getGyry() {
        return gyry;
    }

    public void setGyry(double gyry) {
        this.gyry = gyry;
    }

    public double getGyrz() {
        return gyrz;
    }

    public void setGyrz(double gyrz) {
        this.gyrz = gyrz;
    }

    @Override
    public String toString() {
        return "AccGvtGyr{" +
                "accx=" + accx +
                ", accy=" + accy +
                ", accz=" + accz +
                ", gvtx=" + gvtx +
                ", gvty=" + gvty +
                ", gvtz=" + gvtz +
                ", gyrx=" + gyrx +
                ", gyry=" + gyry +
                ", gyrz=" + gyrz +
                '}';
    }
}
