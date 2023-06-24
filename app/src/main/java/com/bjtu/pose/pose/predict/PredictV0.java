package com.bjtu.pose.pose.predict;

import static com.bjtu.pose.pose.utils.Functions.featureGenerator;
import static com.bjtu.pose.pose.utils.Functions.jointSenUnit2FtrStrV0;

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
 * * Date:2023/4/21 16:00
 * * Description Some methods for predict
 *  *            用于步行检测
 */
public class PredictV0 {

    private SVM<double[]> svm = (SVM<double[]>) Object2File.readObjectFromFile(Constants.svmReadPathV0);

    /**
     * @param unitsListList
     * @return 预测POSE
     * @see SenUnitV2
     * Description: 传入UnitsList， 返回预测POSE
     *              指定时间窗口的实时预测，svm的传入数据为 时间长度Len * 50
     */
    public Pair<String, Double> predictFiveSeconds(List<List<SenUnitV2>> unitsListList) {
        List<SenUnitV2> predictList = new ArrayList<>();
        for (List<SenUnitV2> unitsList : unitsListList) {
            List<SenUnitV2> crntSenUnitList = unitsList;
            if (crntSenUnitList.size() >= 40 && crntSenUnitList.size() <= 50) {
                for (int i=0; i<(50-crntSenUnitList.size()); i++) {
                    crntSenUnitList.add(crntSenUnitList.get(crntSenUnitList.size()-1));
                }
            } else if (crntSenUnitList.size() >= 51 && crntSenUnitList.size() <= 60) {
                for (int i=0; i<(crntSenUnitList.size() - 50); i++) {
                    crntSenUnitList.remove(crntSenUnitList.size()-1);
                }
            } else {        // 必须要传入40-60的数据
                try {
                    throw new Exception("LiuFeng: Then length of unitsList of each second is not in 40-60");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            predictList.addAll(crntSenUnitList);
        }


        List<XYZParent> accFtrArr = new ArrayList<>();
        List<XYZParent> gvtFtrArr = new ArrayList<>();
        List<XYZParent> gyrFtrArr = new ArrayList<>();

        // 遍历当前 SenUnitV2列表，分别构建 accxyz列表，gvtxyz列表，gyrxyz列表，9元素列表
        for (SenUnitV2 unit : predictList) {
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

        double[] pArray = new double[2];
        int predict = svm.predict(dlc.stream().mapToDouble(Double::valueOf).toArray(), pArray);

        Pair<String, Double> result = null;
        switch (predict) {
            case 0:
                if (pArray[0] >= Constants.boundV0) {
                    result = new Pair<String, Double>(Constants.DRIVING, pArray[0]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[0]);
                }
                break;
            case 1:
                if (pArray[1] >= Constants.boundV0) {
                    result = new Pair<String, Double>(Constants.WALKING, pArray[1]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[1]);
                }
                break;
            default:
                try {
                    throw new Exception("LiuFeng: predict value is not 0 or 1");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        return result;
    }

    /**
     * @param unitsList
     * @return 预测POSE
     * @see SenUnitV2
     * Description: 传入UnitsList， 返回预测POSE
     *              1s时间窗口的实时预测，svm的传入数据为 时间长度1 * 50
     */
    public Pair<String, Double> predictMoment(List<SenUnitV2> unitsList) {
        List<SenUnitV2> crntSenUnitList = unitsList;
        if (crntSenUnitList.size() >= 40 && crntSenUnitList.size() <= 50) {
            for (int i=0; i<(50-crntSenUnitList.size()); i++) {
                crntSenUnitList.add(crntSenUnitList.get(crntSenUnitList.size()-1));
            }
        } else if (crntSenUnitList.size() >= 51 && crntSenUnitList.size() <= 60) {
            for (int i=0; i<(crntSenUnitList.size() - 50); i++) {
                crntSenUnitList.remove(crntSenUnitList.size()-1);
            }
        } else {
            return new Pair("Error", -0.1);
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

        double[] pArray = new double[2];
        int predict = svm.predict(dlc.stream().mapToDouble(Double::valueOf).toArray(), pArray);

        Pair<String, Double> result = null;
        switch (predict) {
            case 0:
                if (pArray[0] >= Constants.boundV0) {
                    result = new Pair<String, Double>(Constants.DRIVING, pArray[0]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[0]);
                }
                break;
            case 1:
                if (pArray[1] >= Constants.boundV0) {
                    result = new Pair<String, Double>(Constants.WALKING, pArray[1]);
                } else {
                    result = new Pair<String, Double>(Constants.UNKNOWN, pArray[1]);
                }
                break;
            default:
                try {
                    throw new Exception("LiuFeng: predict value is not 0 or 1");
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
     * Description: 传入Map， 返回预测列表
     */
    public List<Pair<String, Double>> predict(Map<Long, List<SenUnitV2>> tsSenMap) {

        RawFtrLists rawFtrItem = jointSenUnit2FtrStrV0(tsSenMap, Constants.seqLen, Constants.stride, Constants.rejectSec, "default");

        // ftr长度即切片（切片是对key的切片）个数
        List<FtrElem> ftr = rawFtrItem.getSfl();  //   n * (5*3*3)

        List<List<List<List<Double>>>> testDataList = new ArrayList<>();

        for (FtrElem item : ftr) {
            testDataList.add(item.getContent());
        }

        List<List<Double>> dlc = new ArrayList<>();
        for (List<List<List<Double>>> tmp3 : testDataList) {
            List<Double> dlcItem = new ArrayList<>();
            for (List<List<Double>> tmp2 : tmp3) {
                for (List<Double> tmp1 : tmp2) {
                    for (Double item0 : tmp1) {
                        dlcItem.add(item0);
                    }
                }
            }
            dlc.add(dlcItem);
        }

        List<Pair<String, Double>> testLabelPairList = new ArrayList<>();

        for (List<Double> item : dlc) { // item: DoubleArray
            double[] array = new double[2];
            int predict = svm.predict(item.stream().mapToDouble(Double::valueOf).toArray(), array);
            Pair<String, Double> result = null;
            switch (predict) {
                case 0:
                    if (array[0] >= Constants.boundV0) {
                        result = new Pair<String, Double>(Constants.DRIVING, array[0]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, array[0]);
                        testLabelPairList.add(result);
                    }
                    break;
                case 1:
                    if (array[1] >= Constants.boundV0) {
                        result = new Pair<String, Double>(Constants.WALKING, array[1]);
                        testLabelPairList.add(result);
                    } else {
                        result = new Pair<String, Double>(Constants.UNKNOWN, array[1]);
                        testLabelPairList.add(result);
                    }
                    break;
                default:
                    try {
                        throw new Exception("LiuFeng: predict value is not 0 or 1");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
        }

        return testLabelPairList;
    }

}
