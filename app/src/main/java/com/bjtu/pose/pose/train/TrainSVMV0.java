package com.bjtu.pose.pose.train;

import static com.bjtu.pose.pose.utils.Functions.accuracy;
import static com.bjtu.pose.pose.utils.Functions.getReLabels;
import static com.bjtu.pose.pose.utils.Functions.jointSenUnit2FtrStrV0;
import static com.bjtu.pose.pose.utils.Functions.listDataSetFilesV0;
import static com.bjtu.pose.pose.utils.Functions.parseSenFromFile;

import com.bjtu.pose.pose.entity.Constants;
import com.bjtu.pose.pose.entity.FtrElem;
import com.bjtu.pose.pose.entity.RawElem;
import com.bjtu.pose.pose.entity.RawFtrLists;
import com.bjtu.pose.pose.entity.SenUnitV2;
import com.bjtu.pose.pose.svm.SvmProcessV0;
import com.bjtu.pose.pose.utils.Object2File;

import smile.classification.SVM;

import java.util.*;


/**
 * * Author: Liu Feng
 * * Date:2023/4/22 10:11
 * * Description
 */
public class TrainSVMV0 {

    static class companion {
        static void trainModel() {
            // All Params
            System.out.println("seq len = "+ Constants.seqLen +", stride = "+Constants.stride+", reject sec = "+Constants.rejectSec+".");

            Map<String, List<String>> pathMap = new HashMap<>();
            List<String> list1 = new ArrayList<>();
            list1.add(Constants.drivingPath);
            pathMap.put(Constants.DRIVING, list1);
            list1.clear();
            list1.add(Constants.walkingPath);
            list1.add(Constants.flatPath+"/phone1");
            list1.add(Constants.pocketPath+"/phone2");
            list1.add(Constants.flatPath+"/phone4");
            list1.add(Constants.pocketPath+"/phone5");
            pathMap.put(Constants.WALKING, list1);

            List<RawElem> sampleTrainRaw = new ArrayList<>();  // n * (5*3*3)
            List<FtrElem> sampleTrainFtr = new ArrayList<>();  // n * (5*3*3)
            List<List<RawElem>> sampleTestRaw = new ArrayList<>(); // m * n * (5*3*3)
            List<List<FtrElem>> sampleTestFtr = new ArrayList<>(); // m * n * (5*3*3)
            List<String> fileNameLs = new ArrayList<>();

            pathMap.forEach((key,value)->{
                for (String _path : value) {
                    Map<String, List<String>> filePathMap = listDataSetFilesV0(_path);//
                    filePathMap.forEach((k,v)->{
                        System.out.println("---------------"+k+"-------------------------");
                        for(String filePath : v) {
                            System.out.println("read "+filePath);
                            Map<Long, List<SenUnitV2>> tsSenMap = parseSenFromFile(filePath);//
                            RawFtrLists rawFtrItem = jointSenUnit2FtrStrV0(tsSenMap, Constants.seqLen, Constants.stride, Constants.rejectSec, key);
                            List<RawElem> raw = rawFtrItem.getSrl();
                            List<FtrElem> ftr = rawFtrItem.getSfl();  //   n * (5*3*3)

                            if (ftr.size() != 0) {
                                switch (k) {
                                    case Constants.TRAIN:
                                        sampleTrainRaw.addAll(raw); //   n * (5*3*3)
                                        sampleTrainFtr.addAll(ftr); //   n * (5*3*3)
                                        break;
                                    case Constants.TEST:        // 对于测试集， 会把测试集文件名记录 由代码逻辑可知 一个文件对应sampleTestFtr列表中一个元素
                                        sampleTestRaw.add(raw); // m * n * (5*3*3)
                                        sampleTestFtr.add(ftr); // m * n * (5*3*3)
                                        fileNameLs.add(filePath.trim());
                                        break;
                                }
                            }
                        }
                    });

                }
            });

            System.out.println("sample_train_cnt="+sampleTrainFtr.size());

            // 将训练数据打乱，不能将测试数据打乱，保证文件列表与测试列表的对应关系
            Collections.shuffle(sampleTrainFtr);

            // Train
            List<List<List<List<Double>>>> dataList = new ArrayList<>();  // n* (5*3*3)
            List<Integer> groundTruthList = new ArrayList<>();
            for (FtrElem item : sampleTrainFtr) { // item: (5*3*3)
                dataList.add(item.getContent());
                groundTruthList.add(item.getPoseID());
            }

            // Train data 就是 dataList 的转化
            // Train Truth 就是 groundTruthList 的转化

            System.out.println("------------");
            System.out.println("DataList Length: "+dataList.size()); // 25950
            System.out.println("-------------------");


            // 更改dataList 的数据结构 从 n * (5*3*3) 到 n * 45
            int cal = 0;
            double[][] dataListConver = new double[dataList.size()][];
//            List<List<Double>> dataListConver = new ArrayList<>();
            for (List<List<List<Double>>> item3 : dataList) {
                List<Double> da = new ArrayList<>();
                for (List<List<Double>> item2 : item3) { // 3
                    for(List<Double> item1 : item2) { // 5
                        for (Double item0 : item1) { // 3
                            da.add(item0);
                        }
                    }
                }

                if (da.size() == 45) {
                    dataListConver[cal] = da.stream().mapToDouble(Double::valueOf).toArray();
                    cal += 1;
//                    dataListConver.add(da);
                } else {
                    try {
                        throw new Exception("Very Diff Error Occor! The 45 size is "+da.size());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            System.out.println("Transform Successfully! Cal = "+cal);
            double[][] trainDataV1 = dataListConver;   // This Test OK !!
            int[] groundDataV1 = groundTruthList.stream().mapToInt(Integer::valueOf).toArray();
            SVM<double[]> svm = new SvmProcessV0(trainDataV1, groundDataV1).getSVM();//
            System.out.println("Train Data Process Finished!"); // This test OK !!


            //Test
            List<List<List<List<List<Double>>>>> testDataList = new ArrayList<>();
            List<List<Integer>> testGroundTruthList = new ArrayList<>();
            // sampleTestFtr: m*n*45
            // item: n*45
            for (List<FtrElem> item : sampleTestFtr) {
                List<List<List<List<Double>>>> tmpData = new ArrayList<>();
                List<Integer> tmpTruth = new ArrayList<>();
                // smallItem: 45
                for (FtrElem smallItem : item) {
                    tmpData.add(smallItem.getContent());
                    tmpTruth.add(smallItem.getPoseID());
                }
                testDataList.add(tmpData);
                testGroundTruthList.add(tmpTruth);
            }
            System.out.println("Test Data Process Finished!");

            // Test Process
            List<Integer> allTestSrLabel = new ArrayList<>();
            List<Integer> allTestReLabel = new ArrayList<>();
            List<Integer> allTestTruth = new ArrayList<>();
            for (int i=0; i<testDataList.size(); i++) {
                List<List<Double>> dlc = new ArrayList<>();
                for (List<List<List<Double>>> item3 : testDataList.get(i)) {
                    List<Double> dlcItem = new ArrayList<>();
                    for (List<List<Double>> item2 : item3) {
                        for (List<Double> item1 : item2) {
                            for (Double item0 : item1) {
                                dlcItem.add(item0);
                            }
                        }
                    }

                    if (dlcItem.size() == 45) {
                        dlc.add(dlcItem);
                    } else {
                        try {
                            throw new Exception("LiuFeng: Very Diff Error Occor! The 45 size is ${dlcItem.size}");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                List<List<Double>> testData = dlc;
                int[] testTruth = testGroundTruthList.get(i).stream().mapToInt(Integer::valueOf).toArray();

                // 生成 testLabel testLabel 是我们通过svm来预测的
                // 对于testData中的每一个DoubleArray都生成一个Int值最后形成一个列表
                List<Integer> testLabelList = new ArrayList<>();
                for (List<Double> item : testData) { // item: DoubleArray
                    testLabelList.add(svm.predict(item.stream().mapToDouble(Double::valueOf).toArray()));
                }
                int[] testLabel = testLabelList.stream().mapToInt(Integer::valueOf).toArray();


                // 修正预测到的testLabel以达到更高的预测精度， 得到reTesLabel
                // 将testLabel 的第一个元素作为 列表的第一个元素
                List<Integer> reTesLabel = getReLabels(testLabel);//

//              println("Size of test data is ${testData.size}")
//              println("Size of reTesLabel is ${reTesLabel.size}")

                System.out.println("File: "+fileNameLs.get(i));

//              对testTruth(实际类标签) 和 testlabel（预测类标签）
                System.out.println("Test Set accuracy score: "+accuracy(testTruth, testLabel));
                // 对testTruth（实际类标签） 和 reTesLabel （修改过的预测类标签）
                System.out.println("Re Test Set accuracy score: "+accuracy(testTruth, reTesLabel.stream().mapToInt(Integer::valueOf).toArray()));

                allTestSrLabel.addAll(testLabelList);   //testLabel就是将testLabelList转换为数组，所以此处直接用testLabelList
                allTestReLabel.addAll(reTesLabel);
                allTestTruth.addAll(testGroundTruthList.get(i));     //testTruth就是将testGroundTruthList.get(i)转换为数组，所以此处直接用testGroundTruthList.get(i)
            }

            System.out.println("All Test Sets Accuracy Score: "+accuracy(allTestTruth.stream().mapToInt(Integer::valueOf).toArray(),
                                                                        allTestSrLabel.stream().mapToInt(Integer::valueOf).toArray()));
            System.out.println("All Re Test Sets Accuracy score: "+accuracy(allTestTruth.stream().mapToInt(Integer::valueOf).toArray(),
                                                                            allTestReLabel.stream().mapToInt(Integer::valueOf).toArray()));

            // Save SVM
            Object2File.writeObjectToFile(svm, Constants.svmSavePathV0);
        }
    }

}
