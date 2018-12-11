package org.haohhxx.util.matrics.feature;

/**
 * @author zhenyuan_hao@163.com
 */
public interface FeatureLine {
    double getTarget();
    void setTarget(double target);

    enum LineDataType{
        /**
         * 特征文件格式
         */
        svm(), csv()
    }

}
