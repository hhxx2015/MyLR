package LR;

import java.io.*;
import java.util.*;

import model.Vector;
import tools.LoadFeatures;
import tools.writeFile;

/**
 * 用于在MQ2008上测试验证线性排序可行性
 * 该类实现内容与classifiation一样
 * @author hao
 * @version v1.0
 * @since v1.0
 */
public class LR_ranking {
	private double weight[];
	private double alpha;
	private int feaNub;
	
	@Override
	public String toString() {
		return "newLR [weight=" + Arrays.toString(weight) + ", alpha=" + alpha + "]";
	}

	public double[] getWeight(){
		return this.weight;
	}
	
	public LR_ranking(int feaNub, double alpha) {
		//super();
		this.feaNub=feaNub;
		this.weight=new double [feaNub];
		for (int i = 0; i < feaNub; i++) {
			this.weight[i]=1.0;
		}
		this.alpha = alpha;
	}

	/**
	 * @param itea迭代次数
	 * @param vectors特征数组
	 */
	public void train(int itea,ArrayList<Vector> vectors){
		int m = vectors.size();
		for (int it = 0; it < itea; it++) {	
			for (int j=0;j < feaNub ; j++) {
				double Allwrong=0.0;
				for (int i =0;i<m;i++) {
					Vector vecline = vectors.get(i);
					String lable = vecline.getLable();
					double onefea[] = vecline.getFeatures();
					double prediction1 = classify(vecline);//该行特征是1的概率
					double wrong =( prediction1- Double.parseDouble(lable) );
					Allwrong+=(wrong*onefea[j]);
				}
				weight[j] = weight[j] - (alpha*(Allwrong/(m+0.0))); 
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
	 * @param 特征数组
	 * @return
	 */
	private double classify(Vector v) {
		double[] feas = v.getFeatures();
		double logit=0.0;
		for (int i = 0; i < feaNub; i++) {
			logit += (feas[i]*weight[i]) ;//0
		}
		return sigmoid(logit);
	}
/*****************分类***********************************************************************************************************/
	private double Accuracy=0.0;
	
	public  double getPrecision(){
		return this.Accuracy;
	}
	
	/**
	 * 用于test
	 * @param vectors
	 * @return
	 */
	public ArrayList<Double> classify(ArrayList<Vector> vectors) {
		int correct=0;
		ArrayList<Double> list = new ArrayList<>();
		for (Vector v:vectors) {
			double score = classify(v);
			if(score>=0.5&&v.getLable().equals("1")){
				correct++;
			}else if(score<0.5&&v.getLable().equals("0")){
				correct++;
			}
			list.add(score);
		}
		Accuracy = (correct+0.0) / (list.size()+0.0);
		return list;
	}
/*****************其他方法*************************************************************************	******************************************************/
	//加载本地模型
	//写出本地模型
	
/*****************分主函数*************************************************************************	******************************************************/
	public static void main(String[] args) {
		
	}
	
	
	public static void main1(String[] args) throws IOException {
		
		int feaNub=46;//46
		
		int iter = 5;
		double alpha=0.1;
		int b=0;//是否加入分类面
		
		//配置lr
		LR_ranking lrc = new LR_ranking(feaNub, alpha);
		if(b==1){
			feaNub++;
		}
		for (int fold = 1; fold <= 5; fold++) {
			// 加载特征
			ArrayList<Vector> listtrain = LoadFeatures.loadSVM_RankFea(feaNub,new File("/home/hao/实验/MQ2008/Fold"+fold+"/train.txt"));
			//train
			//System.out.println(listtrain.get(0).getFeatures().length);
			lrc.train(iter,listtrain);
			
			double weight[] = lrc.getWeight();
//
			/**
			 * qid vector
			 */
			ArrayList<Vector> testList  = LoadFeatures.loadSVM_RankFeaToClassify(feaNub,new File(
					"/home/hao/实验/MQ2008/Fold"+fold+"/test.txt"));
			writeFile.writeResult(lrc.classify(testList), 
					"/home/hao/实验/lreval/Fold"+fold+"/test_re5.txt");
			
			ArrayList<Vector> valiList  = LoadFeatures.loadSVM_RankFeaToClassify(feaNub,new File(
					"/home/hao/实验/MQ2008/Fold"+fold+"/vali.txt"));
			writeFile.writeResult(lrc.classify(valiList), 
					"/home/hao/实验/lreval/Fold"+fold+"/vali_re5.txt");
			
			//	System.out.println(lrc.getPrecision());
			//"/home/hao/桌面/分类任务/data/noQID/train/featurePospre.txt");
		}
		
	}
}
