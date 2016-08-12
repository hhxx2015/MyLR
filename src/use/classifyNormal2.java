package use;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import l2r4sr2016.eval.EvalEasy;
import tools.io.FeatureLoader;
import tools.writeFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * @author hao : 1347261894@qq.com
 * @version 1.0
 * @date 创建时间：2016年6月11日 下午4:50:32
 * @parameter
 * @return
 */
public class classifyNormal2 {
    static void printTime() {
        //Long d = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
        System.out.println(sdf.format(new Date()));
    }

    public static void main(String[] args) throws IOException {
        double alpha = 0.1;
        int itea = 10000;
        int b = 1;

        int feaNub = 20;
        String base = "normal20";//normal20 willams_10vote
        String vote = "";//_vote _votelable2

        String trainfile = "/home/hao/桌面/mylr/old/train/" + base + ".txt";
        String testFile = "/home/hao/桌面/mylr/old/test/" + base + "/";
        String modelPath = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/model/" + base + "_" + alpha + "_" + itea + vote + ".txt";
        String rankPre = "/home/hao/桌面/mylr/old/vote/classify/" + base + vote + "/";//willams20_vote willams_logvote

        String trainAll = "/home/hao/桌面/mylr/old/train/" + base + "/";
        String trainPrePath = "/home/hao/桌面/mylr/old/votetrain/classify/" + base + vote + "/";


        new File(rankPre).mkdirs();
        new File(trainPrePath).mkdirs();
        String ranklongidre = "/home/hao/桌面/mylr/old/longidre/classify/" + base + "//";//willams20_vote willams_logvote
        String rankTrainlongidre = "/home/hao/桌面/mylr/old/trainlongidre/classify/" + base + "//";//willams20_vote willams_logvote

        new File(ranklongidre).mkdirs();
        new File(rankTrainlongidre).mkdirs();
        System.out.println(base + "");
/**/
//		System.out.print("loadfeatures......");printTime();
//		FeatureLoader fl = new FeatureLoader(true,true,feaNub, FeatureLoader.lableClass.C3);
//		ArrayList<Vector> trainfeaList = fl.loadFea(trainfile);
//
//		System.out.print("initlrv......");printTime();
//		LRclassifiation lrc = new LRclassifiation(feaNub, alpha, b);
//
//		System.out.print("train......");
//		System.out.print(trainfeaList.size()+"...");printTime();
//		lrc.train(itea, trainfeaList);
//		lrc.writeWeight(modelPath);


        LRclassifiation lrc = new LRclassifiation(modelPath);

        FeatureLoader testloader = new FeatureLoader(true, false, feaNub, FeatureLoader.lableClass.C3);
        System.out.print("testclassify......");
        printTime();

//		File fs[] = new File(trainAll).listFiles();
//		for (File f : fs) {
//			ArrayList<Vector> listtest = testloader.loadFea(f.getPath());
//			//System.out.println(listtest.get(0).toString());
//
//			ArrayList<Double> re =lrc.classify(listtest);
//			writeFile.writeResult(re, trainPrePath+f.getName());
//
//			HashSet <String> set = new HashSet<>();
//			for (int i = 0 ;i<re.size();i++){
//				Double sc = re.get(i);
//				if(sc>=0.5){
//					set.add(listtest.get(i).getLongid());
//				}
//			}
//			writeFile.writeResult(set, rankTrainlongidre + f.getName());
//
//		}
//
//		System.out.print("end.");
//		printTime();
//
//		EvalEasy.evalResultByOracle(rankTrainlongidre);

        FeatureLoader testloader1 = new FeatureLoader(true, true, feaNub, FeatureLoader.lableClass.C3);
        File fs[] = new File(testFile).listFiles();
        for (File f : fs) {
            ArrayList<Vector> listtest = testloader1.loadFea(f.getPath());

            ArrayList<Double> re = lrc.classify(listtest);
            writeFile.writeResult(re, rankPre + f.getName());

            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < re.size(); i++) {
                Double sc = re.get(i);
                if (sc >= 0.5) {
                    set.add(listtest.get(i).getLongid());
                }
            }
            writeFile.writeResult(set, ranklongidre + f.getName());

        }
        System.out.print("end.");
        printTime();

        EvalEasy.evalResultByOracle(ranklongidre);
    }
}
