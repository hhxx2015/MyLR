package use;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;
import tools.writeFile;
import util.readLRreold;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月8日 下午6:11:16 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class classifyWilliams {
	public static void main(String[] args) throws IOException {
		int feaNub=20;
		double alpha=0.1;
		int itea = 500;

		int b = 1;
		//配置lr
		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
		
		String trainFile="../MyLR/Data/detect/feature/train/trainWilliams_classify.txt";
		ArrayList<Vector> listtrain = LoadFeatures.loadDefineFeature(feaNub,new File(trainFile));
		lrc.train(itea, listtrain);
		String testFile="../MyLR/Data/detect/feature/test/williams/normal/";
		String classifyPre="../MyLR/Data/detect/feature/classifyPre/test/";
		
		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			ArrayList<Vector> listtest = LoadFeatures.loadSvmFeature(feaNub,f);
			
			ArrayList<Double> re =lrc.classify(listtest);
			writeFile.writeResult(re, classifyPre+f.getName());
		}
		
		String longidrePath="../MyLR/Data/detect/feature/longidre/test/";
		readLRreold.get(testFile, classifyPre, longidrePath);
	}
}
//System.out.println(listtrain.get(6));
//lrc.train(itea, listtrain);
//
//writeFile.writeResult(lrc.classify(listtrain), "../MyLR/Data/pre.txt");