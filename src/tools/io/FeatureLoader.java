package tools.io;


import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by hao on 16-8-3.
 */
public class FeatureLoader {
    /**
     * C1("1", "-1")
     * C2("+1", "-1")
     * C3("1", "0")
     * RANK("","")
     */
    public enum lableClass {
        C1("1", "-1"), C2("+1", "-1"), C3("1", "0") ,RANK("","");
        // 成员变量
        private String N;
        private String P;

        // 构造方法
        private lableClass(String N, String P) {
            this.N = N;
            this.P = P;
        }
    }

    private int feaNub;//
    private String splitChar = " ";//空格
    private boolean rankFea;//是否排序
    private boolean feaBub;//是否有特征序号
    private lableClass lableclass;
    private int indexFeaStart;

    /**
     * @param feaBub 是否有特征序号
     * @param feaNub 特征数量
     * @param lableclass 标签类型
     */
    public FeatureLoader(boolean feaBub,int feaNub, lableClass lableclass) {
        this.splitChar = " ";
        this.feaBub=feaBub;
        if(lableclass==lableClass.RANK){
            this.rankFea = true;
            indexFeaStart=2;
        }else{
            this.rankFea = false;
            indexFeaStart=1;
        }
        this.feaNub = feaNub;
        this.lableclass = lableclass;
    }


    /**
     * @param feaBub     是否有特征序号
     * @param qid        是否有qid
     * @param feaNub     特征数量
     * @param lableclass 标签类型
     */
    public FeatureLoader(boolean feaBub, boolean qid, int feaNub, lableClass lableclass) {
        this.splitChar = " ";
        this.feaBub = feaBub;
        if (lableclass == lableClass.RANK) {
            this.rankFea = true;
            indexFeaStart = 2;
        } else {
            this.rankFea = false;
            indexFeaStart = 1;
        }
        if (qid) {
            indexFeaStart = 2;
        }
        this.feaNub = feaNub;
        this.lableclass = lableclass;
    }

    public  ArrayList<Vector> loadFea(String f) throws IOException {
        ArrayList<Vector> re = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(f)));
        String line = "";
        if(rankFea){
            String qidTemp="qid:-1";
            ArrayList<Vector> fealist = new ArrayList<>();//用于暂时存同放一个qid的list
            while((line = br.readLine())!=null){
                String qidStr  = parseQid(line);
                if(qidTemp.equals(qidStr)){
                    fealist.add(lineToVector(line,feaNub));
                }else{
                    qidTemp = qidStr;
                    re.addAll(toClassifyFea(fealist));

                    fealist= new ArrayList<>();
                    fealist.add(lineToVector(line,feaNub));
                }
            }
            re.addAll(toClassifyFea(fealist));
            br.close();
            return re;
        }else{
            while((line = br.readLine())!=null){
                re.add(lineToVector(line,feaNub));
            }
            br.close();
            return re;
        }
    }



    private String parseQid(String line){
        return line.split(" ")[1];
    }

    private Vector lineToVector(String line,int feanub){
        String ss[] = line.split(splitChar);
        double feas[] = new double[feanub];

        for (int i = 0; i <( feanub); i++) {
            if(feaBub){
                feas[i]=Double.parseDouble(ss[i+indexFeaStart].split(":")[1]);
            }else{
                feas[i]=Double.parseDouble(ss[i+indexFeaStart]);
            }
        }

        if(rankFea){
            return new Vector(feas,ss[0]);
        }else{
            if (ss[0].equals(lableclass.N)) {
                return new Vector(feas, "1", line.split("#")[1].split(" ")[0]);
            } else if (ss[0].equals(lableclass.P)) {
                return new Vector(feas, "0", line.split("#")[1].split(" ")[0]);
            } else {
                return new Vector(feas, ss[0], line.split("#")[1].split(" ")[0]);
            }
        }
    }

    /**
     * 一组qid的特征转序对的差值的特征
     * @param list
     * @return
     */
    public static ArrayList<Vector> toClassifyFea(ArrayList<Vector> list){
        ArrayList<Vector> newlist = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Vector v1 = list.get(i);
            for (int j = 0; j < i; j++) {
                Vector v2 = list.get(j);
                newlist.addAll(v1.compare(v2));
            }
        }
        return newlist;
    }


    public static void main(String[] args) throws IOException {
        int feaNub=3;//46

        String trainPath="/home/hao/桌面/lrtest/train";
        String testPath="/home/hao/桌面/lrtest/test";
        String testPre1 = "/home/hao/桌面/lrtest/pre";

        ArrayList<Vector> listTrain = LoadFeatures.loadSVM_RankFea(feaNub,new File(trainPath));
        System.out.println(listTrain.size());

        FeatureLoader trainloader = new FeatureLoader(true , feaNub, lableClass.RANK);
        ArrayList<Vector> listTrain2 = trainloader.loadFea(trainPath);
        System.out.println(listTrain2.size());


    }
}
