package hao.LR.entity;

import java.util.HashMap;

/**
 * 特征类
 * Created by hao on 16-8-9.
 */
public class Feature {
    private String lable;
    private Double lableValue;
    private FeaMap feaMap;

//    public Feature() {
//测试用
//    }

    public Feature(FeaMap feaMap) {
        this.feaMap = feaMap;
    }


    public Feature(String lable, FeaMap feaMap) {
        this.lable = lable;
        this.feaMap = feaMap;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "lable=" + lable +
                ", feaMap=" + feaMap +
                '}';
    }

    public void setLableValue(Double i) {
        this.lableValue = i;
    }

    public Double getLableValue() {
        return this.lableValue;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public FeaMap getFeaMap() {
        return feaMap;
    }

    public void setFeaMap(FeaMap feaMap) {
        this.feaMap = feaMap;
    }

    public void putFea(Integer key,Double value){
        this.feaMap.put(key,value);
    }

}
