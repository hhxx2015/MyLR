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
public class LR_hash implements Serializable {
    private final long serialVersionUID = 1L;

    private FeaMap weightMap;
    private double alpha;
    private int feaNub = 0;//特征数量
    private boolean b=false;//分类面标识，默认无分类面
    private int lineNub;


    public FeaMap getWeight() {
        return this.weightMap;
    }

    /**
     * 写出模型，如果有分类面，分类面写在最后一列
     * @param modelPath
     */
    public void writeWeight(String modelPath) {

    }

    public void initClassify(Features features) {
        this.lineNub = features.size();


        this.weightMap = new FeaMap();
        if(b){
            weightMap.putB();
        }
        for (int i = 0; i < feaNub; i++) {
            weightMap.put(i, 1.0);
        }
    }

    public LR_hash(int feaNub, double alpha, boolean b) {
        this.b = b;
        this.alpha = alpha;
        this.feaNub = feaNub;
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
        initClassify(features);
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

            for (Integer j : weightMap.keySet()) {
                double Allwrong=0.0;
                for (int i = 0; i < lineNub; i++){
                    Feature feature = features.get(i);
                    FeaMap feaMap = feature.getFeaMap();
                    Double lable = feature.getLableValue();
                    double prediction1 = preMap.get(i);
                    if(feaMap.containsKey(j)){
                        double wrong = (prediction1 - lable);
                        Allwrong+=(wrong * feaMap.get(j));
                    }
                }
                Double newW = weightMap.get(j) - (alpha*(Allwrong/(lineNub+0.0)));
                weightMap.put(j,newW);
            }

//            for (int j = 0; j < feaNub; j++) {
//                double Allwrong=0.0;
//                for (int i = 0; i < lineNub; i++){
//                    Feature feature = features.get(i);
//                    Integer lable = feature.getLableValue();
//                    FeaMap feaMap = feature.getFeaMap();
//                    double prediction1 = preMap.get(i);
//                    //double prediction1 = classify(feaMap);
//                    if(feaMap.containsKey(j)){
//                        double wrong =( prediction1 - (lable+0.0) );
//                        Allwrong+=(wrong * feaMap.get(j));
//                    }
//                }
//                Double newW = weightMap.get(j) - (alpha*(Allwrong/(lineNub+0.0)));
//                weightMap.put(j,newW);
//            }

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
    public double classify(FeaMap feamap) {
        double logit=0.0;
        for (Integer feanub:feamap.keySet()) {
            Double x = feamap.get(feanub);
            Double w = weightMap.get(feanub);
            logit += (x*w) ;
        }
        return sigmoid(logit);
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
		int iter = 5000;
		double alpha=0.5;

		boolean b = true;
		//配置lr
        LR_hash lrh = new LR_hash(5, alpha, b);

        Features feas = new Features();
        FeaMap fm = new FeaMap();
        fm.put(0, 1.0);
        fm.put(1, 0.8);
        fm.put(2, 0.7);
        fm.put(3, 0.6);
        fm.put(4, 0.2);
        Feature a1 = new Feature("1", fm);
        fm = new FeaMap();
        fm.put(0, 0.9);
        fm.put(1, 0.8);
        fm.put(2, 0.6);
        fm.put(3, 0.9);
        fm.put(5, 0.2);
        Feature a2 = new Feature("1", fm);
        feas.add(a1);
        feas.add(a2);

        fm = new FeaMap();
        fm.put(0, 0.1);
        fm.put(1, 0.1);
        fm.put(2, 0.3);
        fm.put(3, 0.4);
        Feature b1 = new Feature("0", fm);
        fm = new FeaMap();
        fm.put(0, 0.2);
        fm.put(1, 0.3);
        fm.put(2, 0.4);
        fm.put(3, 0.1);
        fm.put(4, 0.2);
        Feature b2 = new Feature("0", fm);
        feas.add(b1);
        feas.add(b2);
        //feas.setFeaNub(10);

        lrh.train(iter, feas);

        System.out.println(lrh.getWeight());
        System.out.println(lrh.classify(feas));

	}
}
