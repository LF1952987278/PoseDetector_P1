package com.bjtu.pose.pose.predict;

import static com.bjtu.pose.pose.utils.Functions.featureGenerator;
import static com.bjtu.pose.pose.utils.Functions.jointSenUnit2FtrStr;

import android.util.Pair;

import com.bjtu.pose.pose.entity.Constants;
import com.bjtu.pose.pose.entity.FtrElem;
import com.bjtu.pose.pose.entity.RawFtrLists;
import com.bjtu.pose.pose.entity.SenUnitV2;
import com.bjtu.pose.pose.entity.XYZParent;
import com.bjtu.pose.pose.utils.Object2File;


import smile.classification.SVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * * Author: Liu Feng
 * * Date:2023/4/21 14:56
 * * Description Some methods for predict
 *  *            用于姿态检测
 */
public class Predict {

    private SVM<double[]> svm = (SVM<double[]>) Object2File.readObjectFromFile(Constants.svmReadPath);

    /**
     * @param unitsList
     * @return 预测POSE
     * @see SenUnitV2
     * Description: 传入UnitsList， 返回预测POSE
     *              预测1s内的姿态，返回预测POSE
     *              实时预测不需要切片，切片的目的是分时间片
     */
    public Pair<String, Double> predictMoment(List<SenUnitV2> unitsList) {
        List<SenUnitV2> crntSenUnitList = unitsList;
        if (crntSenUnitList.size() >= 40 && crntSenUnitList.size() <= 50) {
            for (int i=0; i<(50-crntSenUnitList.size()); i++) {
                crntSenUnitList.add(crntSenUnitList.get(crntSenUnitList.size()-1));
            }
        } else if (crntSenUnitList.size() >= 51 && crntSenUnitList.size() <= 60) {
            for (int i=1; i<(crntSenUnitList.size() - 50); i++) {
                crntSenUnitList.remove(crntSenUnitList.size()-1);
            }
        } else {
            return new Pair("Error", -0.2);
        }

        List<XYZParent> accFtrArr = new ArrayList<>();
        List<XYZParent> gvtFtrArr = new ArrayList<>();
        List<XYZParent> gyrFtrArr = new ArrayList<>();

        // 遍历当前 SenUnitV2列表，分别构建 accxyz列表，gvtxyz列表，gyrxyz列表，9元素列表
        for (SenUnitV2 unit : crntSenUnitList) {
            accFtrArr.add(new XYZParent(unit.getAccx(), unit.getAccy(), unit.getAccz()));
            gvtFtrArr.add(new XYZParent(unit.getGvtx(), unit.getGvty(), unit.getGvtz()));
            gyrFtrArr.add(new XYZParent(unit.getGyrx(), unit.getGyry(), unit.getGyrz()));
        }

        List<List<List<Double>>> list = new ArrayList<>();
        list.add(featureGenerator(accFtrArr));
        list.add(featureGenerator(gvtFtrArr));
        list.add(featureGenerator(gyrFtrArr));


        List<Double> dlc = new ArrayList<>();
        // 转为 DoubleArray
        for (List<List<Double>> tmp3 : list) {
            for (List<Double> tmp2 : tmp3) {
                for (Double tmp1 : tmp2) {
                    dlc.add(tmp1);
                }
            }
        }

        double[] pArray = new double[4];
        int predict = svm.predict(dlc.stream().mapToDouble(Double::valueOf).toArray(), pArray);

        Pair<String, Double> result = null;
        switch (predict) {
            case 0:
                if (pArray[0] >= Constants.bound) {
                    result = new Pair<String, Double>(Constants.FLAT, pArray[0]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[0]);
                }
                break;
            case 1:
                if (pArray[1] >= Constants.bound) {
                    result = new Pair<String, Double>(Constants.CALLING, pArray[1]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[1]);
                }
                break;
            case 2:
                if (pArray[2] >= Constants.bound) {
                    result = new Pair<String, Double>(Constants.POCKET, pArray[2]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[2]);
                }
                break;
            case 3:
                if (pArray[3] >= Constants.bound) {
                    result = new Pair<String, Double>(Constants.BAG, pArray[3]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[3]);
                }
                break;
            default:
                try {
                    throw new Exception("LiuFeng: Predict value is not 0/1/2/3");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        return result;

    }

    /**
     * @param tsSenMap
     * @return 预测列表
     * @see SenUnitV2
     * Description: 传入Map， 返回预测列表，预测列表的长度和Map的key值长度有关，也就是和时间长度有关
     *              预测一个时间区间内的姿态，返回预测列表
     *              由于属于历史数据的长时间预测，使用到了 时间切片 和 时间窗口（svm预测输入数据为切片长度Len * 50）
     */
    public List<Pair<String, Double>> predict(Map<Long, List<SenUnitV2>> tsSenMap) {

        // 1 -- 1649316923
        // 2 -- "1651567820"
        // 3 -- "1660217258"
        // 4 -- "1660217583"
        // 5 -- "1660218652"
        // 6 -- "1651568948"
        // 7 --  "1667440313" "1667440792"

        RawFtrLists rawFtrItem = jointSenUnit2FtrStr(tsSenMap, Constants.seqLen, Constants.stride, Constants.rejectSec, "default");

        // ftr长度即切片（切片是对key的切片）个数
        List<FtrElem> ftr = rawFtrItem.getSfl();  //   n * (5*3*3)

        List<List<List<List<Double>>>> testDataList = new ArrayList<>();

        for (FtrElem elem : ftr) {
            testDataList.add(elem.getContent());
        }

        List<List<Double>> dlc = new ArrayList<>();
        for (List<List<List<Double>>> tmp3 : testDataList) {
            List<Double> dlcItem = new ArrayList<>();
            for (List<List<Double>> tmp2 : tmp3) {
                for (List<Double> tmp1 : tmp2) {
                    for (Double tmp0 : tmp1) {
                        dlcItem.add(tmp0);
                    }
                }
            }
            dlc.add(dlcItem);
        }

        List<Pair<String, Double>> testLabelPairList = new ArrayList<>();


        for (List<Double> item : dlc) { // item: DoubleArray
            double[] pArray = new double[4];

            double[] d = new double[item.size()];
            for (int i=0; i<item.size(); i++) {
                d[i] = item.get(i);
            }
            int predict = svm.predict(d, pArray);

            Pair<String, Double> result = null;
            switch (predict) {
                case 0:
                    if (pArray[0] >= Constants.bound) {
                        result = new Pair<String, Double>(Constants.FLAT, pArray[0]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, pArray[0]);
                        testLabelPairList.add(result);
                    }
                    break;
                case 1:
                    if (pArray[1] >= Constants.bound) {
                        result = new Pair<String, Double>(Constants.CALLING, pArray[1]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, pArray[1]);
                        testLabelPairList.add(result);
                    }
                    break;
                case 2:
                    if (pArray[2] >= Constants.bound) {
                        result = new Pair<String, Double>(Constants.POCKET, pArray[2]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, pArray[2]);
                        testLabelPairList.add(result);
                    }
                    break;
                case 3:
                    if (pArray[3] >= Constants.bound) {
                        result = new Pair<String, Double>(Constants.BAG, pArray[3]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, pArray[3]);
                        testLabelPairList.add(result);
                    }
                    break;
                default:
                    try {
                        throw new Exception("Haitao: Predict value is not 0/1/2/3");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }

        }
        return testLabelPairList;
    }


}
