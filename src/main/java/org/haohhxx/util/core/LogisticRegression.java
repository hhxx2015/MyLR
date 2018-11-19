package org.haohhxx.util.core;

import org.haohhxx.util.feature.AbstractFeatureLine;
import org.haohhxx.util.feature.FeatureMatrix;
import org.haohhxx.util.feature.SparseFeatureLine;
import org.haohhxx.util.feature.VectorLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author zhenyuan_hao@163.com
 */
public class LogisticRegression {

    public LogisticRegression(double alpha){
        this.alpha = alpha;
    }

    private final Random random = new Random();
    private double alpha;
    private SparseFeatureLine weightMap = new SparseFeatureLine();
    private Map<Integer,Double> maxValMap = new HashMap<>();
    private Map<Integer,Double> minValMap = new HashMap<>();

    /**
     * @param itea     迭代次数
     * @param features 实例数组
     */
    public void fit(FeatureMatrix features, int itea) {

        int m = features.size();
        this.maxValMap = features.getMax();
        this.minValMap = features.getMin();

        for (int it = 0; it < itea; it++) {

            HashMap<Integer, Double> predictCache = new HashMap<>(m);
            /*
              梯度下降  全部数据迭代一次后更新 weight
             */
            for (int i = 0; i < m ; i++){
                AbstractFeatureLine xi = features.get(i);
                double predict = predict(xi);
                predictCache.put(i, predict);
            }

            for (Integer j : weightMap.vectorLine.keySet()) {
                double gradient = 0.0;
                for (int i = 0; i < m; i++) {
                    AbstractFeatureLine xi = features.get(i);
                    double yi = xi.getTarget();

                    Double predict = predictCache.get(i);
                    Double err = (predict - yi);
                    if (xi.containsKey(j)) {
                        gradient += (err * xi.get(j));
                    }
                }
                /* System.out.println(gradient / m); */
                Double theta = weightMap.get(j) - (alpha * (gradient / m));
                weightMap.put(j, theta);
            }
        }
    }

    /**
     * 正例的概率
     * @param z z
     * @return logit
     */
    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    private double maxminum(int feaIndex ,double feaval){
        double maxFeaVal = maxValMap.getOrDefault(feaIndex,1d);
        double minFeaVal = minValMap.getOrDefault(feaIndex,0d);
        return (feaval-minFeaVal) / (maxFeaVal-minFeaVal);
    }


    public Double predict(AbstractFeatureLine featureLine) {

        /*
           bias
         */
//        if (!featureLine.containsKey(-1)) {
//            featureLine.put(-1, 0.5);
//        }

        /**
         * weight 初始化
         */
        double logit = featureLine.dot(weightMap);
        return sigmoid(logit);
    }

    public List<Double> predict(FeatureMatrix features) {
        return features.stream()
                .map(this::predict)
                .collect(Collectors.toList());
    }

}

