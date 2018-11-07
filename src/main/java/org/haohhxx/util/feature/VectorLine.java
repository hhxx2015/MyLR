package org.haohhxx.util.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author zhenyuan_hao@163.com
 */
public class VectorLine extends HashMap<Integer,Double> {

    private double target;

    public VectorLine(){

    }

    public VectorLine(double target){
        this.target=target;
    }

    public VectorLine(String svmLine){
        String [] ls = svmLine.split("\\s");
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            this.put(Integer.parseInt(node[0]),Double.parseDouble(node[1]));
        }
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

}



