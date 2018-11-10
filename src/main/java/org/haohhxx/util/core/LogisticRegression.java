package org.haohhxx.util.core;

import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;

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
    private VectorLine weightMap = new VectorLine();
    private Map<Integer,Double> maxValMap = new HashMap<>();
    private Map<Integer,Double> minValMap = new HashMap<>();

    /**
     * @param itea     迭代次数
     * @param features 实例数组
     */
    public void fit(int itea, VectorMatrix features) {

        int m = features.size();
        this.maxValMap = features.getMax();
        this.minValMap = features.getMin();

        for (int it = 0; it < itea; it++) {

            HashMap<Integer, Double> preMap = new HashMap<>(m);
            /*
              梯度下降  全部数据迭代一次后更新 weight
             */
            for (int i = 0; i < m ; i++){
                VectorLine xi = features.get(i);
                double predict = predict(xi);
                preMap.put(i, predict);
            }

            for (Integer j : weightMap.keySet()) {
                double gradient = 0.0;
                for (int i = 0; i < m; i++) {
                    VectorLine xi = features.get(i);
                    double yi = xi.getTarget();

                    Double predict = preMap.get(i);
                    if (xi.containsKey(j)) {
                        Double err = (predict - yi);
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


    public Double predict(VectorLine featureLine) {

        /*
           bias
         */
        if (!featureLine.containsKey(-1)) {
            featureLine.put(-1, 0.5);
        }

        double logit = 0.0;
        for (Map.Entry<Integer,Double> featureNode : featureLine.entrySet()) {
            Integer featureIndex = featureNode.getKey();
            Double featureValue = featureNode.getValue();
            featureValue = this.maxminum(featureIndex, featureValue);

            weightMap.computeIfAbsent(featureIndex,k-> random.nextDouble());
            Double weightValue = weightMap.get(featureIndex);
            logit += (featureValue * weightValue);
        }
        return sigmoid(logit);
    }

    public List<Double> predict(VectorMatrix features) {
        return features.stream()
                .map(this::predict)
                .collect(Collectors.toList());
    }

}

