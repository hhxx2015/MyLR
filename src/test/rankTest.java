//package test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import hao.LR.core.LRclassifiation;
//import hao.LR.entity.Vector;
//import hao.LR.util.io.LoadFeatures;
//import hao.LR.util.io.iotools.writeFile;
//
//
//public class rankTest {
//	public static void main1(String[] args) throws IOException {
//		int feaNub=46;//46
//		int itea = 5;
//		double alpha=0.1;
//		int b=1;//是否加入分类面
//
//		String trainPath="../MyLR/Data/MQ2008/Fold1/train.txt";
//		String testPath="../MyLR/Data/MQ2008/Fold1/test.txt";
//		String modelPath = "./rankmodel.bin";
//		String testPre1 = "../MyLR/Data/tre/re1.txt";
//		String testPre2 = "../MyLR/Data/tre/re2.txt";
//
//		ArrayList<Vector> listTrain = LoadFeatures.loadSVM_RankFea(feaNub,new File(trainPath));
//		ArrayList<Vector> listTest= LoadFeatures.loadSVM_RankFea(feaNub,new File(testPath));
//
//		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
//		lrc.train(itea, listTrain);
//		lrc.save(modelPath);
//
//		LRclassifiation lrc2 = new LRclassifiation(modelPath);
//
//		//writeFile.writeResult(lrc.classify(listTest), testPre1);
//		//writeFile.writeResult(lrc.classify(listTest), testPre2);
//		//System.out.println( lrc2.toString());
//
//	}
//
//	public static void main(String[] args) throws IOException {
//
//		int feaNub=46;//46
//
//		int iter = 5;
//		double alpha=0.1;
//		int b=1;//是否加入分类面
//
//		//配置lr
//		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
//
//		for (int fold = 1; fold <= 5; fold++) {
//			// 加载特征
//			ArrayList<Vector> listtrain = LoadFeatures.loadSVM_RankFea(feaNub,new File("/home/hao/实验/MQ2008/Fold"+fold+"/train.txt"));
//
//			lrc.train(iter,listtrain);
//
//			/**
//			 * qid vector
//			 */
////			ArrayList<Vector> testList  = LoadFeatures.loadSVM_RankFeaToClassify(feaNub,new File(
////					"/home/hao/实验/MQ2008/Fold"+fold+"/test.txt"));
////			writeFile.writeResult(lrc.classify(testList),
////					"/home/hao/实验/lreval/Fold"+fold+"/test_re6.txt");
////
////			ArrayList<Vector> valiList  = LoadFeatures.loadSVM_RankFeaToClassify(feaNub,new File(
////					"/home/hao/实验/MQ2008/Fold"+fold+"/vali.txt"));
////			writeFile.writeResult(lrc.classify(valiList),
////					"/home/hao/实验/lreval/Fold"+fold+"/vali_re6.txt");
//
//		}
//	}
//}
