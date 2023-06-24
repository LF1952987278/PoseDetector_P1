package com.bjtu.pose.pose.entity;

/**
 * * Author: Liu Feng
 * * Date:2023/4/20 15:27
 * Description: 常量定义
 */
public class Constants {

    // These consts can not change
    public static final int OUTDOOR = 0;
    public static final int INDOOR = 1;
    public static final String FLAT = "Flat";
    public static final String CALLING = "Calling";
    public static final String POCKET = "Pocket";
    public static final String BAG = "Bag";
    public static final String ALL = "all";
    public static final String TRAIN = "train";
    public static final String TEST = "test";

    public static final String DRIVING = "driving";
    public static final String WALKING = "walking";
    public static final String UNKNOWN = "unknown";


    // These consts can modify to satisfy our needs
    public static final String phonePose = "all";
    public static final int seqLen = 5;
    public static final int stride = 1;
    public static final int rejectSec = 3;
    public static final double ratioTrain = 0.8;
    // ftr_version0
    // level
    public static final String flatPath = "src/dataset/220407_Wground-recovery"; // 55
    public static final String callingPath = "src/dataset/220503_Wground-recovery/calling"; // 50
    public static final String pocketPath = "src/dataset/220503_Wground-recovery/pocket";  // 50
    public static final String bagPath = "src/dataset/220802_multi-pose-recovery/bag"; // 50

    public static final String drivingPath = "src/dataset/221106_capmall-recovery/driving";
    public static final String walkingPath = "src/dataset/221106_capmall-recovery/walking";

    public static final String svmSavePath = "src/model/svm.model";
    public static final String svmReadPath = "/storage/emulated/0/DataCollect/svm.model";

    public static final String svmSavePathV0 = "src/model/svmV0.model";
    public static final String svmReadPathV0 = "/storage/emulated/0/DataCollect/svmV0.model";

    public static final int place = OUTDOOR;

    //This is for driving/walking/unknown bound
    public static final double boundV0 = 0.80;

    // This is for flat/calling/bag/pocket bound
    public static final double bound = 0.82;

}
