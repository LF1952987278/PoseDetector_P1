package com.bjtu.pose.pose;//import constant.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entry {
//    public static void main(String[] args) {
//        train();
////        test();
////        predict();
//
////        predictMom();
////        TrainSVMV0.Companion.trainModel();
////        TestSVMV0.Companion.testModel();
////        predictV0();
//    }
//
//    public static void train() {
//        TrainSVM.Companion.trainModel();
//    }
//
//    public static void test() {
//        TestSVM.Companion.testModel();
//    }
//
//    public static void predictV0() {
//        String fileName = Constants.drivingPath + "/phone1/user8/2022-11-06 16-29-16.csv";
////        String fileName = Constant.drivingPath + "/phone2/user4/2022-11-06 16-27-28.csv";
////        String fileName = Constant.walkingPath + "/bag/phone1/user8/2022-11-06 15-36-17.csv";
////        String fileName = Constant.walkingPath + "/bag/phone2/user4/2022-11-06 15-34-12.csv";
////        String fileName = Constant.walkingPath + "/calling/phone1/user8/2022-11-06 15-58-22.csv";
////        String fileName = Constant.walkingPath + "/calling/phone4/user27/2022-11-06 16-02-40.csv";
//        List<Pair<String, Double>> result = new PredictV0().predict(parseSenFromFile(fileName));
//        for(Pair<String, Double> item: result) {
//            System.out.println(item.getKey() + ": " + item.getValue());
//        }
//    }
//
//    public static void predict() {
////        String fileName = Constant.flatPath + "/phone2/user4/2022-04-07 15-34-59.csv";
////        String fileName = Constant.callingPath + "/phone1/user4/2022-05-03 16-50-06.csv";
////        String fileName = Constant.bagPath + "/phone2/user20/2022-08-11 19-26-53.csv";
////        String fileName = Constant.bagPath + "/phone2/user20/2022-08-11 19-32-18.csv";
////        String fileName = Constant.bagPath + "/phone2/user20/2022-08-11 19-50-19.csv";
////        String fileName = Constant.pocketPath + "/phone1/user4/2022-05-03 17-08-46.csv";
////        String fileName = "src/dataset/2022-11-03 09-51-44.csv";
////        String fileName = "src/dataset/2023-02-23 15-49-24.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-22 23-21-02.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-23 15-27-10.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-23 15-49-24.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-23 16-20-59.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-23 16-30-50.csv";
////        String fileName = "src/dataset/phone1-shenzhen/2023-02-23 16-33-15.csv";
//
//        String fileName = "src/dataset/221106_capmall_recovery/driving/phone1/user8/2022-11-06 16-35-04.csv";
//
//
//
//        // First use
////        List<String> result = Predict.Companion.predict(dp.FuncKt.parseSenFromFile(fileName));
////        System.out.println(result);
//
//        // Second use
////        List<String> result = Predict.Companion.predict(dp.FuncKt.parseSenFromUnits(FuncKt.parseUnitsFromFile(fileName)));
////        System.out.println(result.toString());
//
//        // Second use and more detail
//        ArrayList<SenUnitV2> unitsList = new ArrayList<>(parseUnitsFromFile(fileName));
////        System.out.println("unitsList.size:"+unitsList.size());
//        Map<Long, List<SenUnitV2>> map = parseSenFromUnits(unitsList);
////        System.out.println("map.size:"+map.size());
//        List<Pair<String, Double>> result = new Predict().predict(map);
//        for(Pair<String, Double> item: result) {
//            System.out.println(item.getKey() + ": " + item.getValue());
//        }
//    }
//
//    public static void predictMomV0() {
//        // 下面代码只是一个predictMoment的使用实例，由于csv中units数量远远大于50所以会Error，所以实际要准备40-60之间长度的unitsList
//        String fileName = Constants.walkingPath + "/calling/phone4/user27/2022-11-06 16-02-40.csv";
//        ArrayList<SenUnitV2> unitsList = new ArrayList<>(parseUnitsFromFile(fileName));
//        Pair<String, Double> result = new PredictV0().predictMoment(unitsList);
//    }
//
//
//    public static void predictMom() {
//        // 下面代码只是一个predictMoment的使用实例，由于csv中units数量远远大于50所以会Error，所以实际要准备40-60之间长度的unitsList
//        String fileName = Constants.flatPath + "/phone1/user2/2022-04-07 15-58-41.csv";
//        ArrayList<SenUnitV2> unitsList = new ArrayList<>(parseUnitsFromFile(fileName));
//        Pair<String, Double> result = new Predict().predictMoment(unitsList);
//    }
//
//    public static void fileStream() {
//        String fileName = Constants.flatPath + "/phone1/user2/2022-04-07 15-58-41.csv";
//        ArrayList<SenUnitV2> unitsList = new ArrayList<>(parseUnitsFromFile(fileName));
//
//    }

}
