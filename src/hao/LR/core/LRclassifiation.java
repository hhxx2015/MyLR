package hao.LR.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import hao.LR.entity.Vector;

/**
 * lr实现分类
 * @author hao
 * @version v2.1 构造时加入了分类面选择，加入了权重模型写出和加载，更改了模型的更新方式，更改了加载特征的函数
 *  @date 2016-5-31 最后更改2016-8-1
 */
public class LRclassifiation {
	private final String VERSION="LRclassifiation_V2.1";

	private double weight[];
	private double alpha;
	private int feaNub;//特征数量
	private boolean b=false;//分类面标识，默认无分类面
	
	@Override
	public String toString() {
		return VERSION+"  feaNub="+ feaNub+",b="+ b+",[weight=" +Arrays.toString(weight) + ", alpha=" + alpha + "]";
	}

	public double[] getWeight(){
		return this.weight;
	}
	
	/**
	 * 写出模型，如果有分类面，分类面写在最后一列
	 * @param modelPath
	 */
	public void writeWeight(String modelPath) {
		BufferedWriter bw =null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(modelPath)));
			bw.write(VERSION);bw.flush();bw.newLine();
			for (double w:weight) {
				bw.write(w+" ");bw.flush();
			}
			bw.newLine();
			bw.write("feaNub="+(feaNub));
			bw.write(" b="+b);
			bw.flush();bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造方法1，用于声明分类器参数，初始化模型weight数组
	 * @param feaNub
	 * @param alpha
	 * @param b
	 */
	public LRclassifiation(int feaNub , double alpha , boolean b) {
		//super();
		if(b){
			feaNub = feaNub+1;
		}
		this.feaNub = feaNub;
		this.weight = new double [feaNub];
		this.b=b;
		for (int i = 0; i < feaNub; i++) {
			this.weight[i]=1.0;
		}
		this.alpha = alpha;
	}

	/**
	 * 构造方法2，用于加载模型
	 */
	public LRclassifiation(String ModelPath) {
		//super();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(ModelPath)));
		} catch (FileNotFoundException e) {
			System.err.println("模型不存在！");e.printStackTrace();
		}
		try {
			if(br.readLine().equals(VERSION)){
				String weights[]=br.readLine().split(" ");
				System.out.print("loadmodel: ");
				System.out.print(br.readLine()+" weights=[");
				this.feaNub = weights.length;
				this.weight=new double [ this.feaNub];
				for (int i = 0; i < this.feaNub; i++) {
					this.weight[i]=Double.parseDouble(weights[i]);
					System.out.print(weight[i]);
					System.out.print(",");
				}
				System.out.println("]");
			}else{
				System.err.println("版本错误：V2.0");
			}
			br.close();
		} catch (IOException e) {
			System.err.println("模型格式错误！");e.printStackTrace();
		}

		
	}
	/**
	 * @param itea 迭代次数
	 * @param vectors 实例数组
	 */
	public void train(int itea,ArrayList<Vector> vectors){
		int vNub = vectors.size();//得到实例数量
		for (int it = 0; it < itea; it++) {	//迭代次数
			double weightnow[] = weight;//临时更新的权重数组
			for (int j=0;j < feaNub ; j++) {//第j个特征
				double Allwrong=0.0;
				for (int i =0;i<vNub;i++) {//单独计算每一行实例的误差
					Vector vecline = vectors.get(i);//得到该行实例
					
					String lable = vecline.getLable();//得到标签y
					double onefea[] = vecline.getFeatures();//得到特征数组
					
					double prediction1 = classify(vecline);//该行特征是1的概率  w*x
					double wrong =( prediction1- Double.parseDouble(lable));//计算误差，wx-y
					//是否有分类面
					if(b) {
						try {
							Allwrong += (wrong * onefea[j]);            //叠加误差
						} catch (ArrayIndexOutOfBoundsException e) {
							Allwrong += (wrong * 1.0);                           //分类面
						}
					}else{
						Allwrong += (wrong * onefea[j]);            //叠加误差
					}

				}
				weightnow[j] = weightnow[j] - (alpha*(Allwrong/(vNub+0.0))); 	//更新第j个特征
			}
			weight = weightnow;
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
	 * @param v 特征数组
	 * @return
	 */
	private double classify(Vector v) {
		double[] feas = v.getFeatures();
		double logit=0.0;
		for (int i = 0; i < feaNub; i++) {
			try{
				logit += (feas[i]*weight[i]) ;
			}catch( ArrayIndexOutOfBoundsException e){
				//System.out.println(i);
				logit += (1.0*weight[i]) ;
			}
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
		Accuracy = (correct+0.0) / (vectors.size()+0.0);
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
//	public static void main(String[] args) throws IOException {
//		int feaNub=6;
//		int iter = 5000;
//		double alpha=0.5;
//
//		int b = 0;
//		//配置lr
//		LRclassifiation lrc = new LRclassifiation(feaNub, alpha);
//
//		 // 加载特征
//		//ArrayList<Vector> listtrain = LoadFeatures.loadFeatures2(feaNub+1,new File("../MyLR/Data/aa.txt"));
//		ArrayList<Vector> listtrain = LoadFeatures.loadSvmFeature(feaNub,new File("../MyLR/Data/05/Meteor_Score.txt"));
//		//train
//		lrc.train(iter,listtrain);
//	
//		double weight[] = lrc.getWeight();
//		System.out.print("weight: ");
//		for (int i = 0; i < weight.length; i++) {
//			System.out.print(weight[i]+" ");
//		}
//		System.out.println();
//		
//		//classify
//		System.out.println(lrc.classify(listtrain));
//		System.out.println(lrc.getPrecision());
////		writeFile.writeResult(n.classifyAll(listtrain), 
////				"/home/hao/桌面/b");
//				//"/home/hao/桌面/分类任务/data/noQID/train/featurePospre.txt");
//	}
}
