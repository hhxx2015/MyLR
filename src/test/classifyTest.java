package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;
import tools.io.FeatureLoader;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月11日 下午1:15:04 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class classifyTest {
	public static void main(String[] args) throws IOException {
		int feaNub=3;//46
		int itea = 500;
		double alpha=0.1;
		boolean b=true;//是否加入分类面
		
		String trainPath="/home/hao/桌面/lrtest/train";
		String testPath="/home/hao/桌面/lrtest/test";
		String testPre1 = "/home/hao/桌面/lrtest/pre";
		
		ArrayList<Vector> listTrain = LoadFeatures.loadSVM_RankFea(feaNub,new File(trainPath));
		FeatureLoader trainloader = new FeatureLoader(true,feaNub, FeatureLoader.lableClass.RANK);
		ArrayList<Vector> listTrain2 = trainloader.loadFea(trainPath);
		System.out.println(listTrain.size());
		System.out.println(listTrain2.size());

//		ArrayList<Vector> listTest= LoadFeatures.loadDefineFeature(feaNub,new File(testPath));
//
		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
//		lrc.train(itea, listTrain);
//		System.out.println(lrc.toString());
//
//		writeFile.writeResult(lrc.classify(listTest), testPre1);
		//System.out.println( lrc2.toString());
	}
}
