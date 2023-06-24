package com.bjtu.pose.pose.svm;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;

/**
 * @author LiuFeng
 * @version 1.0
 * Description: SVM定义、训练、输出训练效果
 *              用于姿态检测
 */
public class SvmProcess {
    private final SVM<double[]> svm = new SVM<double[]>(new LinearKernel(), 0.4, 4, SVM.Multiclass.ONE_VS_ALL);

    /**
     * @param data 数据
     * @param label 标签/分类
     */
    public SvmProcess(double[][] data, int[] label) {
        System.out.println("Then will learn");
        svm.learn(data, label);
        svm.trainPlattScaling(data, label);
        svm.finish();

        double right = 0.0;
        for (int i = 0; i < data.length; i++) {
            int tag = svm.predict(data[i]);
            if (tag == label[i]) {
                right += 1;
            }
        }
        double accr = right / data.length;
        System.out.println("Accurate: " + accr * 100 + "%");
    }

    /**
     * @return svm object
     */
    public SVM<double[]> getSVM() {
        return this.svm;
    }
}
