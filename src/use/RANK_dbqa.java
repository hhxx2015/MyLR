package use;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;
import tools.writeFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by hao on 16-7-4.
 */
public class RANK_dbqa {

    public static double[] lineToFeas(String line,int feaNub){
        double re[] = new double[feaNub];
        String ss[] = line.split(" ");
        for (int i=0;i<feaNub;i++){
            re[i] = Double.parseDouble(ss[i]);
        }
        return re;
    }


    public static ArrayList<Vector> loadFeature(String paths,int feaNub) throws Exception {
        String pathLable = "/home/hao/桌面/DBQA/nlpcc-iccpol-2016.dbqa.training-data";
        ArrayList<Vector> re = new  ArrayList<Vector>();
        BufferedReader br1 = new BufferedReader(new FileReader(new File(paths)));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(pathLable)));
        String line = "";
        while((line = br1.readLine())!=null){
            String lable = br2.readLine().split("\t")[2];
            double feas[] = lineToFeas(line,feaNub);
            re.add(new Vector(feas,lable));
        }
        ArrayList<Vector> nre = new ArrayList<>();
        nre.addAll(LoadFeatures.toClassifyFea(re));
        return nre;
    }

    public static void main(String[] args)  throws Exception{
        int feaNub=32;
        double alpha=1;
        int itea = 500;

        boolean b = true;
        LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);

        String feaPath="/home/hao/桌面/DBQA/all/togenormal.fea";
        ArrayList<Vector> listtrain = loadFeature(feaPath,feaNub);

        System.out.println(listtrain.size());
        lrc.train(itea,listtrain);
        ArrayList<Double> re = lrc.classify(listtrain);

        System.out.println(lrc.toString());
        String rePath="/home/hao/桌面/DBQA/lrre/rer1.txt";
        writeFile.writeResult(re,rePath);
    }
}
