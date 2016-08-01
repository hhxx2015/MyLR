package otherVertion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import tools.FileRead;

public class LogisticRegression1 {
	//private HashMap<String,Double> weightMap = new HashMap<>();
	private TreeMap<Integer,Double> weightMap = new TreeMap<>();
	private double alpha=0.01;
	private int feaNub=0;
	
	public LogisticRegression1(double alpha){
		this.alpha=alpha;
//		weightMap.put(key, value)
	}
	
	
	/**
	 * 训练一行，
	 * @param y该行的标签，正例还是负例0/1
	 * @param featureList特征集合 <特征号，特征值>
	 */
	public  void logisticRegression(String line) {
		//权重map<标签，权重值>
		//特征集合List<Map<标签，特征值>>
		//ArrayList<HashMap<String,Double>> featureList = new ArrayList<>();
		//int y = 0;
		String y=line.split(" ")[0];
		ArrayList<HashMap<String,Double>> featureList = loadFeature(line);
		
		double Z =0;
		int wrongWNub=0;
		
		double score = score(weightMap,featureList);
		//System.out.println(score);
		
		if(score>0.5||y.equals("0")){
			Z +=(score-0.0);
			wrongWNub++;
		}else if(score<=0.5||y.equals("1")){
			Z +=(score-1.0);
			wrongWNub++;
		}
		
		double subZ=alpha*Z/wrongWNub;
		
		for (Integer lable:weightMap.keySet()) {
			double w = weightMap.get(lable);		
			weightMap.put(lable, w-subZ);
		}
	}
	
	public TreeMap<Integer, Double> getWeightMap() {
		return weightMap;
	}

	public  double score(TreeMap<Integer,Double> weightMap,ArrayList<HashMap<String,Double>> featureList) {
		double LR_score=0.0;
		//HashMap<String,Double> weightMap = new HashMap<>();
		//ArrayList<HashMap<String,Double>> featureList= new ArrayList<>();
		//HashMap<String,Double> weightMap = new HashMap<>();
		for (HashMap<String,Double> feature:featureList) {
			//System.out.println(feature);
			//System.out.println(weightMap);
			
			for(String lable:feature.keySet()){	
				double ws = 1.0;
				if(weightMap.containsKey(Integer.parseInt(lable))){
					weightMap.get(Integer.parseInt(lable));
				}else{
					weightMap.put(Integer.parseInt(lable), 1.0);
				}
				double fs = feature.get(lable);
				
		//		System.out.println("ws"+ws);
		//		System.out.println("fs"+fs);
				LR_score+=(ws*fs);
			}
		}
		//return Math.exp(LR_score) / (1 + Math.exp(LR_score));
	//	System.out.println( Math.exp(LR_score));
		return 1 / (1 + Math.exp(LR_score));
	}

	public  ArrayList<HashMap<String,Double>> loadFeature(String line) {
		//标签 特征。。。#
		String featureArr[]=line.split(" ");
		this.feaNub = featureArr.length-2;
		ArrayList<HashMap<String,Double>> feaList = new ArrayList<>();
		//权重模型初始化
		//		LogisticRegression();
		for (int i = 1; i < featureArr.length-1; i++) {
			HashMap<String,Double> m = new HashMap<String,Double>();
			double fea  = Double.parseDouble(featureArr[i]);
			m.put(i-1+"", fea);
			feaList.add(m);
		}
		return feaList;
	}

	public static void main(String[] args) {
		ArrayList<String> lineList = new ArrayList<>();
		
		try {
			lineList = FileRead.FileReader("/home/hao/桌面/featureAll.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int itea = 10;
		double alpha=0.01;
	

		LogisticRegression1  lr = new LogisticRegression1(alpha);
		
		for (int i = 0; i < itea; i++) {
			for (String line : lineList) {
				lr.logisticRegression(line);		
				//System.out.println(lr.getWeightMap());	
			}				
		}
		
		System.out.println(lr.getWeightMap());	
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
