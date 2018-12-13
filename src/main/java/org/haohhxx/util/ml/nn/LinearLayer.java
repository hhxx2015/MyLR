package org.haohhxx.util.ml.nn;



public class LinearLayer implements Layer {

    private double bias;
    private double hidden[][];

    private int hiddenSize;
    private int inputSize = 0;

    public LinearLayer(int hiddenSize){
        this.hiddenSize = hiddenSize;
    }

    /**
     * 初始化数据
     */
    private void init(){

    }

    @Override
    public double[] forward(double[] input) {
        this.inputSize = input.length;
        hidden = new double[hiddenSize][inputSize];
        double[] out = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double pre = MatricsUtil.dot(input, hidden[i]);
            out[i] = activationFunction(pre);
        }
        return out;
    }


    private double activationFunction(double z){
        return sigmoid(z);
    }

    /**
     * 正例的概率
     * @param z z
     * @return logit
     */
    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

}
