package com.bjtu.pose.pose.entity;

/**
 * * Author: Liu Feng
 * * Date:2023/4/20 15:44
 * * Description 表示 acc 或 gvt 或 gyr 的xyz三元素
 */
public class XYZParent {

    private double xValue;
    private double yValue;
    private double zValue;

    public XYZParent(double xValue, double yValue, double zValue) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.zValue = zValue;
    }

    public double getxValue() {
        return xValue;
    }

    public void setxValue(double xValue) {
        this.xValue = xValue;
    }

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getzValue() {
        return zValue;
    }

    public void setzValue(double zValue) {
        this.zValue = zValue;
    }

    @Override
    public String toString() {
        return "XYZParent{" +
                "xValue=" + xValue +
                ", yValue=" + yValue +
                ", zValue=" + zValue +
                '}';
    }
}
