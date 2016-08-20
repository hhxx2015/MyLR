//package test;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashSet;
//import hao.LR.core.LRclassifiation;
//import hao.LR.entity.FeaMap;
//import hao.LR.entity.Feature;
//import hao.LR.entity.Features;
//import hao.LR.entity.Vector;
//import hao.LR.util.io.LoadFeatures;
//import hao.LR.util.io.iotools.writeFile;
//
//
///**
//* @author  hao : 1347261894@qq.com
//* @date 创建时间：2016年6月11日 下午1:15:04
//* @version 1.0
//* @parameter
//* @since
//* @return
//*/
//public class classifyTest {
//	public static void main(String[] args) throws IOException {
//		//int feaNub=3;//46
////		int itea = 500;
////		double alpha=0.1;
////		boolean b=true;//是否加入分类面
////
////		String trainPath="/home/hao/桌面/lrtest/train";
////		String testPath="/home/hao/桌面/lrtest/test";
////		String testPre1 = "/home/hao/桌面/lrtest/pre";
////
////		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
////
////
////		lrc.train(itea, listTrain);
////		System.out.println(lrc.toString());
////
////		writeFile.writeResult(lrc.classify(listTest), testPre1);
////		System.out.println( lrc.toString());
//	}
//
//
//	public static void loadFeature(String path) throws Exception {
//		//model1 "c1_c2"
//		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
//		String line ="";
//		Features features = new Features();
//		while((line = br.readLine())!=null){
//			String []ls = line.split(" ");
//			FeaMap fm = new FeaMap();
//			for (int i =1;i<ls.length;i++){
//				fm.put(i,Double.parseDouble(ls[i]));
//			}
//			Feature fea = new Feature(ls[0],fm);
//			features.appendFeature(ls[0],fea);
//		}
//
//		String lablearr[] = features.getLables();
//		for(int i = 0 ; i< lablearr.length ; i++){
//			for(int j = i ; j < lablearr.length ; j++){
//
//			}
//		}
//
//
//
//
//
//	}
//}
