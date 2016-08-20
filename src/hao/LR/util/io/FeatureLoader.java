//package hao.LR.util.io;
//
//
//import hao.LR.core.LRclassifiation;
//import hao.LR.entity.Vector;
//import tools.writeFile;
//import util.readLRreold;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.lang.invoke.LambdaConversionException;
//import java.util.ArrayList;
//
///**
// * Created by hao on 16-8-3.
// */
//public class FeatureLoader {
//    /**
//     * C1("1", "-1")
//     * C2("+1", "-1")
//     * C3("1", "0")
//     * RANK("","")
//     * 标签类型枚举，初始化lable的类型
//     */
//    public enum lableClass {
//        C1("1", "-1"), C2("+1", "-1"), C3("1", "0"), RANK("", ""), OTHER("", "");
//        // 成员变量
//        private String N;
//        private String P;
//
//        // 构造方法
//        private lableClass(String N, String P) {
//            this.N = N;
//            this.P = P;
//        }
//
//        public void initOther(String N, String P) {
//            OTHER.N = N;
//            OTHER.P = P;
//        }
//
//    }
//
//    private int feaNub;//
//    private String splitChar;//空格
//    private boolean rankFea;//是否排序
//    private boolean feaBub;//是否有特征号码
//    private lableClass lableclass;
//    private int indexFeaStart;
//
//    /**
//     * @param feaBub     是否有特征序号
//     * @param feaNub     特征数量
//     * @param lableclass 标签类型
//     */
//    public FeatureLoader(boolean feaBub, int feaNub, lableClass lableclass) {
//        this.splitChar = " ";
//        this.feaBub = feaBub;
//        if (lableclass == lableClass.RANK) {
//            this.rankFea = true;
//            indexFeaStart = 2;
//        } else {
//            this.rankFea = false;
//            indexFeaStart = 1;
//        }
//        this.feaNub = feaNub;
//        this.lableclass = lableclass;
//    }
//
//    public ArrayList<Vector> loadFea(String f) throws IOException {
//        ArrayList<Vector> re = new ArrayList<>();
//        BufferedReader br = new BufferedReader(new FileReader(new File(f)));
//        String line = "";
//        if (rankFea) {
//            String qidTemp = "qid:-1";
//            ArrayList<Vector> fealist = new ArrayList<>();//用于暂时存同放一个qid的list
//            while ((line = br.readLine()) != null) {
//                String qidStr = parseQid(line);
//                if (qidTemp.equals(qidStr)) {
//                    fealist.add(lineToVector(line, feaNub));
//                } else {
//                    qidTemp = qidStr;
//                    re.addAll(toClassifyFea(fealist));
//
//                    fealist = new ArrayList<>();
//                    fealist.add(lineToVector(line, feaNub));
//                }
//            }
//            re.addAll(toClassifyFea(fealist));
//            br.close();
//            return re;
//        } else {
//            while ((line = br.readLine()) != null) {
//                re.add(lineToVector(line, feaNub));
//            }
//            br.close();
//            return re;
//        }
//    }
//
//
//    private String parseQid(String line) {
//        return line.split(" ")[1];
//    }
//
//    private Vector lineToVector(String line, int feanub) {
//        String ss[] = line.split(splitChar);
//        double feas[] = new double[feanub];
//
//        for (int i = 0; i < (feanub); i++) {
//            if (feaBub) {
//                feas[i] = Double.parseDouble(ss[i + indexFeaStart].split(":")[1]);
//            } else {
//                feas[i] = Double.parseDouble(ss[i + indexFeaStart]);
//            }
//        }
//
//        if (rankFea) {
//            return new Vector(feas, ss[0]);
//        } else {
//            if (ss[0].equals(lableclass.N)) {
//                return new Vector(feas, "1");
//            } else if (ss[0].equals(lableclass.P)) {
//                return new Vector(feas, "0");
//            } else {
//                return null;
//            }
//        }
//    }
//
//    /**
//     * 一组qid的特征转序对的差值的特征
//     *
//     * @param list
//     * @return
//     */
//    public static ArrayList<Vector> toClassifyFea(ArrayList<Vector> list) {
//        ArrayList<Vector> newlist = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            Vector v1 = list.get(i);
//            for (int j = 0; j < i; j++) {
//                Vector v2 = list.get(j);
//                newlist.addAll(v1.compare(v2));
//            }
//        }
//        return newlist;
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        int feaNub = 20;//46
//
//
//        double alpha = 0.1;
//        int itea = 500;
//
//        int b = 1;
//        //配置lr
//        LRclassifiation lrc = new LRclassifiation(feaNub, alpha, b);
//
//        boolean BUB = true;
//        String trainPath = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/train/trainWilliams_classify.txt";
//        lableClass lc = lableClass.OTHER;
//        lc.initOther("a", "b");
//        FeatureLoader trainloader = new FeatureLoader(BUB, feaNub, lc);
//        ArrayList<Vector> listTrain = trainloader.loadFea(trainPath);
//
//        lrc.train(itea, listTrain);
//        lrc.toString();
//        String testFile = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/test/williams/normal/";
//        String classifyPre = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/classifyPre/test/";
//
//        File fs[] = new File(testFile).listFiles();
//        for (File f : fs) {
//            ArrayList<Vector> listtest = LoadFeatures.loadSvmFeature(feaNub, f);
//
//            ArrayList<Double> re = lrc.classify(listtest);
//            writeFile.writeResult(re, classifyPre + f.getName());
//        }
//
//        String longidrePath = "/home/hao/IdeaProjects/MyLR/Data/detect/feature/longidre/test/";
//        readLRreold.get(testFile, classifyPre, longidrePath);
//
//    }
//}
