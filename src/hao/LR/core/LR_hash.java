package hao.LR.core;

/**
 * Created by hao on 16-8-9.
 */

import hao.LR.entity.FeaMap;
import hao.LR.entity.Features;
import hao.LR.entity.Feature;

import java.io.*;
import java.util.*;

/**
 * @author hao
 * @version v2.0
 */
public class LR_hash {
    private final String VERSION="LR_hash v2.0";

    private FeaMap weightMap;
    private double alpha;
    private int feaNub;//特征数量
    private boolean b=false;//分类面标识，默认无分类面
    private int lineNub;

    public HashMap<Integer,Double> getWeight(){
        return this.weightMap;
    }

    /**
     * 写出模型，如果有分类面，分类面写在最后一列
     * @param modelPath
     */
    public void writeWeight(String modelPath) {

    }

    /**
     * 构造方法1，用于声明分类器参数，初始化模型weight数组
     * @param feaNub
     * @param alpha
     * @param b
     */
    public LR_hash(int feaNub , double alpha , boolean b) {
        //super();
        if(b){
            feaNub = feaNub+1;
            this.weightMap.putB();
        }
        this.feaNub = feaNub;
        this.alpha = alpha;
        this.weightMap = new FeaMap(feaNub);
    }

    /**
     * 构造方法2，用于加载模型
     */
    public LR_hash(String ModelPath) {

    }
    /**
     * @param itea 迭代次数
     * @param features 实例数组
     */
    public void train(int itea,Features features){
        this.lineNub = features.size();

        for (int it = 0; it < itea; it++) {

            HashMap <Integer,Double> preMap = new HashMap<Integer,Double>();

            for (int i = 0; i < lineNub; i++){
                Feature feature = features.get(i);
                FeaMap feaMap = feature.getFeaMap();
                //添加分类面
                if(b)   {  feaMap.putB();   }

                double prediction1 = classify(feaMap);
                preMap.put(i,prediction1);
            }

            for (int j = 0; j < feaNub; j++) {
                double Allwrong=0.0;
                for (int i = 0; i < lineNub; i++){
                    Feature feature = features.get(i);
                    Integer lable = feature.getLable();
                    FeaMap feaMap = feature.getFeaMap();
                    double prediction1 = preMap.get(i);
                    //double prediction1 = classify(feaMap);
                    if(feaMap.containsKey(j)){
                        double wrong =( prediction1 - (lable+0.0) );
                        Allwrong+=(wrong * feaMap.get(j));
                    }
                }
                Double newW = weightMap.get(j) - (alpha*(Allwrong/(lineNub+0.0)));
                weightMap.put(j,newW);
            }

        }
    }
    /**
     * 正例的概率
     * @param z
     * @return
     */
    private double sigmoid(double z) {
        //return Math.exp(z) / (1.0 + Math.exp(z));//1
        return 1.0/ (1.0 + Math.exp(-z));//1
    }

    /**
     * 先求和后求概率
     * @param feamap 特征映射
     * @return
     */
    private double classify(FeaMap feamap) {
        double logit=0.0;
        for (Integer feanub:feamap.keySet()) {
            Double x = feamap.get(feanub);
            Double w = weightMap.get(feanub);
            logit += (x*w) ;
        }
        return sigmoid(logit);
    }

    /***↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分类部分↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓***/
    private double Accuracy=0.0;
    public  double getPrecision(){
        return this.Accuracy;
    }

    /**
     * 用于test
     * @param features
     * @return
     */
    public ArrayList<Double> classify(Features features) {
        ArrayList<Double> list = new ArrayList<>();
        for (Feature feature:features) {
            double score = classify(feature.getFeaMap());
            list.add(score);
        }
        return list;
    }

/***↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分类↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*****/

    /**
     * 特殊！！！！仅用于源检索实验test  longid
     * @param vectors
     * @return
     */
    public ArrayList<Double> classifyForLongid(ArrayList<Vector> vectors) {
        //?????
        return null;
    }

/*****************分主函数*************************************************************************	******************************************************/
	public static void main(String[] args) throws IOException {
		int feaNub=6;
		int iter = 5000;
		double alpha=0.5;

		boolean b = true;
		//配置lr
		LR_hash lrh = new LR_hash(feaNub,alpha,b);

	}
}
