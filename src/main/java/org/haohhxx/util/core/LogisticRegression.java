package org.haohhxx.util.core;

import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author zhenyuan_hao@163.com
 */
public class LogisticRegression {

    private final Random random = new Random();
    private final double alpha = 0.0001;
    private VectorLine weightMap = new VectorLine();


    /**
     * @param itea     迭代次数
     * @param features 实例数组
     */
    public void fit(int itea, VectorMatrix features) {
        int m = features.size();
        for (int it = 0; it < itea; it++) {
            HashMap<Integer, Double> preMap = new HashMap<>(m);
            for (int i = 0; i < m ; i++) {
                VectorLine xi = features.get(i);
                double predict = predict(xi);
                preMap.put(i, predict);
            }

            for (Integer j : weightMap.keySet()) {
                double gradient = 0.0;
                for (int i = 0; i < m; i++) {
                    VectorLine vectorLine = features.get(i);
                    double yi = vectorLine.getTarget();

                    Double predict = preMap.get(i);
                    if (vectorLine.containsKey(j)) {
                        Double err = (predict - yi);
                        gradient += (err * vectorLine.get(j));
                    }
                }
                Double theta = weightMap.getOrDefault(j, random.nextDouble()) - (alpha * (gradient / m));
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


    public double predict(VectorLine featureLine) {
        double logit = 0.0;
        for (Map.Entry<Integer,Double> featureNode : featureLine.entrySet()) {
            Integer featureIndex = featureNode.getKey();
            Double featureValue = featureNode.getValue();
            weightMap.computeIfAbsent(featureIndex,k-> random.nextDouble());
            Double weightValue = weightMap.get(featureIndex);
            logit += (featureValue * weightValue);
        }
        return sigmoid(logit);
    }


}

