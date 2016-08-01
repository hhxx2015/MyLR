package otherVertion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import tools.FileRead;


public class LogisticRegression {
	//private HashMap<String,Double> weightMap = new HashMap<>();
	//private TreeMap<Integer,Double> weightMap = new TreeMap<>();
	private ArrayList<Double> weightList = new ArrayList<>();
	private double alpha;

	public LogisticRegression(double alpha,ArrayList<Double> weightList ){
		this.alpha=alpha;
		this.weightList=weightList;
//		weightMap.put(key, value)
	}
	
	/**
	 * 训练一行，
	 * @param y该行的标签，正例还是负例0/1
	 * @param featureList特征集合
	 */
	public  void logisticRegression(String line) {
		//权重map<标签，权重值>
		//特征集合List<Map<标签，特征值>>
		//ArrayList<HashMap<String,Double>> featureList = new ArrayList<>();
		//int y = 0;
		double Z=0.0;
		String y=line.split(" ")[0];
		ArrayList<Double> feaList = loadFeature(line);
		//System.out.println(y);
		//System.out.println(feaList);
		ArrayList<Integer> errList = new ArrayList<>();
		for (int i = 0; i < feaList.size(); i++) {
			double score=score(feaList.get(i),weightList.get(i));
			//System.out.println(score);
			if(score>0.5&&y.equals("0")){
			//负例预测为正例
				//System.out.println(score-Double.parseDouble(y));
				Z-=(score-Double.parseDouble(y));
				errList.add(i);//记录错误的特征
			}
			if(score<=0.5&&y.equals("1")){
			//正例预测为负例	
				//System.out.println(score-Double.parseDouble(y));
				Z-=(score-Double.parseDouble(y));
				errList.add(i);//记录错误的特征
			}
		}
		//System.out.println(Z);
		//System.out.println(errList);
		double Zall = Z/(0.0+errList.size());
		//System.out.println(weightList);
		for (int i :errList) {//按序更新weightList
			double weight = weightList.get(i);
			weightList.remove(i);
//			weightList.add(i, weight-(Zall*alpha)); ;
			weightList.add(i, weight-(Zall*alpha*feaList.get(i))); ;
		}
		
	}

	public  double score(double feature,double weight) {
		double LR_score;
		LR_score = feature*weight;
		return Math.exp(LR_score) / (1 + Math.exp(LR_score));
	}

	/**
	 * 加载一行特征
	 * @param line
	 * @return
	 */
	public  ArrayList<Double> loadFeature(String line) {
		//标签 特征。。。#
		String featureArr[]=line.split(" ");

		ArrayList<Double> feaList = new ArrayList<>();
		//权重模型初始化
		//LogisticRegression();
		for (int i = 1; i < featureArr.length-1; i++) {
			double fea  = Double.parseDouble(featureArr[i]);
			feaList.add(fea);
		}
		return feaList;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> lineList = new ArrayList<>();
		//featureQueryString featureAll featureNeighbor featurePos
		String s="featurePos";
		try {
			lineList = FileRead.FileReader("/home/hao/桌面/分类任务/data/noQID/train/"+s+".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int feaNub=11;
		int itea = 2000;
		double alpha=0.0000001;
		ArrayList<Double> weightList = new ArrayList<>();
		for (int i = 0; i < feaNub; i++) {
			weightList.add(0.0);		
		}
		LogisticRegression  lr = new LogisticRegression(alpha,weightList);
		
		for (int i = 0; i < itea; i++) {
			for (String line : lineList) {
				lr.logisticRegression(line);		
			}				
		}
		new File("/home/hao/文档/mylr/model/"+s).mkdirs();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/home/hao/文档/mylr/model/"+s+"/model.txt")));
		for (Double l:weightList) {
			bw.write(l+"");
			bw.newLine();
			bw.flush();
		}
		bw.close();
		System.out.println(lr.getWeightList());	
	}
	
	public ArrayList<Double> getWeightList() {
		return weightList;
	}


	public static ArrayList<String> readFile(String idFile) throws IOException{
		ArrayList<String> idList = new ArrayList<String>();		
		BufferedReader br = new BufferedReader( new FileReader(idFile));		
		String s = "";		
		while((s=br.readLine())!=null){ 			
			if(s.startsWith("#")){
				
			}else{
				idList.add(s);				
			}
		}
		br.close();
		return idList;
	}
	
	
}
