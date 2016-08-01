package otherVertion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import tools.FileRead;

public class LogisticRegressionClassify {
	//private HashMap<String,Double> weightMap = new HashMap<>();
	private TreeMap<Integer,Double> weightMap = new TreeMap<>();
	
	public LogisticRegressionClassify(){

	}
	
	/**
	 * 预测一行
	 * @param y该行的标签，正例还是负例0/1
	 * @param featureList特征集合 <特征号，特征值>
	 */
	public  Double logisticRegressionClassify(String line) {
		//权重map<标签，权重值>
		//特征集合List<Map<标签，特征值>>
		//ArrayList<HashMap<String,Double>> featureList = new ArrayList<>();
		//int y = 0;
		ArrayList<HashMap<String,Double>> featureList = loadFeature(line);
		
		double score = score(weightMap,featureList);
		return score;
	}
	
	public TreeMap<Integer, Double> getWeightMap() {
		return weightMap;
	}

	public  double score(TreeMap<Integer,Double> weightMap,ArrayList<HashMap<String,Double>> featureList) {
		double LR_score=0.0;
		//HashMap<String,Double> weightMap = new HashMap<>();
		//ArrayList<HashMap<String,Double>> featureList= new ArrayList<>();
		for (HashMap<String,Double> feature:featureList) {
			//System.out.println(feature);
			// System.out.println(weightMap);
			for(String lable:feature.keySet()){	
				if(weightMap.containsKey(Integer.parseInt(lable))){
					double ws=weightMap.get(Integer.parseInt(lable));
					double fs = feature.get(lable);
					
					//System.out.println("ws"+ws);
					//System.out.println("fs"+fs);
					LR_score += (ws*fs);
				}
			}
		}
		//return Math.exp(LR_score) / (1 + Math.exp(LR_score));
	//	System.out.println(LR_score);
	//	System.out.println("--------------------------------------"+ Math.exp(LR_score));
		return Math.exp(LR_score) / (1 + Math.exp(LR_score));
	}

	public  ArrayList<HashMap<String,Double>> loadFeature(String line) {
		//标签 特征。。。#
		String featureArr[]=line.split(" ");
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

	public void loadModel(String modelPath) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(modelPath)));
		String line="";
		int i=0;
		while((line= br.readLine())!=null){
			weightMap.put(i++, Double.parseDouble(line));
		}
		br.close();
	}
	
	public static void main(String[] args) throws IOException {
		//featureQueryString featureAll featureNeighbor featurePos
		String s="featurePos";
		String testPATH="/home/hao/桌面/分类任务/data/noQID/test/"+s+"/";
		String modelPATH="/home/hao/文档/mylr/model/"+s+"/model.txt";
		File fs[]= new File(testPATH).listFiles();
		for (File f:fs) {
			String prePATH="/home/hao/文档/mylr/pre/"+s+"/";
			String longidPATH="/home/hao/文档/mylr/longid/"+s+"/";
			new File(prePATH).mkdirs();
			new File(longidPATH).mkdirs();
			classify(prePATH+f.getName(),f.getPath(),modelPATH,longidPATH+f.getName());
		}
	}
	
	public static void classify(String prePath,String testPath,String modelPath,String longidPATH) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(prePath)));
		BufferedWriter bwlongid = new BufferedWriter(new FileWriter(new File(longidPATH)));
		
		ArrayList<String> lineList = new ArrayList<>();
		
		try {
			lineList = FileRead.FileReader(testPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LogisticRegressionClassify  lr = new LogisticRegressionClassify();
		lr.loadModel(modelPath);
		for (String line : lineList) {
			double score=lr.logisticRegressionClassify(line);
			bw.write(score+"");
			bw.newLine();
			bw.flush();
			//System.out.println(lr.getWeightMap());	
			if(score>=0.5){
				bwlongid.write(line.split(" ")[0]);
				bwlongid.newLine();
				bwlongid.flush();
			}
		}				
		bw.close();
		bwlongid.close();
//		System.out.println(lr.getWeightMap());	
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
