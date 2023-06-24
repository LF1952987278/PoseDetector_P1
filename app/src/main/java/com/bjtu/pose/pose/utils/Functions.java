package com.bjtu.pose.pose.utils;

import android.annotation.SuppressLint;

import com.bjtu.pose.PoseActivity;
import com.bjtu.pose.pose.entity.*;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * * Author: Liu Feng
 * * Date:2023/4/20 16:01
 * * Description 常用方法
 */
public class Functions {

    /**
     * @param truth 真实值数组
     * @param label 预测值数组
     * @return 预测准确度
     */
    public static Double accuracy(int[] truth, int[] label) {
        if (truth.length != label.length) {
            System.out.println("数组长度不一致!");
            try {
                throw new Exception("Array Size Not The Same!!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        double judge = 0.0;
        for (int i=0; i<truth.length; i++) {
            if (truth[i] == label[i]) {
                judge += 1.0;
            }
        }
        return (judge / truth.length);
    }

    /**
     * @param testLabel 预测得到的label列表
     * @return 修正过的label列表
     * Decription: 修正预测得到的label列表
     */
    public static List<Integer> getReLabels(int[] testLabel) {
        // 修正预测到的testLabel以达到更高的预测精度， 得到reTesLabel
        // 将 testLabel 的第一个元素作为 列表的第一个元素
        List<Integer> reTesLabel = new ArrayList<>();
        reTesLabel.add(testLabel[0]);
        for (int i=1; i<testLabel.length-2; i++) {
            if (testLabel[i] != reTesLabel.get(i-1)) {
                if (testLabel[i] == testLabel[i + 1] && testLabel[i] == testLabel[i + 2]) {
                    reTesLabel.add(testLabel[i]);
                } else {
                    reTesLabel.add(reTesLabel.get(i - 1));
                }
            } else {
                reTesLabel.add(testLabel[i]);
            }
        }

        reTesLabel.add(reTesLabel.get(reTesLabel.size()-2));
        reTesLabel.add(reTesLabel.get(reTesLabel.size()-1));

        return reTesLabel;
    }

    /**
     * @param dft_path csv数据文件夹路径
     * @return 返回一个key分别为train和test的Map,指定训练数据文件(csv)路径列表和测试数据文件(csv)路径列表
     * Description: 用于步行与行车的二分类
     */
    public static Map<String, List<String>> listDataSetFilesV0(String dft_path) {
        // 得到文件列表
        List<String> datasetFilePathList = ComTools.traverse_dir(dft_path, "csv");
        Collections.shuffle(datasetFilePathList);

        List<String> trainFilePathList = new ArrayList<>();
        List<String> testFilePathList = new ArrayList<>();

        for (int i=0; i<datasetFilePathList.size(); i++) {
            if ((i+1) % 4 == 0) {
                testFilePathList.add(datasetFilePathList.get(i));
            } else {
                trainFilePathList.add(datasetFilePathList.get(i));
            }
        }

        Map<String, List<String>> dataSetV0 = new HashMap<>();
        dataSetV0.put(Constants.TRAIN, trainFilePathList);
        dataSetV0.put(Constants.TEST, testFilePathList);
        return dataSetV0;
    }

    /**
     * @param dft_path csv数据文件夹路径
     * @param POSE 姿态
     * @return 返回一个key分别为train和test的Map,指定训练数据文件(csv)路径列表和测试数据文件(csv)路径列表
     * Description: 用于姿态检测的多分类
     */
    @SuppressLint("SuspiciousIndentation")
    public static Map<String, List<String>> listDataSetFiles(String dft_path, String POSE) {
        // 得到文件列表
        List<String> datasetFilePathList = ComTools.traverse_dir(dft_path, "csv");

        // 过滤文件列表
        List<String> filteredFilePathList = new ArrayList<>();

        // 过滤
        for (String filepath : datasetFilePathList) {
            if (filepath.contains("phone1") && filepath.contains("user8") && POSE == "flat")
                continue;
            if (filepath.contains("phone4") && filepath.contains("user7") && POSE == "flat") {
                if (filepath.contains("2022-04-07 15-02-25") || filepath.contains("2022-04-07 15-05-58")
                        || filepath.contains("2022-04-07 15-10-07") || filepath.contains("2022-04-07 14-59-16"))
                continue;
            }
            if (filepath.contains("phone5") && filepath.contains("user3") && POSE == "flat") {
                if (filepath.contains("2022-04-07 16-40-41") || filepath.contains("2022-04-07 16-44-35")
                        || filepath.contains("2022-04-07 16-48-17") || filepath.contains("2022-04-07 16-35-32"))
                continue;
            }
            if (filepath.contains("phone5") && filepath.contains("user14") && POSE == "flat")
                continue;
            if (filepath.contains("phone1") && filepath.contains("user5") && (POSE == "calling" || POSE == "pocket"))
                continue;
            filteredFilePathList.add(filepath);
        }

        Collections.shuffle(filteredFilePathList);

        List<String> trainFilePathList = new ArrayList<>();
        List<String> testFilePathList = new ArrayList<>();
        for (int i=0; i<filteredFilePathList.size(); i++) {
            if ((i+1) % 4 == 0)
                testFilePathList.add(filteredFilePathList.get(i));
            else
                trainFilePathList.add(filteredFilePathList.get(i));
        }

        Map<String, List<String>> dataSet = new HashMap<>();
        dataSet.put(Constants.TRAIN, trainFilePathList);
        dataSet.put(Constants.TEST, testFilePathList);
        return dataSet;
    }

    /**
     * @param filepath 文件路径(csv)
     * @return 返回一个Map，key为ts值，value为SenUnitV2的列表，tsSenMap可以完整的表示一个csv文件
     * @see SenUnitV2
     * @see IndoorSenUnitV2
     */
    public static Map<Long, List<SenUnitV2>> parseSenFromFile(String filepath) {
        Map<Long, List<SenUnitV2>> tsSenMap = new HashMap<>();
        try {
            URI uri = PoseActivity.class.getResource(filepath).toURI();
            List<String> lineList = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
            for (int i=0; i<lineList.size(); i++) {
                if (i == 0)
                    continue;       // First line reject
                else {
                    if (Constants.place == Constants.OUTDOOR) {
                        SenUnitV2 unit = new SenUnitV2(lineList.get(i)); // 一行数据
                        if (tsSenMap.keySet().contains(unit.getTs())) {
                            List<SenUnitV2> units = tsSenMap.get(unit.getTs());
                            if (units != null) {        // Must not be null
                                units.add(unit);
                            }
                            tsSenMap.put(unit.getTs(), units);
                        } else { // 不包含此键
                            List<SenUnitV2> units = new ArrayList<>();
                            units.add(unit);
                            tsSenMap.put(unit.getTs(), units);
                        }
                    } else if (Constants.place == Constants.INDOOR) {
                        IndoorSenUnitV2 unit = new IndoorSenUnitV2(lineList.get(i)); // 一行数据
                        if (tsSenMap.keySet().contains(unit.getTs())) {
                            List<SenUnitV2> units = tsSenMap.get(unit.getTs());
                            if (units != null) {        // Must not be null
                                units.add(unit);
                            }
                            tsSenMap.put(unit.getTs(), units);
                        } else { // 不包含此键
                            List<SenUnitV2> units = new ArrayList<>();
                            units.add(unit);
                            tsSenMap.put(unit.getTs(), units);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        return tsSenMap;
    }

    /**
     * @param unitsList Units列表
     * @return 返回一个Map，key为ts值，value为SenUnitV2的列表，tsSenMap可以完整的表示一个csv文件
     * @see SenUnitV2 一行数据
     * @see IndoorSenUnitV2
     */
    public static Map<Long, List<SenUnitV2>> parseSenFromUnits(List<SenUnitV2> unitsList) {
        Map<Long, List<SenUnitV2>> tsSenMap = new HashMap<>();
        for (SenUnitV2 unit : unitsList) {
//            System.out.println(unit.getTs());
            if (tsSenMap.keySet().contains(unit.getTs())) {
                List<SenUnitV2> units = tsSenMap.get(unit.getTs());
                if (units != null) {        // Must not be null
                    units.add(unit);
                }
                tsSenMap.put(unit.getTs(), units);
            } else { // 不包含此键
                List<SenUnitV2> units = new ArrayList<>();
                units.add(unit);
                tsSenMap.put(unit.getTs(), units);
            }
        }
        return tsSenMap;
    }

    /**
     * @param filepath 文件路径(csv)
     * @return SenUnitV2的列表
     * @see SenUnitV2 一行数据
     * @see IndoorSenUnitV2
     */
    public static List<SenUnitV2> parseUnitsFromFile(String filepath) {
        List<SenUnitV2> unitLs = new ArrayList<>();
        try {
            URI uri = PoseActivity.class.getResource(filepath).toURI();
            List<String> lineList = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
//            System.out.println("lineList:"+lineList);
            for (int i=0; i<lineList.size(); i++) {
                if (i == 0)
                    continue;
                else {
                    switch (Constants.place) {
                        case Constants.OUTDOOR:
                            unitLs.add(new SenUnitV2(lineList.get(i)));
                            break;
                        case Constants.INDOOR:
                            unitLs.add(new IndoorSenUnitV2(lineList.get(i)));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return unitLs;
    }

    /**
     * @param ftrArr acc/gvt/gyr 的xyz三元组的列表
     * @return 5*3 5分别表示Max/Min/Mean/Median/Std 3表示xyz
     * @see XYZParent
     * Description: 传入一个xyz列表，分别对x列、y列、z列求Max/Min/Mean/Median/Std
     *              特别注意： 传入featureGenerator必然是一个时间窗口的数据，也就是 Len * 50 长度的数据
     *              实时预测 不使用切片， 但可能传入一个时间窗口的数据
     *              非实时预测如历史数据预测、长时间数据预测、文件数据预测等含有长时间流的数据需要使用切片来多次预测
     *              切片的目的是分而治之，分时间片后分别预测，得到预测列表，预测列表的长度等于切片数量
     *              举例：实时预测，传入1s数据，那么不切片，将 1 * 50 的数据传入本函数得到特征值，再使用svm直接预测
     *                   实时预测，传入5s数据，那么不切片，将 5 * 50 的数据传入本函数得到特征值，再使用svm直接预测
     *                   文件历史数据预测， 切片后对于每个切片，将（切片长度 * 50）的数据传入本函数得到特征值，分别使用svm预测
     */
    public static List<List<Double>> featureGenerator(List<XYZParent> ftrArr) {
        try {
            if (ftrArr.size() == 0)
                throw new Exception("ftrArr列表为空!");

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        List<Double> zList = new ArrayList<>();

        // The length of xList is ftrArr length
        for (int i=0; i<ftrArr.size(); i++) {
            xList.add(ftrArr.get(i).getxValue());
            yList.add(ftrArr.get(i).getyValue());
            zList.add(ftrArr.get(i).getzValue());
        }

        List<Double> xyzMinElement = new ArrayList<>();
        xyzMinElement.add(Collections.min(xList));
        xyzMinElement.add(Collections.min(yList));
        xyzMinElement.add(Collections.min(zList));

        List<Double> xyzMaxElement = new ArrayList<>();
        xyzMaxElement.add(Collections.max(xList));
        xyzMaxElement.add(Collections.max(yList));
        xyzMaxElement.add(Collections.max(zList));

        List<Double> xyzMeanElement = new ArrayList<>();
        Double x_sum = 0.0;
        for(Double d:xList) {
            x_sum += d;
        }
        Double x_average = x_sum/xList.size();
        Double y_sum = 0.0;
        for(Double d:yList) {
            y_sum += d;
        }
        Double y_average = y_sum/yList.size();
        Double z_sum = 0.0;
        for(Double d:zList) {
            z_sum += d;
        }
        Double z_average = z_sum/zList.size();
        xyzMeanElement.add(x_average);
        xyzMeanElement.add(y_average);
        xyzMeanElement.add(z_average);

        // Std Value
        Double sum = 0.0;
        for (Double tmp : xList) {
            sum += ((tmp-x_average) * (tmp-x_average));
        }
        Double xStd = Math.sqrt(sum / (xList.size() - 0));

        sum = 0.0;
        for (Double tmp : yList) {
            sum += ((tmp-y_average) * (tmp-y_average));
        }
        Double yStd = Math.sqrt(sum / (yList.size() - 0));

        sum = 0.0;
        for (Double tmp : zList) {
            sum += ((tmp-z_average) * (tmp-z_average));
        }
        Double zStd = Math.sqrt(sum / (zList.size() - 0));

        List<Double> xyzStdElement = new ArrayList<>();
        xyzStdElement.add(xStd);
        xyzStdElement.add(yStd);
        xyzStdElement.add(zStd);


        // Median Value
        List<Double> xyzMedianElement = new ArrayList<>();
        xyzMedianElement.add(meanMethod(xList));
        xyzMedianElement.add(meanMethod(yList));
        xyzMedianElement.add(meanMethod(zList));

        List<List<Double>> results = new ArrayList<>();
        results.add(xyzMaxElement);
        results.add(xyzMinElement);
        results.add(xyzMeanElement);
        results.add(xyzMedianElement);
        results.add(xyzStdElement);

        return results;
    }

    private static Double meanMethod(List<Double> doubles) {
        Collections.sort(doubles);
        if (doubles.size() % 2 == 0)
            return (doubles.get((doubles.size() / 2) - 1) + doubles.get((doubles.size() / 2))) / 2;
        else
            return doubles.get(doubles.size() / 2);
    }

    /**
     * @param tsSenMap 传入一个Map，key为ts值，value为(SenUnitV2列表)，完整表示一个csv文件的全部数据
     * @param SEQ_LEN 切片长度
     * @param STRIDE 步长
     * @param REJECT_SEC 去掉的头尾长度
     * @param STATE tsSenMap对应的状态：driving or walking
     * @return RawFtrLists
     * @see SenUnitV2
     * @see RawFtrLists
     * Description: 传入一个tsSenMap的Map，通过对key进行切片获取特征值，返回两个List，两个List的长度均为切片个数
     *              用于步行检测
     */
    public static RawFtrLists jointSenUnit2FtrStrV0(Map<Long, List<SenUnitV2>> tsSenMap, int SEQ_LEN, int STRIDE, int REJECT_SEC, String STATE) {
        if ("default".equals(STATE)) {
            STATE = Constants.DRIVING;
        }

        // 姿态分类标签
        Map<String, Integer> poseIdxMap = new HashMap<>();
        poseIdxMap.put(Constants.DRIVING, 0);
        poseIdxMap.put(Constants.WALKING, 1);

        // 获取tsSenMap的key列表，也就是ts值的列表
        List<Long> tsList = tsSenMap.keySet().stream().collect(Collectors.toList());
        Collections.sort(tsList);       // Example:1651567804 - 1651568019

        // Remove first and end some units
        for (int i=0; i<REJECT_SEC; i++) {
            tsList.remove(0);
            tsList.remove(tsList.size()-1);
        }

        List<FtrElem> sampleFtrList = new ArrayList<>();
        List<RawElem> sampleRawList = new ArrayList<>();

        // 指定步长和切片间隔，循环切片（对key列表切片） 循环次数为切片数量
        for (int i=0; i<(tsList.size() - SEQ_LEN); i+=STRIDE) {
            //获取当前key列表切片，长度为(SEQ_LEN + 1)
            List<Long> sampleTSList = tsList.subList(i, i + SEQ_LEN + 1);

            // sec_ftr_list
            // sec_raw_list
            List<List<XYZParent>> accFtrArrList = new ArrayList<>();
            List<List<XYZParent>> gvtFtrArrList = new ArrayList<>();
            List<List<XYZParent>> gyrFtrArrList = new ArrayList<>();
            List<List<AccGvtGyr>> secRawList = new ArrayList<>();

            // 遍历key切片，分别得到当前key值和下一key值对应的 SenUnitV2列表
            for (int j=0; j < (sampleTSList.size() - 1); j++) {
                List<SenUnitV2> crntSenUnitList = tsSenMap.get(sampleTSList.get(j));    // 当前
                List<SenUnitV2> nextSenUnitList = tsSenMap.get(sampleTSList.get(j + 1));    // 下一个

                SenUnitV2 crntHeadSenUnit = null;
                SenUnitV2 nextHeadSenUnit = null;
                if (crntSenUnitList != null)        // Must Not Be Null
                    crntHeadSenUnit = crntSenUnitList.get(0);

                float crntLon = crntHeadSenUnit.getLon();
                float crntLat = crntHeadSenUnit.getLat();
                float crntSpd = crntHeadSenUnit.getSpeed();
                float crntBrng = crntHeadSenUnit.getBearing();

                if (nextSenUnitList != null)        // Must Not Be Null
                    nextHeadSenUnit = nextSenUnitList.get(0);

                float nextLon = nextHeadSenUnit.getLon();
                float nextLat = nextHeadSenUnit.getLat();
                float nextSpd = nextHeadSenUnit.getSpeed();
                float nextBrng = nextHeadSenUnit.getBearing();

                // 如果两点之间距离太远
                if (GeoUtils.haversine_formula(crntLat, crntLon, nextLat, nextLon) > 3.0)
                    continue;

                // 重构到长度为 50
                if (crntSenUnitList.size() >= 40 && crntSenUnitList.size() <= 50) {
                    for (int k=0; k<(50-crntSenUnitList.size()); k++) {
                        crntSenUnitList.add(crntSenUnitList.get(crntSenUnitList.size()-1));
                    }
                } else if (crntSenUnitList.size() >= 51 && crntSenUnitList.size() <= 60) {
                    for (int k=0; k<(crntSenUnitList.size() - 50); k++) {
                        crntSenUnitList.remove(crntSenUnitList.size()-1);
                    }
                } else {
                    continue;
                }

                List<XYZParent> accFtrArr = new ArrayList<>();
                List<XYZParent> gvtFtrArr = new ArrayList<>();
                List<XYZParent> gyrFtrArr = new ArrayList<>();
                List<AccGvtGyr> secRaw = new ArrayList<>();

                // 遍历当前 SenUnitV2列表，分别构建 accxyz列表，gvtxyz列表，gyrxyz列表，9元素列表
                for (SenUnitV2 unit : crntSenUnitList) {
                    accFtrArr.add(new XYZParent(unit.getAccx(), unit.getAccy(), unit.getAccz()));
                    gvtFtrArr.add(new XYZParent(unit.getGvtx(), unit.getGvty(), unit.getGvtz()));
                    gyrFtrArr.add(new XYZParent(unit.getGyrx(), unit.getGyry(), unit.getGyrz()));
                    secRaw.add(new AccGvtGyr(unit.getAccx(), unit.getAccy(), unit.getAccz(),
                                            unit.getGvtx(), unit.getGvty(), unit.getGvtz(),
                                            unit.getGyrx(), unit.getGyry(), unit.getGyrz()));
                }

                // 对于此key切片中不同key，会有不同的accFtrArr/gvtFtrArr/gyrFtrArr
                // 得到accxyz列表的列表, gvtxyz列表的列表, gyrxyz列表的列表，9元素列表的列表
                // accFtrArrList/gvtFtrArrList/gyrFtrArrList的长度与切片长度有关
                accFtrArrList.add(accFtrArr);
                gvtFtrArrList.add(gvtFtrArr);
                gyrFtrArrList.add(gyrFtrArr);
                secRawList.add(secRaw);
            }
            if (secRawList.size() == 0)
                continue;

            // SecRaw 是一个AccGvtGyr的数组， 属于二维范畴
            // SecRawList 是一个 SecRaw的数组 ， 属于三维范畴
            // accFtrArrList 是一个三维数组， DP.featureGenerator 需要传入二维数组
            // 也就是将XYZParent作为行铺开
            List<XYZParent> accFtrArrList2D = new ArrayList<>();
            for (List<XYZParent> accFtrList : accFtrArrList) {
                accFtrArrList2D.addAll(accFtrList);
            }
            List<List<Double>> accFtr = featureGenerator(accFtrArrList2D);  // 5 * 3

            List<XYZParent> gvtFtrArrList2D = new ArrayList<>();
            for (List<XYZParent> gvtFtrList : gvtFtrArrList) {
                gvtFtrArrList2D.addAll(gvtFtrList);
            }
            List<List<Double>> gvtFtr = featureGenerator(gvtFtrArrList2D);  //

            List<XYZParent> gyrFtrArrList2D = new ArrayList<>();
            for (List<XYZParent> gyrFtrList : gyrFtrArrList) {
                gyrFtrArrList2D.addAll(gyrFtrList);
            }
            List<List<Double>> gyrFtr = featureGenerator(gyrFtrArrList2D);  //

            // 新建两个新的数据结构FtrElem 和 RawElem 添加到sample_ftr_list 中    FtrElem.getContent() : 5 * 3 * 3
            // sampleFtrList: n * (5 * 3 * 3)
            if (poseIdxMap.get(STATE) != null) {
                sampleFtrList.add(new FtrElem(accFtr, gvtFtr, gyrFtr, poseIdxMap.get(STATE))); // POSE Must Be Right and this must not be null
                sampleRawList.add(new RawElem(secRawList, poseIdxMap.get(STATE))); // POSE Must Be Right and this must not be null
            }

        }
        System.out.println("sample_str_cnt="+sampleFtrList.size());
        return new RawFtrLists(sampleRawList, sampleFtrList);
    }


    /**
     * @param tsSenMap 传入一个Map，key为ts值，value为(SenUnitV2列表)，完整表示一个csv文件的全部数据
     * @param SEQ_LEN 切片长度
     * @param STRIDE 步长
     * @param REJECT_SEC 去掉的头尾长度
     * @param POSE 姿态 注意姿态不一定传入有意义的数值
     * @return RawFtrLists
     * @see SenUnitV2
     * @see RawFtrLists
     * Description: 传入一个tsSenMap的Map，通过对key进行切片获取特征值，返回两个List，两个List的长度均为切片个数
     *              用于姿态检测
     */
    public static RawFtrLists jointSenUnit2FtrStr(Map<Long, List<SenUnitV2>> tsSenMap, int SEQ_LEN, int STRIDE, int REJECT_SEC, String POSE) {
        if ("default".equals(POSE)){
            POSE = Constants.FLAT;
        }

        // 姿态分类标签
        Map<String, Integer> poseIdxMap = new HashMap<>();
        poseIdxMap.put(Constants.FLAT, 0);
        poseIdxMap.put(Constants.CALLING, 1);
        poseIdxMap.put(Constants.POCKET, 2);
        poseIdxMap.put(Constants.BAG, 3);

        // 获取tsSenMap的key列表，也就是ts值的列表
        List<Long> tsList = tsSenMap.keySet().stream().collect(Collectors.toList());
        Collections.sort(tsList);   // 1651567804 - 1651568019   216

        // Remove first and end some units
        for (int i=0; i<REJECT_SEC; i++) {
            tsList.remove(0);
            tsList.remove(tsList.size()-1);
        }

        List<FtrElem> sampleFtrList = new ArrayList<>();
        List<RawElem> sampleRawList = new ArrayList<>();

        // 指定步长和切片间隔，循环切片（对key列表切片） 循环次数为切片数量
        for (int i=0; i<(tsList.size() - SEQ_LEN); i+=STRIDE) {
            //获取当前key列表切片，长度为(SEQ_LEN + 1)
            List<Long> sampleTSList = tsList.subList(i, i + SEQ_LEN + 1);

            // sec_ftr_list
            // sec_raw_list
            List<List<XYZParent>> accFtrArrList = new ArrayList<>();    // 存储 切片长度Len * 50 个xyz
            List<List<XYZParent>> gvtFtrArrList = new ArrayList<>();
            List<List<XYZParent>> gyrFtrArrList = new ArrayList<>();
            List<List<AccGvtGyr>> secRawList = new ArrayList<>();

            // 遍历key切片，分别得到当前key值和下一key值对应的 SenUnitV2列表
            for (int j=0; j<(sampleTSList.size() - 1); j++) {
                List<SenUnitV2> crntSenUnitList = tsSenMap.get(sampleTSList.get(j));// 当前
                List<SenUnitV2> nextSenUnitList = tsSenMap.get(sampleTSList.get(j + 1));// 下一个

                SenUnitV2 crntHeadSenUnit = null;
                SenUnitV2 nextHeadSenUnit = null;
                if (crntSenUnitList != null) {      // Must Not Be Null
                    crntHeadSenUnit = crntSenUnitList.get(0);
                }

                float crntLon = crntHeadSenUnit.getLon();
                float crntLat = crntHeadSenUnit.getLat();
                float crntSpd = crntHeadSenUnit.getSpeed();
                float crntBrng = crntHeadSenUnit.getBearing();

                if (nextSenUnitList != null) {      // Must Not Be Null
                    nextHeadSenUnit = nextSenUnitList.get(0);
                }

                float nextLon = nextHeadSenUnit.getLon();
                float nextLat = nextHeadSenUnit.getLat();
                float nextSpd = nextHeadSenUnit.getSpeed();
                float nextBrng = nextHeadSenUnit.getBearing();

                // 如果两点之间距离太远
                if (GeoUtils.haversine_formula(crntLat, crntLon, nextLat, nextLon) > 3.0)
                    continue;

                // 重构到长度为 50
                if (crntSenUnitList.size() >= 40 && crntSenUnitList.size() <= 50) {
                    for (int k=0; k<(50-crntSenUnitList.size()); k++) {
                        crntSenUnitList.add(crntSenUnitList.get(crntSenUnitList.size()-1));
                    }
                } else if (crntSenUnitList.size() >= 51 && crntSenUnitList.size() <= 60) {
                    for (int k=0; k<(crntSenUnitList.size() - 50); k++) {
                        crntSenUnitList.remove(crntSenUnitList.size()-1);
                    }
                } else {
                    continue;
                }


                List<XYZParent> accFtrArr = new ArrayList<>(); // 存储50个xyz
                List<XYZParent> gvtFtrArr = new ArrayList<>(); // 存储50个xyz
                List<XYZParent> gyrFtrArr = new ArrayList<>(); // 存储50个xyz
                List<AccGvtGyr> secRaw = new ArrayList<>();

                // 遍历当前 SenUnitV2列表，分别构建 accxyz列表，gvtxyz列表，gyrxyz列表，9元素列表
                for (SenUnitV2 unit : crntSenUnitList) {
                    accFtrArr.add(new XYZParent(unit.getAccx(), unit.getAccy(), unit.getAccz()));
                    gvtFtrArr.add(new XYZParent(unit.getGvtx(), unit.getGvty(), unit.getGvtz()));
                    gyrFtrArr.add(new XYZParent(unit.getGyrx(), unit.getGyry(), unit.getGyrz()));
                    secRaw.add(new AccGvtGyr(unit.getAccx(), unit.getAccy(), unit.getAccz(),
                                            unit.getGvtx(), unit.getGvty(), unit.getGvtz(),
                                            unit.getGyrx(), unit.getGyry(), unit.getGyrz()));
                }

                // 对于此key切片中不同key，会有不同的accFtrArr/gvtFtrArr/gyrFtrArr
                // 得到accxyz列表的列表, gvtxyz列表的列表, gyrxyz列表的列表，9元素列表的列表
                // accFtrArrList/gvtFtrArrList/gyrFtrArrList的长度与切片长度有关
                accFtrArrList.add(accFtrArr);
                gvtFtrArrList.add(gvtFtrArr);
                gyrFtrArrList.add(gyrFtrArr);
                secRawList.add(secRaw);
            }
            if (secRawList.size() == 0)
                continue;

            // SecRaw 是一个AccGvtGyr的数组， 属于二维范畴
            // SecRawList 是一个 SecRaw的数组 ， 属于三维范畴
            // accFtrArrList 是一个三维数组， DP.featureGenerator 需要传入二维数组
            // 也就是将XYZParent作为行铺开

            List<XYZParent> accFtrArrList2D = new ArrayList<>(); // 存储 (Len * 50) 个xyz
            for (List<XYZParent> accFtrList : accFtrArrList) {
                accFtrArrList2D.addAll(accFtrList);
            }
            List<List<Double>> accFtr = featureGenerator(accFtrArrList2D);// 5 * 3

            List<XYZParent> gvtFtrArrList2D = new ArrayList<>();
            for (List<XYZParent> gvtFtrList : gvtFtrArrList) {
                gvtFtrArrList2D.addAll(gvtFtrList);
            }
            List<List<Double>> gvtFtr = featureGenerator(gvtFtrArrList2D);//

            List<XYZParent> gyrFtrArrList2D = new ArrayList<>();
            for (List<XYZParent> gyrFtrList : gyrFtrArrList) {
                gyrFtrArrList2D.addAll(gyrFtrList);
            }
            List<List<Double>> gyrFtr = featureGenerator(gyrFtrArrList2D);//

            // 新建两个新的数据结构FtrElem 和 RawElem 添加到sample_ftr_list 中    FtrElem.getContent() : 5 * 3 * 3
            // sampleFtrList: n * (5 * 3 * 3)
            if (poseIdxMap.get(POSE) != null) {
                sampleFtrList.add(new FtrElem(accFtr,gvtFtr,gyrFtr,poseIdxMap.get(POSE))); // POSE Must Be Right and this must not be null
                sampleRawList.add(new RawElem(secRawList, poseIdxMap.get(POSE))); // POSE Must Be Right and this must not be null
            }
        }
        System.out.println("sample_str_cnt="+sampleFtrList.size());
        return new RawFtrLists(sampleRawList, sampleFtrList);
    }


}
