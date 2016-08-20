package hao.LR.core;//package hao.LR.core;
//
//import java.io.*;
//import java.util.*;
//
//import hao.LR.entity.Vector;
///**
// * lr实现分类
// * @author hao
// * @version v2.0 构造时加入了分类面选择，加入了权重模型写出和加载
// *  @date 2016-5-31
// */
//public class LRclassifiation implements Serializable {
//    private final long serialVersionUID = 1L;
//
//	private double weight[];
//	private double alpha;
//	private int feaNub;//特征数量
//    private boolean b = false;//分类面标识，默认无分类面
//
//    //序列化写入文件
//    public void saveModel(String path) throws IOException {
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
//        oos.writeObject(this);
//        oos.close();
//    }
//
//    //反序列化从文件读取
//    public void loadModel(String path) throws IOException, ClassNotFoundException {
//        FileInputStream f = new FileInputStream(path);
//        ObjectInputStream ois = new ObjectInputStream(f);
//        LRclassifiation lrc = (LRclassifiation) ois.readObject();
//        ois.close();
//    }
//
//
//	@Override
//	public String toString() {
//        return serialVersionUID + "  feaNub=" + feaNub + ",b=" + b + ",[weight=" + Arrays.toString(weight) + ", alpha=" + alpha + "]";
//    }
//
//	public double[] getWeight(){
//		return this.weight;
//	}
//
//	/**
//	 * 构造方法1，用于声明分类器参数，初始化模型weight数组
//	 * @param feaNub
//	 * @param alpha
//	 * @param b
//	 */
//    public LRclassifiation(int feaNub, double alpha, boolean b) {
//        //super();
//        this.b=b;
//        if(b){
//            feaNub = feaNub + 1;
//        }
//        this.feaNub = feaNub;
//        this.weight = new double[feaNub];
//        for (int i = 0; i < feaNub; i++) {
//			this.weight[i]=1.0;
//		}
//		this.alpha = alpha;
//	}
//	/**
//	 * 构造方法2，用于加载模型
//	 */
//	public LRclassifiation(String ModelPath) {
//        try {
//            this.loadModel(ModelPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//	/**
//	 * @param itea 迭代次数
//	 * @param vectors 实例数组
//	 *
//	 */
//	public void train(int itea,ArrayList<Vector> vectors){
//		int vNub = vectors.size();//得到实例数量
//		for (int it = 0; it < itea; it++) {	//迭代次数
//			for (int j=0;j < feaNub ; j++) {//第j个特征
//				double Allwrong=0.0;
//				for (int i =0;i<vNub;i++) {//单独计算每一行实例的误差
//					Vector vecline = vectors.get(i);//得到该行实例
//					String lable = vecline.getLable();//得到标签y
//					double onefea[] = vecline.getFeatures();//得到特征数组
//                    double prediction1 = classify(vecline);//该行特征是1的概率  w*x
//                    double wrong = (prediction1 - Double.parseDouble(lable));//计算误差，wx-y
//                    if(b){
//                        try {
//                            Allwrong += (wrong * onefea[j]);            //叠加误差
//                        } catch (ArrayIndexOutOfBoundsException e) {
//                            Allwrong += (wrong * 1.0);                           //分类面
//                        }
//                    }else{
//                        Allwrong += (wrong * onefea[j]);
//                    }
//
//                }
//                weight[j] = weight[j] - (alpha * (Allwrong / (vNub + 0.0)));    //更新第j个特征
//            }
//        }
//	}
//	/**
//	 * 正例的概率
//	 * @param z
//	 * @return
//	 */
//	private double sigmoid(double z) {
//		//return Math.exp(z) / (1.0 + Math.exp(z));//1
//		return 1.0/ (1.0 + Math.exp(-z));//1
//	}
//
//	/**
//	 * 先求和后求概率
//     * @param v 特征数组
//     * @return
//     */
//	private double classify(Vector v) {
//		double[] feas = v.getFeatures();
//		double logit=0.0;
//		for (int i = 0; i < feaNub; i++) {
//			try{
//				logit += (feas[i]*weight[i]) ;
//			}catch( ArrayIndexOutOfBoundsException e){
//				//System.out.println(i);
//				logit += (1.0*weight[i]) ;
//			}
//		}
//		return sigmoid(logit);
//	}
//
//    /***
//     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分类部分↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//     ***/
//    private double Accuracy = 0.0;
//
//    public  double getPrecision(){
//		return this.Accuracy;
//	}
//
//	/**
//	 * 用于test
//	 * @param vectors
//	 * @return
//	 */
//	public ArrayList<Double> classify(ArrayList<Vector> vectors) {
//		int correct=0;
//		ArrayList<Double> list = new ArrayList<>();
//		for (Vector v:vectors) {
//			double score = classify(v);
//			if(score>=0.5&&v.getLable().equals("1")){
//				correct++;
//			}else if(score<0.5&&v.getLable().equals("0")){
//				correct++;
//			}
//			list.add(score);
//		}
//		Accuracy = (correct+0.0) / (vectors.size()+0.0);
//		return list;
//	}
//
///***↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分类↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*****/
//
//	/**
//	 * 特殊！！！！仅用于源检索实验test  longid
//	 * @param vectors
//	 * @return
//	 */
//	public ArrayList<Double> classifyForLongid(ArrayList<Vector> vectors) {
//		//?????
//		return null;
//	}
//
///*****************分主函数*************************************************************************	******************************************************/
////	public static void main(String[] args) throws IOException {
////		int feaNub=6;
////		int iter = 5000;
////		double alpha=0.5;
////
////		int b = 0;
////		//配置lr
////		LRclassifiation lrc = new LRclassifiation(feaNub, alpha);
////
////		 // 加载特征
////		//ArrayList<Vector> listtrain = LoadFeatures.loadFeatures2(feaNub+1,new File("../MyLR/Data/aa.txt"));
////		ArrayList<Vector> listtrain = LoadFeatures.loadSvmFeature(feaNub,new File("../MyLR/Data/05/Meteor_Score.txt"));
////		//train
////		lrc.train(iter,listtrain);
////
////		double weight[] = lrc.getWeight();
////		System.out.print("weight: ");
////		for (int i = 0; i < weight.length; i++) {
////			System.out.print(weight[i]+" ");
////		}
////		System.out.println();
////
////		//classify
////		System.out.println(lrc.classify(listtrain));
////		System.out.println(lrc.getPrecision());
//////		writeFile.writeResult(n.classifyAll(listtrain),
//////				"/home/hao/桌面/b");
////				//"/home/hao/桌面/分类任务/data/noQID/train/featurePospre.txt");
////	}
//}
///**
// * Other Vertion
// *
// *
// * public void train(int itea,ArrayList<Vector> vectors){
// int vNub = vectors.size();//得到实例数量
// for (int it = 0; it < itea; it++) {	//迭代次数
// for (int j=0;j < feaNub ; j++) {//第j个特征
// double Allwrong=0.0;
// for (int i =0;i<vNub;i++) {//单独计算每一行实例的误差
// Vector vecline = vectors.get(i);//得到该行实例
//
// String lable = vecline.getLable();//得到标签y
// double onefea[] = vecline.getFeatures();//得到特征数组
//
// double prediction1 = classify(vecline);//该行特征是1的概率  w*x
// double wrong =( prediction1- Double.parseDouble(lable) );//计算误差，wx-y
// try{
// Allwrong+=(wrong*onefea[j]);			//叠加误差
// }catch( ArrayIndexOutOfBoundsException e){
// Allwrong+=(wrong*1.0);                           //分类面
// }
// }
// weight[j] = weight[j] - (alpha*(Allwrong/(vNub+0.0))); 	//更新第j个特征
// }
// }
// }
// *
// //double prediction0 = sigmoid0(onefea*weight[i]);
// //if(prediction1>=0.5&&lable.equals("0")){
// //	//负例预测为正例
// //	//System.out.println(prediction-Double.parseDouble(lable));
// //	Allwrong-=(prediction1-Double.parseDouble(lable));
// //	errList.add(i);//记录错误的特征
// //}
// //if(prediction1<0.5&&lable.equals("1")){
// ////	System.out.print(weight[i]);System.out.print("  ");
// ////	System.out.print(onefea);System.out.print("  ");
// ////	System.out.print(prediction);System.out.print("  ");
// ////	System.out.print(lable);System.out.print("  ");
// ////	System.out.println(prediction-Double.parseDouble(lable));
// //	//正例预测为负例
// //	Allwrong-=(prediction1-Double.parseDouble(lable));
// //	errList.add(i);//记录错误的特征
// //}
//
// public void train(int itea,ArrayList<Vector> vectors){
// for (int it = 0; it < itea; it++) {
// for (Vector vecline :vectors) {
// //System.out.println(vecline);
// double feas[]  = vecline.getFeatures();//一行特征
// String lable = vecline.getLable();//该行的标签
//
// double Allwrong=0.0;
// ArrayList<Integer> errList = new ArrayList<>();
// for (int i=0; i<feas.length; i++) {
// //alpha = 4.0 / (1.0 + it + i) + 0.01;
// double prediction1 = sigmoid(feas[i]*weight[i]);// 1的概率
// if(prediction1>=0.5 && lable.equals("0")){
// //负例预测为正例
// //System.out.println(prediction-Double.parseDouble(lable));
// Allwrong-=(prediction1-Double.parseDouble(lable));
// errList.add(i);//记录错误的特征
// }
// if(prediction1<0.5 && lable.equals("1")){
// //					System.out.print(weight[i]);System.out.print("  ");
// //					System.out.print(onefea);System.out.print("  ");
// //					System.out.print(prediction);System.out.print("  ");
// //					System.out.print(lable);System.out.print("  ");
// //					System.out.println(prediction-Double.parseDouble(lable));
// //正例预测为负例
// Allwrong-=(prediction1-Double.parseDouble(lable));
// errList.add(i);//记录错误的特征
// }
//
// }
// double wrongAVG = Allwrong/(0.0+errList.size());
// for (int i :errList) {//按序更新weight
// weight[i] = weight[i] + (wrongAVG*alpha*feas[i]);
// }
// }
// }
// }
//
// public void train2(int itea,ArrayList<Vector> vectors){
// int m = vectors.size();
// for (int it = 0; it < itea; it++) {
//
// for (int j=0;j < feaNub ; j++) {
// double Allwrong=0.0;
// for (int i =0;i<m;i++) {
// Vector vecline = vectors.get(i);
// String lable = vecline.getLable();
// double onefea[] = vecline.getFeatures();
//
// //alpha = 4.0 / (1.0 + it + i) + 0.01;
// //double prediction1 = sigmoid1(onefea[j]*weight[j]);
// double prediction1 = classify(onefea);
// double wrong =(  prediction1- Double.parseDouble(lable) );
// Allwrong+=(wrong*onefea[j]);
// }
// weight[j] = weight[j] - (alpha*(Allwrong/(m+0.0)));
// //weight[j] = weight[j] - (alpha*(Allwrong));
// }
// }
// }
// **/
//
