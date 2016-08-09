package hao.LR.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;

/** 
 * 多类分类
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月10日 下午4:03:07 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class LR_multy {
	private final String VERSION="LRmulty_V1.0";
	private double weight[][];
	private double alpha;
	private int classNub;//特征数量
	private int feaNub;//特征数量
	private int b=0;//分类面标识，默认无分类面
	
	@Override
	public String toString() {
		return VERSION+"  feaNub="+ feaNub+",b="+ b+",[weight=" +Arrays.toString(weight) + ", alpha=" + alpha + "]";
	}

	public double[][] getWeight(){
		return this.weight;
	}
	
	/**
	 * 写出模型，如果有分类面，分类面写在最后一列
	 * @param modelPath
	 */
//	public void writeWeight(String modelPath) {
//		BufferedWriter bw =null;
//		try {
//			bw = new BufferedWriter(new FileWriter(new File(modelPath)));
//		} catch (IOException e) {
//			System.err.println("路径不存在");
//			e.printStackTrace();
//		}
//		try {
//			bw.write(VERSION);bw.flush();bw.newLine();
//			for (double w:weight) {
//				bw.write(w+" ");bw.flush();
//			}
//			bw.newLine();
//			bw.write("feaNub="+(feaNub-b));
//			bw.write(" b="+b);
//			bw.flush();bw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 构造方法1，用于声明分类器参数，初始化模型weight数组
	 * @param feaNub
	 * @param alpha
	 * @param b
	 */
	public LR_multy(int feaNub, double alpha,int b,int classNub) {
		//super();
		feaNub=feaNub+b;
		this.feaNub=feaNub;
		this.classNub=classNub;
		this.weight=new double [classNub][feaNub];
		this.b=b;
		for (int j = 0;j < classNub; j++) {
			for (int i = 0; i < feaNub; i++) {
				this.weight[j][i]=1.0;
			}
		}
		this.alpha = alpha;
	}
	/**
	 * 构造方法2，用于加载模型
	 */
//	public LR_multy(String ModelPath) {
//		//super();
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader(new FileReader(new File(ModelPath)));
//		} catch (FileNotFoundException e) {
//			System.err.println("模型不存在！");e.printStackTrace();
//		}
//		try {
//			if(br.readLine().equals(VERSION)){
//				String weights[]=br.readLine().split(" ");
//				System.out.print("loadmodel: ");
//				System.out.print(br.readLine()+" weights=[");
//				this.feaNub = weights.length;
//				this.weight=new double [ this.feaNub];
//				for (int i = 0; i < this.feaNub; i++) {
//					this.weight[i]=Double.parseDouble(weights[i]);
//					System.out.print(weight[i]);
//					System.out.print(",");
//				}
//				System.out.println("]");
//			}else{
//				System.err.println("版本错误：V2.0");
//			}
//		} catch (IOException e) {
//			System.err.println("模型格式错误！");e.printStackTrace();
//		}		
//	}
	
	/**
	 * @param itea迭代次数
	 * @param vectors特征数组
	 * train 分类面修改！！！！！！！！！！！！！！！！！！！！！！！！！！
	 * 
	 */
	public void train(int itea,ArrayList<Vector> vectors){
		int vNub = vectors.size();
		for (int it = 0; it < itea; it++) {	
			for (int cnub = 0; cnub < classNub; cnub++) {
				for (int j=0;j < feaNub ; j++) {
					double Allwrong=0.0;
					for (int i =0;i<vNub;i++) {
						Vector vecline = vectors.get(i);
						int lable = Integer.parseInt(vecline.getLable());
						if((cnub-lable)==0){
							lable=1;
						}else{
							lable=0;							
						}
						double onefea[] = vecline.getFeatures();
						double prediction1= classify(vecline)[cnub] ;//该行特征是1的概率
						double wrong =( prediction1 - lable);
						try{
							Allwrong+=(wrong*onefea[j]);						
						}catch( ArrayIndexOutOfBoundsException e){
							Allwrong+=(wrong*1.0);
						}	
					}
					weight[cnub][j] = weight[cnub][j] - (alpha*(Allwrong/(vNub+0.0))); 
				}
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
	private Double[] classify(Vector v) {
		Double re[] = new Double[classNub];
		double[] feas = v.getFeatures();
		double logit=0.0;
		for (int cnub = 0; cnub < classNub; cnub++) {
			for (int i = 0; i < feaNub; i++) {
				try{
					logit += (feas[i]*weight[cnub][i]) ;
				}catch( ArrayIndexOutOfBoundsException e){
					logit += (1.0*weight[cnub][i]) ;
				}
			}	
			re[cnub]=sigmoid(logit);
		}
		return re;
	}
/***↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分类部分↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓***/
	/**
	 * 用于test
	 * @param vectors
	 * @return
	 */
	public ArrayList<Double[]> classify(ArrayList<Vector> vectors) {
		ArrayList<Double[]> list = new ArrayList<>();
		for (Vector v:vectors) {
			Double[] score = classify(v);
			//System.out.println(score[0]);
			list.add(score);	
		}
		return list;
	}
	private static int big(Double[] d){
		double temp=-1000.0;
		int re=-1;
		for (int i = 0; i < d.length; i++) {
			if(d[i]>temp){
				temp = d[i];
				re=i;
			}
		}
		return re;
	}
	public static void writedoubleArray(ArrayList<Double[]> idList, String fileName) throws IOException {  
		BufferedWriter bw  = new BufferedWriter( new FileWriter(fileName));
	//	System.out.println(idList.size());
		for(Double[] d:idList){ 
			//System.out.print(big(d));
			bw.write(big(d)+"");bw.write("\t");
			for (Double dd:d) {
				//System.out.print("\t"+dd);
				bw.write(dd+"");bw.write("\t");
			}
			//System.out.println();
			bw.flush();bw.newLine();
		}
		bw.close(); 
	}
	
	public static void main(String[] args) throws IOException {
		int feaNub=29573;//46
		int itea = 500;
		double alpha=0.1;
		int classNub=3;
		int b=1;//是否加入分类面
		int fold=2;
		
		String trainPath="/home/hao/桌面/学科分类/fold/lr/fold"+fold+"/train/train.txt";
		String testPath="/home/hao/桌面/学科分类/fold/lr/fold"+fold+"/test/test.txt";
		String testPre1 = "/home/hao/桌面/学科分类/fold/lr/fold"+fold+"/pre/pre.txt";
		String trainPre1 = "/home/hao/桌面/学科分类/fold/lr/fold"+fold+"/trainpre/pre.txt";
		
		System.out.println("loadFea......");
		ArrayList<Vector> listTrain = LoadFeatures.loadDefineFeature(feaNub,new File(trainPath));
		ArrayList<Vector> listTest= LoadFeatures.loadDefineFeature(feaNub,new File(testPath));
		//System.out.println(listTrain.get(0).toString());
		//System.out.println(listTrain.get(1).toString());
		//System.out.println(listTrain.get(2).toString());
		System.out.println("init......");
		LR_multy lrc = new LR_multy(feaNub, alpha,b,classNub);
		System.out.println("train......");
		
		lrc.train(itea, listTrain);
		//System.out.println(lrc.toString());
		System.out.println("test......");

		writedoubleArray(lrc.classify(listTest), testPre1);
		writedoubleArray(lrc.classify(listTrain), trainPre1);
	//	System.out.println(lrc.classify(listTest));
		//System.out.println( lrc2.toString());
	}
	
}
