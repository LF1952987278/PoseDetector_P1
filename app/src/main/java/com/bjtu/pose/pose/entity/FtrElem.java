package com.bjtu.pose.pose.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * * Author: Liu Feng
 * * Date:2023/4/20 15:32
 * Description: acc、gvt、gyr分别包含5 * 3，此数据结构表示 （3*5*3 + 1）一个切片对应一个FtrElem
 */
public class FtrElem {

    private List<List<Double>> accFtrOfElem;
    private List<List<Double>> gvtFtrOfElem;
    private List<List<Double>> gyrFtrOfElem;
    private int poseID;

    public FtrElem(List<List<Double>> accFtrOfElem, List<List<Double>> gvtFtrOfElem, List<List<Double>> gyrFtrOfElem, int poseID) {
        this.accFtrOfElem = accFtrOfElem;
        this.gvtFtrOfElem = gvtFtrOfElem;
        this.gyrFtrOfElem = gyrFtrOfElem;
        this.poseID = poseID;
    }

    public List<List<Double>> getAccFtrOfElem() {
        return accFtrOfElem;
    }

    public void setAccFtrOfElem(List<List<Double>> accFtrOfElem) {
        this.accFtrOfElem = accFtrOfElem;
    }

    public List<List<Double>> getGvtFtrOfElem() {
        return gvtFtrOfElem;
    }

    public void setGvtFtrOfElem(List<List<Double>> gvtFtrOfElem) {
        this.gvtFtrOfElem = gvtFtrOfElem;
    }

    public List<List<Double>> getGyrFtrOfElem() {
        return gyrFtrOfElem;
    }

    public void setGyrFtrOfElem(List<List<Double>> gyrFtrOfElem) {
        this.gyrFtrOfElem = gyrFtrOfElem;
    }

    public int getPoseID() {
        return poseID;
    }

    public void setPoseID(int poseID) {
        this.poseID = poseID;
    }

    @Override
    public String toString() {
        return "FtrElem{" +
                "accFtrOfElem=" + accFtrOfElem +
                ", gvtFtrOfElem=" + gvtFtrOfElem +
                ", gyrFtrOfElem=" + gyrFtrOfElem +
                ", poseID=" + poseID +
                '}';
    }

    /**
     * @return 返回 3 * 5 * 3
     */
    public List<List<List<Double>>> getContent() {
        List<List<List<Double>>> content = new ArrayList<>();
        content.add(accFtrOfElem);
        content.add(gvtFtrOfElem);
        content.add(gyrFtrOfElem);
        return content;
    }

}
