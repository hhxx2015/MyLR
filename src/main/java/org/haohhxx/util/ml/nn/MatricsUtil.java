package org.haohhxx.util.ml.nn;

public class MatricsUtil {



    public static double dot(double[] v1,double[] v2) {
        if(v1.length!=v2.length){
            try {
                throw new Exception("dot v1 size != v2 size");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1.0;
        }
        double sum=0.0;
        for (int i = 0; i <v1.length ; i++) {
            sum += (v1[i] * v2[i]);
        }
        return sum;
    }

}
