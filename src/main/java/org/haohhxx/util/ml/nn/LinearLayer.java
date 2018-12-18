package org.haohhxx.util.ml.nn;



public class LinearLayer implements Layer {

    private double bias;
    private double rate;
    private double[][] hidden;
    /**
     * 本层节点的输出
     */
    private double[] out;
    /**
     * 上一层节点的输出
     */
    private double[] lastOut;
    private int hiddenSize;
    private int inputSize = 0;

    public LinearLayer(int hiddenSize){
        this.hiddenSize = hiddenSize;
        this.rate = rate;
        this.out = new double[hiddenSize];
    }

    /**
     * 初始化数据
     */
    private void init(){

    }

    @Override
    public double[] forward(double[] input) {
        this.inputSize = input.length;
        this.lastOut = input;
//        node_nub * last_node_nub
        hidden = new double[hiddenSize][inputSize];

        for (int i = 0; i < hiddenSize; i++) {
            double pre = MatricsUtil.dot(input, hidden[i]);
            out[i] = activationFunction(pre);
        }
        return out;
    }

    public double[] backward(double[] delta){

        upDate(delta);

        if(inputSize==0.0){
            return null;
        }

        double[] bpDelta = new double[inputSize];
        for (int j = 0; j < inputSize ; j++) {
            double ret=0.0;
            for (int i = 0; i < hiddenSize ; i++){
                ret += (hidden[i][j] * delta[i]);
            }
            bpDelta[j] = lastOut[j] * (1-lastOut[j]) * ret;
        }
        return bpDelta;
    }

    private void upDate(double[] delta){
        for (int i = 0; i <hiddenSize ; i++) {
            for (int j = 0; j < inputSize ; j++) {
                double gradient = delta[i] * lastOut[j];
                hidden[i][j] += rate * gradient;
            }

        }
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
