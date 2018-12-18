package org.haohhxx.util.ml.nn;



class MLP implements Layer{
    private LinearLayer linearLayer1 = new LinearLayer(100);
    private LinearLayer linearLayer2 = new LinearLayer(100);
    private LinearLayer linearLayer3 = new LinearLayer(2);


    @Override
    public double[] forward(double[] input) {
        double[] o1 = linearLayer1.forward(input);
        double[] o2 = linearLayer2.forward(o1);
        double[] o3 = linearLayer3.forward(o2);
        return o3;
    }



    public double[] backward(double[] pre, double[] y) {
        int targetNub = y.length;
        double[] delta = new double[targetNub];
        for (int i = 0; i < targetNub; i++) {
            delta[i] = (y[i] - pre[i]) * pre[i] * (1.0 - pre[i]);
        }

        double[] dt3 = linearLayer3.backward(delta);
        double[] dt2 = linearLayer3.backward(dt3);
        linearLayer3.backward(dt2);
        return null;
    }
}



/**
 * @author zhenyuan_hao@163.com
 */
public class MultiLayerPerceptronet {

    private MLP mlp = new MLP();
    private double lrate = 0.01;

    public void fit(double[][] features, double[][] target){

        double[] pre = mlp.forward(features[0]);

        backPropagation(pre, target[0]);

    }


    /**
     * Cross Entropy
     * @return
     */
    public double loss(){


        return 0.0;
    }


    public void backPropagation(double[] pre, double[] y) {
        int targetNub = y.length;
        double[] err = new double[targetNub];
        for (int i = 0; i < targetNub; i++) {
            err[i] = (y[i] - pre[i]) * pre[i] * (1.0 - pre[i]);
        }

    }

}
