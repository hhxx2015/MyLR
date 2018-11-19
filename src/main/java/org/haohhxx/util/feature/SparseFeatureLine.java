package org.haohhxx.util.feature;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author zhenyuan_hao@163.com
 */
public class SparseFeatureLine extends AbstractFeatureLine{

    private double target;

    public HashMap<Integer,Double> vectorLine = new HashMap<>();

    public SparseFeatureLine(){

    }

    public SparseFeatureLine(LineDataType lineDataType, String line){
        switch (lineDataType){
            case svm: this.loadSVMLine(line);break;
            case csv: this.loadCSVLine(line);break;
            default:System.out.println("wrong data type!");
        }
    }

    public SparseFeatureLine(double target){
        this.target=target;
    }

    private void loadCSVLine(String csvLine){
        String [] ls = csvLine.split(",");
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            this.put(i-1,Double.parseDouble(ls[i]));
        }
    }

    private void loadSVMLine(String svmLine){
        svmLine = svmLine.split("#")[0].trim();
        String [] ls = svmLine.split("\\s");
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            this.put(Integer.parseInt(node[0]),Double.parseDouble(node[1]));
        }
    }

    @Override
    public AbstractFeatureLine sub(VectorLine sx2){
        SparseFeatureLine x2 = (SparseFeatureLine)sx2;
        SparseFeatureLine vectorLine = new SparseFeatureLine();
        Set<Integer> intersectionNodeSet = Sets.union(this.vectorLine.keySet(), x2.vectorLine.keySet());
        for (Integer featureNodeIndex : intersectionNodeSet){
            double sub = this.vectorLine.getOrDefault(featureNodeIndex,0.0) -  x2.vectorLine.getOrDefault(featureNodeIndex,0.0);
            vectorLine.put(featureNodeIndex,sub);
        }
        return vectorLine;
    }


    @Override
    public double dot(VectorLine x2){
        double sum = 0.0;
        for (Integer featureNodeIndex : this.vectorLine.keySet()){
            if(x2.containsKey(featureNodeIndex)){
                sum += this.get(featureNodeIndex) *  x2.get(featureNodeIndex);
            }
        }
        return sum;
    }

    /**
     * 平方
     * @return
     */
    @Override
    public double pow2(){
        double sum = 0.0;
        for (Integer featureNodeIndex : this.vectorLine.keySet()){
            sum += Math.pow(this.get(featureNodeIndex), 2);
        }
        return sum;
    }

    @Override
    public boolean put(int featureNodeIndex, double val){
        vectorLine.put(featureNodeIndex,val);
        return true;
    }

    @Override
    public double get(int featureNodeIndex){
        return vectorLine.getOrDefault(featureNodeIndex, 0.0);
    }

    @Override
    public boolean containsKey(int featureNodeIndex){
        return vectorLine.containsKey(featureNodeIndex);
    }

    @Override
    public double getTarget() {
        return target;
    }

    @Override
    public void setTarget(double target) {
        this.target = target;
    }

}



