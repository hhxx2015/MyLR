package use;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import l2r4sr2016.eval.EvalEasy;
import tools.io.FeatureLoader;
import tools.writeFile;
import util.readRankResultForDetect_v2;

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
public class rankNormal {
    static void printTime() {
        //Long d = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
        System.out.println(sdf.format(new Date()));
    }

    public static void main(String[] args) throws IOException {
        double alpha = 0.1;
        int itea = 500;
        int b = 1;

        int feaNub = 20;

        String base = "normal20";//normal20 willams_10vote
        String cl = "_vote_rank";//_vote _votelable2

        //String trainfile="/home/hao/桌面/mylr/old/train/"+base+".txt";
        String trainfile = "/home/hao/桌面/mylr/old/train/" + "willams21_10vote.txt";//voterank  willams21_10vote.txt
        String testFile = "/home/hao/桌面/mylr/old/test/" + base + "/";
        String modelPath = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/model/" + base + "_" + alpha + "_" + itea + cl + ".txt";

        String trainAll = "/home/hao/桌面/mylr/old/train/" + base + "/";


        FeatureLoader trainloader = new FeatureLoader(true, false, feaNub, FeatureLoader.lableClass.RANK);
        base = base + "_" + alpha + "_" + itea + cl;
        String rankPre = "/home/hao/桌面/mylr/old/vote/rank/" + base + "/";//willams20_vote willams_logvote
        String trainPrePath = "/home/hao/桌面/mylr/old/votetrain/rank/" + base + "/";
        new File(rankPre).mkdirs();
        new File(trainPrePath).mkdirs();

        String testlongidre = "/home/hao/桌面/mylr/old/longidre/rank/" + base + "//";//willams20_vote willams_logvote
        String rankTrainlongidre = "/home/hao/桌面/mylr/old/trainlongidre/rank/" + base + "//";//willams20_vote willams_logvote

        new File(testlongidre).mkdirs();
        new File(rankTrainlongidre).mkdirs();


        System.out.println(base + "");


//		System.out.print("loadfeatures......");printTime();
//		ArrayList<Vector> trainfeaList = trainloader.loadFea(trainfile);
//		System.out.print("initlrv......");printTime();
//		LRclassifiation lrc = new LRclassifiation(feaNub, alpha, b);
//		System.out.print("train......");
//		System.out.print(trainfeaList.size()+"...");printTime();
//		lrc.train(itea, trainfeaList);
//		lrc.writeWeight(modelPath);


        LRclassifiation lrc = new LRclassifiation(modelPath);
        System.out.print("testclassify......");
        printTime();


        FeatureLoader testloader = new FeatureLoader(true, false, feaNub, FeatureLoader.lableClass.C3);
        File fs[] = new File(trainAll).listFiles();
        for (File f : fs) {
            ArrayList<Vector> listtest = testloader.loadFea(f.getPath());

            ArrayList<Double> re = lrc.classify(listtest);
            writeFile.writeResult(re, trainPrePath + f.getName());

        }
        for (double scoreLow = 0.1; scoreLow < 0.9; scoreLow += 0.1) {
            System.out.print(scoreLow + "\t");
            readRankResultForDetect_v2.scoreLow = scoreLow;
            String re = readRankResultForDetect_v2.readEval(base, trainAll, trainPrePath, rankTrainlongidre);
            EvalEasy.evalResultByOracle(re);
        }
        System.out.print("end.");
        printTime();

/*******test********************************************************************************************/

        System.out.println("--------------------------test------------------------------------------------------------------------------");
        File fss[] = new File(testFile).listFiles();
        FeatureLoader testloader1 = new FeatureLoader(true, true, feaNub, FeatureLoader.lableClass.C3);
        for (File f : fss) {
            ArrayList<Vector> listtest = testloader1.loadFea(f.getPath());
            ArrayList<Double> re = lrc.classify(listtest);
            writeFile.writeResult(re, rankPre + f.getName());
        }


        //for(double scoreLow = 0.990; scoreLow<0.999;scoreLow+=0.0001){
        double scoreLow = 0.027;
        System.out.print(scoreLow + "\t");
        readRankResultForDetect_v2.scoreLow = scoreLow;
        String re = readRankResultForDetect_v2.readEval(base, testFile, rankPre, testlongidre);
        EvalEasy.evalResultByOracle(re);
        //}
        System.out.print("end.");
        printTime();

    }
}
