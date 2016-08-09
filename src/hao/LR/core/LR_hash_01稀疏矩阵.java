package hao.LR.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/** 
 * 该版本仅用于处理只有1和0的稀疏矩阵
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月12日 下午4:00:34 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class LR_hash_01稀疏矩阵 {
	
	private final String VERSION="LR_hash_V1.0";
	private double alpha;
	private int feaNub;//特征数量
	private boolean b=false;//分类面标识，默认无分类面
	private HashMap<String,Double> feaMap;
	private HashMap<String,Double> weightMap;
	
	public LR_hash_01稀疏矩阵(double alpha, int feaNub, boolean b) {
		//super();
		this.alpha = alpha;
		this.feaNub = feaNub;
		this.b = b;
		this.weightMap =new HashMap<>();
		for (int i = 1; i <= feaNub; i++) {
			this.weightMap.put(""+i, 1.0);
		}
		this.weightMap.put("#", 1.0);//分类面		
		this.feaMap=(HashMap<String, Double>) weightMap.clone();
	}

	@Override
	public String toString() {
		return "LRlearner [VERSION=" + VERSION + ", alpha=" + alpha + ", feaNub=" + feaNub + ", b=" + b
				+ ", weightMap=" + weightMap + "]";
	}

	public void train(int itea,ArrayList<Feature> features) {
		int lineNub = features.size();
		for (int i = 0; i < itea ; i++) {
			for (String word:feaMap.keySet()) {
				double Allwrong=0.0;
			//for (int j = 0; j < lineNub ; j++) {
				for (Feature oneFea:features) {
					String lable =oneFea.getLable();
					ArrayList<String> wordList = oneFea.getWordList();

					if(b){wordList.add("#");}//是否加入分类面
					System.out.println(wordList);
					if(wordList.contains(word)){
						double prediction1=classify(wordList);
						double wrong =( prediction1- Double.parseDouble(lable) );
			//			System.out.print(word+"\t");System.out.println(feaMap.get(word));
						Allwrong+=(wrong*feaMap.get(word));
					}
					if(b){wordList.remove("#");}//是否加入分类面
				}
				double newWeight = weightMap.get(word) - (alpha*(Allwrong/(lineNub+0.0)));
				//	System.out.println(newWeight);
				weightMap.put(word, newWeight);
			}
		}
	}

	/**
	 * 正例的概率
	 * @param z
	 * @return
	 */
	private double sigmoid(double z) {
		return 1.0/ (1.0 + Math.exp(-z));//1
	}

	/**
	 * 先求和后求概率
	 * @param wordList 特征数组
	 * @return
	 */
	private double classify(ArrayList<String> wordList) {
		double logit=0.0;
		for (String word:wordList) {
			double x = feaMap.get(word);
			double w = weightMap.get(word);
			logit += (x*w) ;
			//System.out.println(logit);
		}
		return sigmoid(logit);
	}

	/**
	 * 用于test
	 * @param Features
	 * @return
	 */
	public ArrayList<Double> classifyTest(ArrayList<Feature> Features) {
		ArrayList<Double> re = new ArrayList<Double>();
		for (Feature v:Features) {
			double score = classify(v.getWordList());
			re.add(score);
		}
		return re;
	}

	/**
	 * 1 1:x 2:x 3:x
	 * @param feaNub
	 * @param path
	 * @return
	 */
	public static ArrayList<Feature> loadFea(int feaNub,String path){
		ArrayList<Feature> re = new ArrayList<Feature>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String ss[] = line.split(" ");
				String lable = ss[0];
				ArrayList<String> wordList = new ArrayList<>();
				for (int i = 1; i <= feaNub; i++) {
					String x = ss[i].split(":")[0];
					String xx = ss[i].split(":")[1];
					if(xx.equals("1.0")){
						wordList.add(x);
					}
				}
				Feature fea = new Feature(lable, wordList);
				re.add(fea);
			}
			br.close();
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	public static void main(String[] args) {
		double alpha=0.1;
	//	int feaNub=29573;//特征数量
		int feaNub=3;//特征数量
		boolean b=false;//分类面标识，默认无分类面
		int itea = 100;

		String trainPath="/home/hao/桌面/lr测试数据/train.txt";
		//String trainPath="/home/hao/桌面/lr测试数据/train.txt";
		ArrayList<Feature> trainList = loadFea(feaNub,trainPath);

		LR_hash_01稀疏矩阵 lrl = new LR_hash_01稀疏矩阵(alpha, feaNub, b);
		System.out.println(trainList);
		lrl.train(itea, trainList);
		System.out.println("weight"+lrl.weightMap);
		System.out.println(lrl.classifyTest(trainList));
	}
}

class Feature {

	private String lable="";
	private ArrayList<String> wordList;

	public Feature(String lable, ArrayList<String> wordList) {
		//super();
		this.lable = lable;
		this.wordList = wordList;
	}

	@Override
	public String toString() {
		return "Feature [lable=" + lable + ", wordList=" + wordList + "]";
	}

	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public ArrayList<String> getWordList() {
		return wordList;
	}
	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}

}