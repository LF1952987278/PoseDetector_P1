package com.bjtu.pose.utils;

import java.util.ArrayList;
import java.util.List;

public class Step6 {
    final int ValueNum = 4;
    //用于存放计算阈值的波峰波谷差值
    float[] tempValue = new float[ValueNum];

    int tempCount = 0;

    //是否上升的标志位
    boolean isDirectionUp = false;

    //持续上升次数
    int continueUpCount = 0;

    //上一点的持续上升的次数，为了记录波峰的上升次数
    int continueUpFormerCount = 0;

    //上一点的状态，上升还是下降
    boolean lastStatus = false;

    //波峰值
    float peakOfWave = 0;

    //波谷值
    float valleyOfWave = 0;

    //此次波峰的时间
    long timeOfThisPeak = 0;
    //上次波峰的时间
    long timeOfLastPeak = 0;
    //当前的时间
    long timeOfNow = 0;

    //当前传感器的值
    float gravityNew = 0;
    //上次传感器的值
    float gravityOld = 0;

    //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    final float InitialValue = (float) 4.5;

    //初始阈值
    float ThreadValue = (float) 3.0;

    //波峰波谷时间差
    int TimeInterval = 10;

    // 步数
    int count = 0;

    // 是否行车 初始认为未在车上 即步行
    // 0 代表unknown
    // 1 代表静止
    // 2 代表车上
    // 3 代表行走
    // 制定规则：静止态：严格静止，判定值应趋近于0
    //         车上态：在车上且手机应相对车静止且在车平稳运行时： 判定值应为 < 30?
    //         行走态：人正常行走中，且应考虑到行走快慢，且在四种姿态内： 判定值应为 > 150?
    //         unknown: 其它情况均可视作unknown
    int status = 0;

    int last_status = 0;

    // 当前数据计数
    int allCount = 0;

    // 判别值
    float num_judge = 0.0F;
    float gps_judge = 0.0F;
    float gps_speed_judge = 0.0F;

    // 判别值记录
    ArrayList<Float> sites = new ArrayList<>();
    ArrayList<Float> gps_sites = new ArrayList<>();
    ArrayList<Float> gps_speed_sites = new ArrayList<>();

    // 历史status
    ArrayList<Integer> status_list = new ArrayList<>();

    // 每一步的时间
    ArrayList<Long> time = new ArrayList<>();

    public int stepCounter(List<String> content) {
        // 读取陀螺仪数据后调用记步
        List<SenUnitV2> ts_data = new ArrayList<>();
        for(String line:content){
            IndoorSenUnitV2 sen_unit = new IndoorSenUnitV2(line);
            ts_data.add(sen_unit);
        }
        for (SenUnitV2 unit:ts_data) {
            // 读取陀螺仪数据后调用记步
            float gravityNew = (float) Math.sqrt(unit.accx * unit.accx +
                    unit.accy * unit.accy +
                    unit.accz * unit.accz);
            allCount = allCount + 1;
            // 每100Hz记录判别值并使判别值归零
            if (allCount % 100 == 0 && allCount != 0) {
                sites.add(num_judge); num_judge = 0.0F;
                gps_sites.add(gps_judge); gps_judge = 0.0F;
                gps_speed_sites.add(gps_speed_judge); gps_speed_judge = 0.0F;
                status = re_qualification2();
                if (status == 0) status = qualification2();
                if (status != 0) last_status = status;

                if (status_list.size() >= 30 * 10) status_list.remove(0);  // 2s一个状态, 1min共30个状态, 最长10min
                status_list.add(status);
            }
            float judge_value = (unit.accx - unit.gvtx) * (unit.accx - unit.gvtx) +
                    (unit.accy - unit.gvty) * (unit.accy - unit.gvty) +
                    (unit.accz - unit.gvtz) * (unit.accz - unit.gvtz);

            if (judge_value > 20) {
                num_judge = num_judge + 20;
            } else {
                num_judge = num_judge + judge_value;
            }
            gps_judge = gps_judge + unit.gps_accu;
            gps_speed_judge = gps_speed_judge + unit.speed;
            detectorNewStep(gravityNew, unit.ts_ms);
        }

        return count;
    }

    public void stepCounter(float[] accValues, float[] graValues, long ts, float gps_speed, float gps_accu) {
        // 读取陀螺仪数据后调用记步
        float gravityNew = (float) Math.sqrt(accValues[0] * accValues[0] +
                                             accValues[1] * accValues[1] +
                                             accValues[2] * accValues[2]);

        allCount = allCount + 1;
        // 每100Hz记录判别值并使判别值归零
        if (allCount % 100 == 0 && allCount != 0) {
            sites.add(num_judge); num_judge = 0.0F;
            gps_sites.add(gps_judge); gps_judge = 0.0F;
            gps_speed_sites.add(gps_speed_judge); gps_speed_judge = 0.0F;
            status = re_qualification2();
            if (status == 0) status = qualification2();
            if (status != 0) last_status = status;

            if (status_list.size() >= 30 * 10) status_list.remove(0);  // 2s一个状态, 1min共30个状态, 最长10min
            status_list.add(status);
        }
        float judge_value = (accValues[0] - graValues[0]) * (accValues[0] - graValues[0]) +
                            (accValues[1] - graValues[1]) * (accValues[1] - graValues[1]) +
                            (accValues[2] - graValues[2]) * (accValues[2] - graValues[2]);

        if (judge_value > 20) {
            num_judge = num_judge + 20;
        } else {
            num_judge = num_judge + judge_value;
        }
        gps_judge = gps_judge + gps_accu;
        gps_speed_judge = gps_speed_judge + gps_speed;

        detectorNewStep(gravityNew, ts);
    }

    // 是否行车 初始认为未在车上 即步行
    // 0 代表unknown
    // 1 代表静止
    // 2 代表车上
    // 3 代表行走
    // 制定规则：静止态：严格静止，判定值应趋近于0
    //         车上态：在车上且手机应相对车静止且在车平稳运行时： 判定值应为 < 30?
    //         行走态：人正常行走中，且应考虑到行走快慢，且在四种姿态内： 判定值应为 > 150?
//    //         unknown: 其它情况均可视作unknown
//    private int qualification2() {
//        // 如果目前判定值不足
//        if (sites.size() < 3) return 0;
//
//        // 记录最后6s的判定值
//        float value1 = sites.get(sites.size() - 1);
//        float value2 = sites.get(sites.size() - 2);
//        float value3 = sites.get(sites.size() - 3);
//
//        int eValue1 = evaluation2(value1);
//        int eValue2 = evaluation2(value2);
//        int eValue3 = evaluation2(value3);
//        // 如果三个区间都是unknown
//        if (eValue1 == 0 && eValue2 == 0 && eValue3 == 0) return 0;
//
//        // 如果有两个区间都是unknown
//        if (
//                        (eValue1 == 0 && eValue2 == 0) ||
//                        (eValue1 == 0 && eValue3 == 0) ||
//                        (eValue2 == 0 && eValue3 == 0)
//        ) return 0;
//
//        // 如果有一个区间是unknown
//        if (eValue1 == 0) {
//            if (eValue2 == eValue3) return eValue2;
//            else return 0;
//        }
//
//        if (eValue2 == 0) {
//            if (eValue1 == eValue3) return eValue1;
//            else return 0;
//        }
//
//        if (eValue3 == 0) {
//            if (eValue1 == eValue2) return eValue1;
//            else return 0;
//        }
//
//        // 如果没有unknown
//        // 判断三个区间是不是相等  判断是不是有两个静止态
//        if (eValue1 == eValue2 && eValue2 == eValue3) return eValue1;
//        else if (
//                        (eValue1 == 1 && eValue2 == 1) ||
//                        (eValue1 == 1 && eValue3 == 1) ||
//                        (eValue2 == 1 && eValue3 == 1)
//        ) return 1;
//        else return 0;
//    }
//
//    // 0 代表unknown
//    // 1 代表静止
//    // 2 代表车上
//    // 3 代表行走
//    private int evaluation2(float value) {
//        if (value < 10) return 1;
//        else if (value >= 10 && value < 100) return 2;
//        else if (value > 180) return 3;
//        else return 0;
//    }
    private int re_qualification2() {
        if (gps_sites.size() > 30) { // 至少已经开始1分钟
            int tempCount = 0;
            float tempValue = gps_sites.get(gps_sites.size() - 1); // 最后一项
            float tempValue2 = gps_speed_sites.get(gps_speed_sites.size() - 1); // 最后一项
            for (int i = 1; i <= 10; i++) { // 判断最后10个2s(100Hz)
                if (gps_sites.get(gps_sites.size() - i) == tempValue &&
                        gps_speed_sites.get(gps_speed_sites.size() - i) == tempValue2) tempCount++;
            }
            if (tempCount == 10) return 0; // unknown
        }
        // 如果目前判定值不足
        if (sites.size() < 1) return 0; // unknown
        // 记录最后2s:100Hz的gps判定值
        float gps_accu_values = gps_sites.get(gps_sites.size() - 1);
        float gps_speed_values = gps_speed_sites.get(gps_speed_sites.size() - 1);
        if (status == 2) {
            if (gps_accu_values < 1300 && gps_accu_values > 100 && gps_speed_values > 50) return 2;
            else return 0;
        } else {
            if (gps_accu_values < 1100 && gps_accu_values > 100 && gps_speed_values > 250) return 2;
            else return 0;
        }
    }

    private int qualification2() {
        // 如果目前判定值不足
        if (sites.size() < 3) return 0;

        // 记录最后6s的判定值
        float value1 = sites.get(sites.size() - 1);
        float value2 = sites.get(sites.size() - 2);
        float value3 = sites.get(sites.size() - 3);

        int eValue1 = evaluation2(value1);
        int eValue2 = evaluation2(value2);
        int eValue3 = evaluation2(value3);

        if (eValue1 == eValue2 && eValue2 == eValue3) return eValue1;
        else if (eValue1 == eValue2 || eValue1 == eValue3) return eValue1;
        else if (eValue2 == eValue3) return eValue2;
        else return 0;
    }
    // 0 代表unknown
    // 1 代表静止
    // 2 代表车上
    // 3 代表行走
    private int evaluation3(float value) {
        if (value < 15) return 1;
        else if (value >= 15 && value < 85) return 2;
        else if (value > 170) return 3;
        else return 0;
    }

    private int evaluation2(float value) {
        if (last_status == 2) { // after driving status
            if (value < 15) return 1; // static
//            else if (value >= 15 && value < 150) return 2; // driving
            else if (value > 250) return 3; // walking
            else return 0;
        } else if (last_status == 3) { // after walking status
            if (value < 15) return 1; // static
//            else if (value >= 15 && value < 90) return 2; // driving
            else if (value > 160) return 3; // walking
            else return 0;
        } else if (last_status == 1) { // after static status
            if (value < 20) return 1; // static
//            else if (value >= 20 && value < 100) return 2; // driving
            else if (value > 180) return 3; // walking
            else return 0;
        } else { // after unknown status
            if (value < 15) return 1; // static
//            else if (value >= 15 && value < 95) return 2; // driving
            else if (value > 170) return 3; // walking
            else return 0;
        }
    }


//    private int evaluation(float value) {
//        if (value > 0.1 && value <= 30) return 3; // 非静止状态且小速度变化
//        else if (value > 30 && value <= 80) return 2; // 中速度变化
//        else if (value > 80 && value <= 130) return 1; // 高速度变化
//        else return 0; // 超高速度变化
//    }

//    // 车上/静止 判定
//    private boolean qualification() {
//        int[] judgeLabels = {0, 0, 0};
//        if (sites.size() < 3) return false;
//        judgeLabels[0] = evaluation(sites.get(sites.size() - 1));
//        judgeLabels[1] = evaluation(sites.get(sites.size() - 2));
//        judgeLabels[2] = evaluation(sites.get(sites.size() - 3));
//        int judge = judgeLabels[0] + judgeLabels[1] + judgeLabels[2];
//        if(judge >= 5 && (judgeLabels[0] == 3 || judgeLabels[1] == 3 || judgeLabels[2] == 3)) return true;
//        else return false;
//    }
//
//    // 下车/静止转动 判定
//    private boolean loseQualification() {
//        int[] judgeLabels = {0, 0, 0};
//        if (sites.size() < 3) return false;
//        if (sites.get(sites.size() - 1) > 250) judgeLabels[0] = 1;
//        if (sites.get(sites.size() - 2) > 250) judgeLabels[1] = 1;
//        if (sites.get(sites.size() - 3) > 250) judgeLabels[2] = 1;
//        int judge = judgeLabels[0] + judgeLabels[1] + judgeLabels[2];
//        if (judge == 3) return true;
//        else return false;
//    }
//
    // 前3个区间
    private boolean testQualification() {
        if (sites.size() == 0) {
            return true;
        } else if (sites.size() == 1) {
            return sites.get(0) > 100;
        } else if (sites.size() == 2) {
            return sites.get(1) > 120;
        }
        else { // 只检查最后一项
            throw new Error("Haitao: Size is 3 or out of 3");
        }
    }


    /*
     * 检测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     * */
    private void detectorNewStep(float values, long ts) {
        if (gravityOld == 0) { // 起始
            gravityOld = values;
        } else {
            if (detectorPeak(values, gravityOld)) {
//                System.out.print("Detect!  ");
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = ts;
                if (timeOfNow - timeOfLastPeak >= TimeInterval) {
                    System.out.print("Peak: " + peakOfWave + " Valley: " + valleyOfWave + "Thread: " + ThreadValue + "  ");
                    if ((peakOfWave - valleyOfWave >= ThreadValue)) {
//                        System.out.print("Wave!" + "  ");
                        timeOfThisPeak = timeOfNow;
                        if (sites.size() < 3) { // 前3个时间区间要通过这种方法判别是否记步
                            if (testQualification()) {
                                count++;
                                addTime(timeOfNow);
                            }
                        } else { // 检测到步伐波动且不是driving 必然是行走中
                            if (status == 3) {
                                count++;
                                addTime(timeOfNow);
                            }
                        }
                    }
                }
                if (timeOfNow - timeOfLastPeak >= TimeInterval && (peakOfWave - valleyOfWave >= InitialValue)) {
//                    System.out.print("Thread!  ");
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = peakValleyThread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }

    /*
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于20
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * */
    private boolean detectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

        if (!isDirectionUp && lastStatus
                && (continueUpFormerCount >= 2 || oldValue >= 20)) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /*
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     * */
    private float peakValleyThread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < ValueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, ValueNum);
            for (int i = 1; i < ValueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[ValueNum - 1] = value;
        }
        return tempThread;
    }

    /*
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     * */
    private float averageValue(float[] value, int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / ValueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        return ave;
    }

    private void addTime(Long ts){
        if(time.size()<5) time.add(ts);
        else {
            time.remove(0);
            time.add(ts);
        }
    }

    public long getTimeInterval(){
        if(time.size()==5) return time.get(time.size()-1) - time.get(0);
        else return Long.MAX_VALUE;
    }

    public ArrayList<Integer> getStatusList(){
        return status_list;
    }

    public String getStatus(){
        if(status_list.size()==0) return "unknown";
        if(status_list.get(status_list.size()-1) == 2){
            return "driving";
        }else if(status_list.get(status_list.size()-1) == 1){
            return "static";
        }else if(status_list.get(status_list.size()-1) == 3){
            return "walking";
        }else return "unknown";
    }
}
