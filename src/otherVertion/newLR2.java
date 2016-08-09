package otherVertion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;
import tools.writeFile;


public class newLR2 {
	private double weight[];
	private double alpha;
	private int feaNub;
	
	@Override
	public String toString() {
		return "newLR [weight=" + Arrays.toString(weight) + ", alpha=" + alpha + "]";
	}

	public double[] getWeight(){
		return this.weight;
	}
	
	public newLR2(int feaNub, double alpha) {
		//super();
		this.feaNub=feaNub;
		this.weight=new double [feaNub];
		for (int i = 0; i < feaNub; i++) {
			this.weight[i]=1.0;
		}
		this.alpha = alpha;
	}
	
	public void train(int itea,ArrayList<Vector> vectors){
		for (int it = 0; it < itea; it++) {
			for (Vector vecline :vectors) {
				//System.out.println(vecline);
				double feas[]  = vecline.getFeatures();//一行特征
				String lable = vecline.getLable();//该行的标签
				
				double Allwrong=0.0;
				ArrayList<Integer> errList = new ArrayList<>();
				for (int i=0; i<feas.length; i++) {
					//alpha = 4.0 / (1.0 + it + i) + 0.01;
					double prediction1 = sigmoid1(feas[i]*weight[i]);// 1的概率						
					if(prediction1>=0.5 && lable.equals("0")){
						//负例预测为正例
						//System.out.println(prediction-Double.parseDouble(lable));
						Allwrong-=(prediction1-Double.parseDouble(lable));
						errList.add(i);//记录错误的特征
					}
					if(prediction1<0.5 && lable.equals("1")){
	//					System.out.print(weight[i]);System.out.print("  ");
	//					System.out.print(onefea);System.out.print("  ");
	//					System.out.print(prediction);System.out.print("  ");
	//					System.out.print(lable);System.out.print("  ");
	//					System.out.println(prediction-Double.parseDouble(lable));
						//正例预测为负例	
						Allwrong-=(prediction1-Double.parseDouble(lable));
						errList.add(i);//记录错误的特征
					}
					
				}
				double wrongAVG = Allwrong/(0.0+errList.size());
				for (int i :errList) {//按序更新weight
					weight[i] = weight[i] + (wrongAVG*alpha*feas[i]); 
				}
			}
		}
	}
	
	public void train2(int itea,ArrayList<Vector> vectors){
		int m = vectors.size();
		for (int it = 0; it < itea; it++) {
			for (int j=0;j < feaNub ; j++) {
				double Allwrong=0.0;
				for (int i =0;i<m;i++) {
					Vector vecline = vectors.get(i);
					String lable = vecline.getLable();
					double onefea[] = vecline.getFeatures();
					
					//alpha = 4.0 / (1.0 + it + i) + 0.01;
					//double prediction1 = sigmoid1(onefea[j]*weight[j]);
					double prediction1 = sigmoidAll(onefea);
					double wrong =(  prediction1- Double.parseDouble(lable) );
					Allwrong+=(wrong*onefea[j]);
				}
				weight[j] = weight[j] - (alpha*(Allwrong/(m+0.0))); 
			}
		}
	}
	public ArrayList<Double> classify(ArrayList<Vector> vectors) {
		ArrayList<Double> list = new ArrayList<>();
		for (Vector v:vectors) {
			double[] feas = v.getFeatures();
			double logit = 0.0;
			for (int i=0; i<weight.length;i++)  {
				logit +=( weight[i] * feas[i]);
				//System.out.println(logit);
			}
			list.add(sigmoid1(logit));
		}
		return list;
	}

	/**
	 * 正例的概率
	 * @param z
	 * @return
	 */
	private double sigmoid1(double z) {
		//return Math.exp(z) / (1.0 + Math.exp(z));//1
		return 1.0/ (1.0 + Math.exp(-z));//1
	}
	/**
	 * 负例的概率
	 * @param z
	 * @return
	 */
	private double sigmoidAll(double []z) {
		double re=0.0;
		for (int i = 0; i < feaNub; i++) {
			re +=sigmoid1(  (z[i]*weight[i])  );//0
		}
		return re;
	}

	public static void main(String[] args) throws IOException {
		int feaNub=2;
		double alpha=0.05;
		newLR2 n = new newLR2(feaNub, alpha);
		ArrayList<Vector> listtrain = LoadFeatures.loadFeatures2(
				feaNub,new File("/home/hao/桌面/aa.txt"));

		n.train2(500,listtrain);
	
		double weight[] = n.getWeight();
		for (int i = 0; i < weight.length; i++) {
			System.out.print(weight[i]+" ");
		}
		System.out.println();
		System.out.println(n.classify(listtrain));
//		System.out.println(n.classify(listtrain).get(0));
//		System.out.println(n.classify(listtrain).get(listtrain.size()-1));
//		//n.classify(weight);
		writeFile.writeResult(n.classify(listtrain), 
				"/home/hao/桌面/b");
				//"/home/hao/桌面/分类任务/data/noQID/train/featurePospre.txt");
	}
}
