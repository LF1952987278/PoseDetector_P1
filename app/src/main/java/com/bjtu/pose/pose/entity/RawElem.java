package com.bjtu.pose.pose.entity;

import java.util.List;

/**
 * * Author: Liu Feng
 * * Date:2023/4/21 10:54
 * * Description 切片切出来的9元素列表，外列表长度为 下标切片 长度，内列表长度为每一个下标对应的Units长度
 *  *              另存储一个数据对应的poseID
 */
public class RawElem {

    private List<List<AccGvtGyr>> secRawOfElem;
    private int poseID;

    public RawElem(List<List<AccGvtGyr>> secRawOfElem, int poseID) {
        this.secRawOfElem = secRawOfElem;
        this.poseID = poseID;
    }

    public List<List<AccGvtGyr>> getSecRawOfElem() {
        return secRawOfElem;
    }

    public void setSecRawOfElem(List<List<AccGvtGyr>> secRawOfElem) {
        this.secRawOfElem = secRawOfElem;
    }

    public int getPoseID() {
        return poseID;
    }

    public void setPoseID(int poseID) {
        this.poseID = poseID;
    }

    @Override
    public String toString() {
        return "RawElem{" +
                "secRawOfElem=" + secRawOfElem +
                ", poseID=" + poseID +
                '}';
    }
}
