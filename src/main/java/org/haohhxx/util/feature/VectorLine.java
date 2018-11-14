package org.haohhxx.util.feature;


import java.util.HashMap;

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



