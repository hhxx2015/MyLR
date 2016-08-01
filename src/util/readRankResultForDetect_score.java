package util;

import l2r4sr2016.iotools.writeFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.*;

public class readRankResultForDetect_score {

	
	public static TreeMap<Integer,ArrayList<Integer>> svm_Rank_Re(
			TreeMap<Integer,ArrayList<String>>  qid_scoreList, String preResultPath ) throws IOException{
		ArrayList<Integer> scoreListSize = new ArrayList<Integer>();
		ArrayList<Integer> qidList = new ArrayList<Integer>();
		for (int qid :qid_scoreList.keySet()) {
			qidList.add(qid);
			int size= qid_scoreList.get(qid).size();
			scoreListSize.add(size);
		}
		TreeMap<Integer,ArrayList<Integer>> re = getPreResult(qidList,scoreListSize,preResultPath);
		
		return re;
	}
	
	/** 得到各个pre结果的topall
	 * @param preResultPath
	 * @return
	 * @throws IOException
	 */
	public static TreeMap<Integer, ArrayList<Integer>> getPreResult(ArrayList<Integer>qidList
			,ArrayList<Integer>scoreListSizes,String preResultPath) throws IOException{
		//System.out.println("qid:"+qidList);
		//System.out.println("size:"+scoreListSizes);
		TreeMap<Integer, ArrayList<Integer>> re =new TreeMap<Integer, ArrayList<Integer>>();		
		BufferedReader br = new BufferedReader(new FileReader(new File(preResultPath)));
		int a = scoreListSizes.size();
		for (int i =0;i<a;i++) {
			ArrayList<Double> preList = new ArrayList<Double>();
			int qid  = qidList.get(i);
			int size = scoreListSizes.get(i);
			int te =0;
			while(te<size){
				preList.add(Double.parseDouble(br.readLine()));
				te++;
			}
			re.put(qid, reRank(preList));
		}
		br.close();
		return re;
	}

	
//舍弃的rerank方法	
//	public static ArrayList<Integer> reRank(ArrayList<Double> preList){
//		TreeMap<Double, Integer> tm = new TreeMap<Double, Integer>();
//		ArrayList<Integer> re = new ArrayList<Integer>();
//		int preSize = preList.size();
//		for (int i = 0; i < preSize; i++) {
//			double onePre = preList.get(i);
//			tm.put(onePre, i);
//		}
//		Collection<Integer> c = tm.values();
//		Iterator<Integer> ite=c.iterator();
//		while(ite.hasNext()){
//			re.add(ite.next());
//		}
//		return re;
//	}
	
	/**
	 * 标准的重排序方法对list的value排序，返回 List<Entry<key,value>>前边是排名后边是值
	 * @param preList
	 * @return
	 */
	public static ArrayList<Integer> reRank(ArrayList<Double> preList){
		TreeMap<Integer, Double> tm = new TreeMap<Integer, Double>();
		
		ArrayList<Integer> re = new ArrayList<Integer>();
		int preSize = preList.size();
		for (int i = 0; i < preSize; i++) {
			double onePre = preList.get(i);
			if(onePre>scoreLow){
				tm.put(i, onePre);
			}else{
				tm.put(-1, onePre);
			}
		}
		
		List<Entry<Integer, Double>> al = new ArrayList(tm.entrySet()); 
		Collections.sort(al, new Comparator(){
			public int compare(Object o1, Object o2){
				Map.Entry obj1 = (Map.Entry) o1;  
	        	Map.Entry obj2 = (Map.Entry) o2;  
	          	return ((Double) obj2.getValue()).compareTo((Double)obj1.getValue());      
			}
		});

		for (Entry<Integer, Double> i : al) {
			re.add(i.getKey());
		}
		return re;
	}
	
	static Double scoreLow  = -1000.0;
	public static void main(String[] args) throws IOException {
		//for (double j = 0.99; j < 1; j+=0.001) {
		//	scoreLow=j;
			//System.out.println(j);
			//for (int i = 50; i < 500; i+=10) {
				main1(1);
			//}
		//}
		
	}
	
	/**
	 * 排序结果写成trec结果
	 * @param
	 * @throws IOException
	 */
	public static void main1(int topscore) throws IOException {
		String base = "normal20";//willams_10vote normal20
		String test="/home/hao/桌面/mylr/test/"+base+"/";
		String prePath="/home/hao/桌面/mylr/vote/"+base+"/";
		String longidrePath="/home/hao/桌面/mylr/longid/"+base+"/top"+topscore+"_score"+scoreLow+"/";

		System.out.println(longidrePath);
		HashSet <String> reset = new HashSet <String>();
		new File(prePath).mkdirs();
		new File(longidrePath).mkdirs();
		File fs[] = new File(test).listFiles();
		for (File f : fs) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			//BufferedWriter bw = new BufferedWriter(new FileWriter(
			// new File(longidrePath+f.getName())));

			String line ="";
			TreeMap<Integer,ArrayList<String>> qid_scoreList=new TreeMap<>();
			while((line = br.readLine())!=null){
	
				//String qid = line.split(" ")[1].replace("qid:", "");
				//int iqid= Integer.parseInt(qid);
				int iqid= 1;
				if(qid_scoreList.containsKey(iqid)){
					ArrayList<String> nublist=qid_scoreList.get(iqid);
					nublist.add(line);
					qid_scoreList.put(iqid, nublist);
				}else{
					ArrayList<String> nublist=new ArrayList<>();
					nublist.add(line);
					qid_scoreList.put(iqid, nublist);
				}
			}
			br.close();
			//重新排序后的list
			TreeMap<Integer,ArrayList<Integer>> rankre = svm_Rank_Re(qid_scoreList, prePath+f.getName());
			
			for (int qidn :qid_scoreList.keySet()) {
				ArrayList<Integer> rankrelist = rankre.get(qidn);
				for (int i = 0; i < Math.min(topscore, rankrelist.size()); i++) {
					if(rankrelist.get(i)==-1){
		//				System.out.println("-1");
					}else{
						String reline =qid_scoreList.get(qidn).get( rankrelist.get(i));
						
						String longid = reline.split("#")[1];
						longid=longid.split(" ")[0];
						reset.add(longid);

						//bw.write(longid);
						//bw.flush();
						//bw.newLine();
					}
					//	System.out.println(sb.toString());
				}
			}
			//bw.close();
			writeFile.writeResult(reset,longidrePath+f.getName());
		}
		
	}
}