package com.bjtu.pose.pose.entity;

import java.util.List;

/**
 * * Author: Liu Feng
 * * Date:2023/4/21 10:53
 * * Description 分别存储 RawElem 的列表 和 FtrElem 的列表
 */
public class RawFtrLists {

    List<RawElem> srl;
    List<FtrElem> sfl;

    public RawFtrLists(List<RawElem> srl, List<FtrElem> sfl) {
        this.srl = srl;
        this.sfl = sfl;
    }

    public List<RawElem> getSrl() {
        return srl;
    }

    public void setSrl(List<RawElem> srl) {
        this.srl = srl;
    }

    public List<FtrElem> getSfl() {
        return sfl;
    }

    public void setSfl(List<FtrElem> sfl) {
        this.sfl = sfl;
    }

    @Override
    public String toString() {
        return "RawFtrLists{" +
                "srl=" + srl +
                ", sfl=" + sfl +
                '}';
    }
}
