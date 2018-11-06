package org.haohhxx.util.core;

import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogisticRegression {

    private VectorLine weightMap = new VectorLine();

    /**
     * @param itea     迭代次数
     * @param features 实例数组
     */
    public void train(int itea, VectorMatrix features) {
        for (int it = 0; it < itea; it++) {
            HashMap<Integer, Double> preMap = new HashMap<Integer, Double>();
            for (VectorLine vectorLine:features) {
                double predict = classifyLine(vectorLine);
                preMap.put(i, predict);
            }

            for (Integer j : weightMap.keySet()) {
                double Allwrong = 0.0;
                for (int i = 0; i < lineNub; i++) {
                    Feature feature = features.get(i);

                    FeaMap feaMap = feature.getFeaMap();
                    Double label = feature.getLableValue();
                    Double prediction1 = preMap.get(i);
                    if (feaMap.containsKey(j)) {
                        Double wrong = (prediction1 - label);
                        Allwrong += (wrong * feaMap.get(j));
                    }
                }
                Double newW = weightMap.get(j) - (alpha * (Allwrong / (lineNub + 0.0)));
                weightMap.put(j, newW);
            }
        }
    }


    /**
     * 正例的概率
     * @param z
     * @return
     */
    private double sigmoid(double z) {
        //return Math.exp(z) / (1.0 + Math.exp(z));
        return 1.0 / (1.0 + Math.exp(-z));
    }


    public double classifyLine(VectorLine featureLine) {
        double logit = 0.0;
        for (Map.Entry<Integer,Double> featureNode : featureLine.entrySet()) {
            Integer featureIndex = featureNode.getKey();
            Double featureValue = featureNode.getValue();

            Double weightValue = weightMap.get(featureIndex);
            logit += (featureValue * weightValue);
        }
        return sigmoid(logit);
    }

    /**
     * 用于test
     * @param features
     * @return
     */
    public ArrayList<Double> classify(VectorMatrix features) {
        ArrayList<Double> list = new ArrayList<Double>();
        for (VectorLine feature : features) {
            double score = classifyLine(feature);
            list.add(score);
        }
        return list;
    }

}

