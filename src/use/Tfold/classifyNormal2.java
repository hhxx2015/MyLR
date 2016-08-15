package use.Tfold;


import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import l2r4sr2016.eval.EvalEasy;
import tools.io.FeatureLoader;
import tools.writeFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;

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
        int itea = 500;
        int b = 1;
        int feaNub = 20;
        String base = "normal20";//normal20 willams21
        String vote = "";//_vote _votelable2
        String ppath = "/home/hao/IdeaProjects/MyLR/src/use/Tfold/normalClassifyPath.properties";


        FeatureLoader fl = new FeatureLoader(true, true, feaNub, FeatureLoader.lableClass.C3);
        FeatureLoader trainloader = new FeatureLoader(true, true, feaNub, FeatureLoader.lableClass.C3);
        FeatureLoader testloader = new FeatureLoader(true, false, feaNub, FeatureLoader.lableClass.C3);


        InputStreamReader in = new InputStreamReader(new FileInputStream(ppath), "utf-8");
        Properties props = new Properties();
        props.load(in);


        String trainfile = props.getProperty("trainfile") + base + ".txt";
        String trainAll = props.getProperty("trainAll") + base + "/";
        String testFile = props.getProperty("testFile") + base + "/";
        String modelPath = props.getProperty("modelPath") + base + "_" + alpha + "_" + itea + vote + ".txt";
        String Pre = props.getProperty("rankPre") + base + vote + "/";//willams20_vote willams_logvote
        String trainPrePath = props.getProperty("trainPrePath") + base + vote + "/";
        String longidre = props.getProperty("ranklongidre") + base + "//";//willams20_vote willams_logvote
        String Trainlongidre = props.getProperty("rankTrainlongidre") + base + "//";//willams20_vote willams_logvote
        new File(Pre).mkdirs();
        new File(trainPrePath).mkdirs();
        new File(longidre).mkdirs();
        new File(Trainlongidre).mkdirs();


        System.out.println(base + "");
/************************************************************************************************/

        System.out.print("loadfeatures......");
        printTime();
        ArrayList<Vector> trainfeaList = fl.loadFea(trainfile);
        System.out.print("initlrv......");
        printTime();
        LRclassifiation lrc = new LRclassifiation(feaNub, alpha, b);
        System.out.print("train......");
        System.out.print(trainfeaList.size() + "...");
        printTime();

        lrc.train(itea, trainfeaList);
        lrc.writeWeight(modelPath);

/************************************************************************************************/
        //LRclassifiation lrc = new LRclassifiation(modelPath);

        System.out.print("testclassify......");
        printTime();

        File fs[] = new File(trainAll).listFiles();
        for (File f : fs) {
            ArrayList<Vector> listtest = trainloader.loadFea(f.getPath());
            // System.out.println(listtest.get(0).toString());

            ArrayList<Double> re = lrc.classify(listtest);
            writeFile.writeResult(re, trainPrePath + f.getName());

            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < re.size(); i++) {
                Double sc = re.get(i);
                if (sc >= 0.5) {
                    set.add(listtest.get(i).getLongid());
                }
            }
            writeFile.writeResult(set, Trainlongidre + f.getName());

        }

        System.out.print("end.");
        printTime();
        EvalEasy.evalResultByOracle(Trainlongidre);


/************************************************************************************************/


//        File fss[] = new File(testFile).listFiles();
//        for (File f : fss) {
//            ArrayList<Vector> listtest = testloader.loadFea(f.getPath());
//
//            ArrayList<Double> re = lrc.classify(listtest);
//            writeFile.writeResult(re, rankPre + f.getName());
//
//            HashSet<String> set = new HashSet<>();
//            for (int i = 0; i < re.size(); i++) {
//                Double sc = re.get(i);
//                if (sc >= 0.5) {
//                    set.add(listtest.get(i).getLongid());
//                }
//            }
//            writeFile.writeResult(set, ranklongidre + f.getName());
//
//        }
//        System.out.print("end.");
//        printTime();
//
//        EvalEasy.evalResultByOracle(ranklongidre);
    }
}
