package com.bjtu.pose.pose.svm;

import smile.classification.SVM;
import smile.math.kernel.GaussianKernel;
import smile.math.kernel.LinearKernel;

/**
 * @author LiuFeng
 * @version 1.0
 * Description: SVM定义、训练、输出训练效果
 *              用于步行检测
 */
public class SvmProcessV0 {
    private final double gamma = 1.0;
    private final double C = 1.0;
    private final SVM<double[]> svm = new SVM<double[]>(new GaussianKernel(gamma), C);

    /**
     * @param data 数据
     * @param label 标签/分类
     */
    public SvmProcessV0(double[][] data, int[] label) {
        System.out.println("Then will learn V2");
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
