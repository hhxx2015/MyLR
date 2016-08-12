package hao.LR.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import hao.LR.entity.Vector;
import hao.LR.entity.Vector_vote;
import hao.LR.util.io.LoadFeatures;
import l2r4sr2016.eval.EvalEasy;
import tools.io.FeatureLoader;
import tools.writeFile;
import util.readRankResultForDetect_v2;

/**
 * lr实现分类vote版本
 * @author hao
 * @version v1.0
 *  @date 2016-5-31
 */
public class LRclassifiation_vote {
	private final String VERSION="LRclassifiation_vote_V1.0";
	private double weight[];
	private double alpha;
	private int feaNub;//特征数量
	private int b=0;//分类面标识，默认无分类面
	
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
		} catch (IOException e) {
			System.err.println("路径不存在");
			e.printStackTrace();
		}
		try {
			bw.write(VERSION);bw.flush();bw.newLine();
			for (double w:weight) {
				bw.write(w+" ");bw.flush();
			}
			bw.newLine();
			bw.write("feaNub="+(feaNub-b));
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
	public LRclassifiation_vote(int feaNub, double alpha,int b) {
		//super();
		feaNub=feaNub+b;
		this.feaNub=feaNub;
		this.weight=new double [feaNub];
		this.b=b;
		for (int i = 0; i < feaNub; i++) {
			this.weight[i]=1.0;
		}
		this.alpha = alpha;
	}
	/**
	 * 构造方法2，用于加载模型
	 */
	public LRclassifiation_vote(String ModelPath) {
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
		} catch (IOException e) {
			System.err.println("模型格式错误！");e.printStackTrace();
		}
	}
	/**
	 * @param itea 迭代次数
	 * @param vectors 特征数组
	 * 改成随机梯度下降??
	 * @throws  
	 */
	public void train(int itea,ArrayList<Vector_vote> vectors){
		//String modelsfile="/home/hao/桌面/mylr/vote/model/modelss.txt";
		try {
			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelsfile), "UTF-8"));
		
			int vNub = vectors.size();
			for (int it = 0; it < itea; it++) {	
				//System.out.print("itea "+it+" :");
				for (int j=0;j < feaNub ; j++) {
					double Allwrong=0.0;
					for (int i =0;i<vNub;i++) {
						Vector_vote vecline = vectors.get(i);
						String lable = vecline.getLable();
						double logvote=vecline.getLogvote();
						double onefea[] = vecline.getFeatures();
						double prediction1 = classify(vecline);//该行特征是1的概率
						double wrong =( prediction1- Double.parseDouble(lable) )* logvote;
						try{
							Allwrong+=(wrong*onefea[j]);						
						}catch( ArrayIndexOutOfBoundsException e){
							Allwrong+=(wrong*1.0);
						}
					}
					weight[j] = weight[j] - (alpha*(Allwrong/(vNub+0.0))); 
					//bw.write(" "+weight[j]);
					//System.out.print(j+"-"+weight[j]+" ");
				}
				//bw.flush();bw.newLine();
				//System.out.println();
			}
			//bw.close();
		} catch ( Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	/**
	 * 用于test
	 * @param vectors
	 * @return
	 */
	public ArrayList<Double> classify(ArrayList<Vector> vectors) {
		ArrayList<Double> list = new ArrayList<>();
		for (Vector v:vectors) {
			double score = classify(v);
			list.add(score);
		}
		return list;
	}
	
	static void printTime(){
		//Long d = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
		System.out.println(sdf.format(new Date()));
	}
/***↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分类↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 * @throws IOException *****/
	public static void main(String[] args) throws IOException {
		double alpha=0.1;
		int itea = 500;
		int b = 1;

		String base = "normal20";//normal20 willams_10vote
		int feaNub = 20;
		String vote = "_lrvote";//_vote

		String trainfile = "/home/hao/桌面/mylr/old/train/" + base + ".txt";
		String testFile = "/home/hao/桌面/mylr/old/test/" + base + "/";
		String modelPath="../MyLR/Data/detect/feature/model/"+base+"_"+alpha+"_"+itea+vote+".txt";


		String trainAll = "/home/hao/桌面/mylr/old/train/" + base + "/";
		String rankPre = "/home/hao/桌面/mylr/old/votepre/" + base + "/";//willams20_vote willams_logvote
		String trainrankPre = "/home/hao/桌面/mylr/old/votePreTrain/" + base + "/";//willams20_vote willams_logvote
		String ranklongidre = "/home/hao/桌面/mylr/old/voteLongidre/" + base + "/";
		String rankTrainlongidre = "/home/hao/桌面/mylr/old/voteTrainlongidre/" + base + "/";
		new File(rankPre).mkdirs();
		new File(trainrankPre).mkdirs();
		new File(ranklongidre).mkdirs();
		new File(rankTrainlongidre).mkdirs();


		System.out.println(base+"");

//		System.out.print("loadfeatures......");	printTime();
//		ArrayList<Vector_vote> trainfeaList = LoadFeatures.loadSVM_RankFea_vote(
//				feaNub,new File(trainfile));
//		System.out.print("initlrv......");	printTime();
//		LRclassifiation_vote lrv = new LRclassifiation_vote(feaNub, alpha, b);
//		System.out.print("train......");
//		System.out.print(trainfeaList.size()+"...");	printTime();
//		lrv.train(itea, trainfeaList);
//		lrv.writeWeight(modelPath);


		FeatureLoader floader1 = new FeatureLoader(true, false, feaNub, FeatureLoader.lableClass.C3);
		FeatureLoader testloader = new FeatureLoader(true, true, feaNub, FeatureLoader.lableClass.C3);


		LRclassifiation_vote lrv = new LRclassifiation_vote(modelPath);
		System.out.print("trainclassify......");
		printTime();

		System.out.print("testclassify......");	printTime();
		File fss[] = new File(trainAll).listFiles();
		for (File f : fss) {
			ArrayList<Vector> listtest = floader1.loadFea(f.getPath());

			ArrayList<Double> re = lrv.classify(listtest);
			writeFile.writeResult(re, trainrankPre + f.getName());
		}

		for (double scoreLow = 0.99; scoreLow < 1.0; scoreLow += 0.1) {
			System.out.print(scoreLow + "\t");
			readRankResultForDetect_v2.scoreLow = scoreLow;
			String re = readRankResultForDetect_v2.readEval(base, trainAll, trainrankPre, rankTrainlongidre);
			EvalEasy.evalResultByOracle(re);
		}


		System.out.println("*********test*****************************************************************************************************");


		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			ArrayList<Vector> listtest = testloader.loadFea(f.getPath());
			//System.out.println(listtest.get(0));

			ArrayList<Double> ree = lrv.classify(listtest);
			writeFile.writeResult(ree, rankPre + f.getName());
		}

		double scoreLow = 0.9955;
		System.out.print(scoreLow + "\t");
		readRankResultForDetect_v2.scoreLow = scoreLow;
		String ree = readRankResultForDetect_v2.readEval(base, testFile, rankPre, ranklongidre);
		EvalEasy.evalResultByOracle(ree);

		System.out.print("end.");
		printTime();

	}
/*****************分主函数*************************************************************************	******************************************************/

}
///**
// * 特殊的序对构建方法
// * @param feanub
// * @param f
// * @return
// * @throws IOException
// */
//public static ArrayList<Vector_vote> loadDefineFeature(int feanub,File f)throws IOException{
//	ArrayList<Vector_vote> all = new ArrayList<>();
//	BufferedReader br = new BufferedReader(new FileReader(f));
//	String line = "";
//	while((line = br.readLine())!=null){
//		String ss[] = line.split(" ");
//		double feas[] = new double[feanub];
//		for (int i = 0; i < feanub; i++) {
//			feas[i]=Double.parseDouble(ss[i+1].split(":")[1]);
//		}
//		//System.out.println(ss[0].substring(1));
//		String asa = getsvmlable(ss[0]);
//		int vote = Integer.parseInt(ss[ss.length-1]);
//		//System.out.println(vote);
//		Vector_vote v = new Vector_vote(vote,feas,asa);
//		all.add(v);
//	}
//	br.close();
//	System.out.println("compare......");
//	return getpairs(all);
//}
//
//private static ArrayList<Vector_vote> getpairs(ArrayList<Vector_vote> list){
//	ArrayList<Vector_vote> newlist = new ArrayList<>();
////	for (int i = 0; i < list.size() ; i++) {
////		Vector_vote v1 = list.get(i);
////		for (int j = 0; j < i; j++) {
////			Vector_vote v2 = list.get(j);
////			newlist.addAll(v1.compare(v2));
////		}
////	}
//	while(list.size()>0){
//		System.out.println(list.size()+"...less...");
//		Vector_vote v1 = list.get(0);
//		list.remove(0);
//		for (Vector_vote v2:list) {
//			newlist.addAll(v1.compare(v2));
//		}
//	}
//	return newlist;
//}
//
//private static void getpairs2(ArrayList<Vector_vote> list) throws IOException{
//	String file="";
//	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//	while(list.size()>0){
//		System.out.println(list.size()+"...less...");
//		Vector_vote v1 = list.get(0);
//		list.remove(0);
//		for (Vector_vote v2:list) {
//			ArrayList<Vector_vote> nl = (v1.compare(v2));
//			for (Vector_vote v3:nl) {
//				bw.write(v3.getLable());
//				double feas[] = v3.getFeatures();
//				for (double fea:feas) {
//					bw.write(" "+fea);
//				}
//				bw.write(" "+v3.getVote());
//			}
//			
//		}
//	}
//	bw.flush();bw.close();
//}
//
//private static String getsvmlable(String s){
//	if(s.startsWith("-")){
//		return "0";
//	}else{
//		return "1";
//	}
//}