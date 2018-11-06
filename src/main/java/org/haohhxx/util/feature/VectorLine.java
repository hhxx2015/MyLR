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

    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

}



