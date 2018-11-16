package org.haohhxx.util.feature;


import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Set;

/**
 * @author zhenyuan_hao@163.com
 */
public class VectorLine extends HashMap<Integer,Double> {

    private double target;

    public VectorLine(){

    }

    public VectorLine(LineDataType lineDataType, String line){
        switch (lineDataType){
            case svm: this.loadSVMLine(line);break;
            case csv: this.loadCSVLine(line);break;
            default:System.out.println("wrong data type!");
        }
    }

    public VectorLine(double target){
        this.target=target;
    }

    private void loadCSVLine(String csvLine){
        String [] ls = csvLine.split(",");
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            super.put(i-1,Double.parseDouble(ls[i]));
        }
    }

    private void loadSVMLine(String svmLine){
        String [] ls = svmLine.split("\\s");
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            this.put(Integer.parseInt(node[0]),Double.parseDouble(node[1]));
        }
    }

    /**
     * 按位相减
     * @param x2 x2
     * @return
     */
    public VectorLine sub(VectorLine x2){
        VectorLine vectorLine = new VectorLine();
        Set<Integer> intersectionNodeSet = Sets.union(this.keySet(), x2.keySet());
        for (Integer featureNodeIndex : intersectionNodeSet){
            double sub = this.getOrDefault(featureNodeIndex,0.0) -  x2.getOrDefault(featureNodeIndex,0.0);
            vectorLine.put(featureNodeIndex,sub);
        }
        return vectorLine;
    }

    /**
     * 对两向量进行点积
     * @param x2 x2
     * @return
     */
    public double dot(VectorLine x2){
        double sum = 0.0;
        Set<Integer> intersectionNodeSet = Sets.intersection(this.keySet(), x2.keySet());
        for (Integer featureNodeIndex : intersectionNodeSet){
            sum += this.get(featureNodeIndex) *  x2.get(featureNodeIndex);
        }
        return sum;
    }

    /**
     * 平方
     * @return
     */
    public double pow2(){
        double sum = 0.0;
        for (Integer featureNodeIndex : this.keySet()){
            sum += Math.pow(this.get(featureNodeIndex), 2);
        }
        return sum;
    }


    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public enum LineDataType{
        /**
         * 特征文件格式
         */
        svm(), csv()
    }
}



