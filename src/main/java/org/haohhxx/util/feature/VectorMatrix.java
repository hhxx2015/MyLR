package org.haohhxx.util.feature;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author zhenyuan_hao@163.com
 */
public class VectorMatrix extends ArrayList<VectorLine> {

    public VectorMatrix(){

    }

    private HashMap<Integer,Double> max = new HashMap<>(256);
    private HashMap<Integer,Double> min = new HashMap<>(256);


    @Override
    public boolean add(VectorLine vectorLine){
        vectorLine.forEach((index,value)->{
            double maxOrDefault = max.getOrDefault(index,Double.MIN_VALUE);
            if(maxOrDefault<value){
                max.put(index,value);
            }
            double minOrDefault = min.getOrDefault(index,Double.MAX_VALUE);
            if(minOrDefault>value){
                min.put(index,value);
            }
        });
        return super.add(vectorLine);
    }

    public HashMap<Integer, Double> getMax() {
        return max;
    }

    public HashMap<Integer, Double> getMin() {
        return min;
    }
}
