package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 特征类
 * @author hao
 
*
*      
*          ┌─┐       ┌─┐
*       ┌──┘ ┴───────┘ ┴──┐
*       │                 │
*       │       ───       │
*       │  ─┬┘       └┬─  │
*       │                 │
*       │       ─┴─       │
*       │                 │
*       └───┐         ┌───┘
*           │         │
*           │         │
*           │         │
*           │         └──────────────┐
*           │                        │
*           │                        ├─┐
*           │                        ┌─┘    
*           │                        │
*           └─┐  ┐  ┌───────┬──┐  ┌──┘         
*             │ ─┤ ─┤       │ ─┤ ─┤         
*             └──┴──┘       └──┴──┘ 
*                 神兽保佑 
*                 代码无BUG! 
*/


public class Vector {
	
	private double features[];
	private String lable;
	
	public Vector(double[] features, String lable) {
		//super();
		this.features = features;
		this.lable = lable;
	}

	@Override
	public String toString() {
		return "vector [ lable="+lable+",features=" + Arrays.toString(features)  + "]";
	}
	
	public double[] getFeatures() {
		return features;
	}
	public String getLable() {
		return lable;
	}
	
	/**
	 * 两个svmrank的特征比较，得到两个普通svm特征
	 * 比之前加入分类面
	 * @param v2
	 * @return
	 */
	public ArrayList<Vector> compare(Vector v2){
		//Vector vr[]  = new Vector[2];
		ArrayList<Vector> vre = new ArrayList<>();
		int leb1 = Integer.parseInt(lable);
		int leb2 = Integer.parseInt(v2.getLable());
		if(leb1>leb2){
			vre.add(new Vector(featureSUB(features,v2.getFeatures()), "1"));
			vre.add(new Vector(featureSUB(v2.getFeatures(),features), "0"));
		}else if(leb1<leb2){
			vre.add(new Vector(featureSUB(features,v2.getFeatures()), "0"));
			vre.add(new Vector(featureSUB(v2.getFeatures(),features), "1"));
		}
		return vre;
	}
	
	/**
	 * 两行数组相减
	 * @param fea1
	 * @param fea2
	 * @return
	 */
	private double[] featureSUB(double fea1[],double fea2[]){
		double re[] = new double[fea1.length];
		
		for (int i = 0; i < fea1.length; i++) {
			//System.out.println(fea2.length);
			re[i]=fea1[i]-fea2[i];
		}
		return re;
	}
	public static void main(String[] args) {
		System.out.println(Math.log((int)Math.E));
		System.out.println(Math.log(Math.E));
	}
}

