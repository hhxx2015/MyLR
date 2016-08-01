package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Vector;
import model.Vector_vote;

//import lrObject.TestCase;

public class LoadFeatures{

	public static ArrayList<Vector> loadFeatures(int feanub,File f) throws IOException {
		feanub++;
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			String ss[] = line.split(" ");
			double feas[] = new double[feanub];
			//feas[feanub]=1.0;
			for (int i = 0; i < feanub; i++) {
				feas[i]=Double.parseDouble(ss[i+1]);
			}
			Vector v = new Vector(feas,ss[0]);
			re.add(v);
		}
		br.close();
		return re;
	}
	/**
	 * 加载特征
	 *    fea1 fea2.... lable
	 * @param feanub
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Vector> loadFeatures2(int feanub,File f) throws IOException {
		feanub++;
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			String ss[] = line.split("\t");
			double feas[] = new double[feanub];
			feas[0] =1.0;
			for (int i = 1; i < feanub; i++) {
				//System.out.println(ss[i]);
				feas[i]=Double.parseDouble(ss[i]);
			}
			Vector v = new Vector(feas,ss[2]);
			re.add(v);
		}
		br.close();
		return re;
	}
	
	
	/**
	 * 分类加载自定义feature，没有qid
	 * 1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 #
	 * 0 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 #  
	 */
	public static ArrayList<Vector> loadDefineFeature(int feanub,File f)throws IOException{
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			String ss[] = line.split(" ");
			double feas[] = new double[feanub];
			for (int i = 0; i < feanub; i++) {
				feas[i]=Double.parseDouble(ss[i+2].split(":")[1]);
			}
			//System.out.println(ss[0].substring(1));
			//int asa = Integer.parseInt(ss[0])-1;
			//Vector v = new Vector(feas,asa+"");
			Vector v = new Vector(feas,(ss[0]));
			re.add(v);
		}
		br.close();
		return re;
	}
	
	
	/**
	 * 分类加载svm普通feature，没有qid
	 * +1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 # 
	 */
	public static ArrayList<Vector> loadSvmFeature(int feanub,File f)throws IOException{
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			String ss[] = line.split(" ");
			double feas[] = new double[feanub];
			for (int i = 0; i < feanub; i++) {
				feas[i]=Double.parseDouble(ss[i+1].split(":")[1]);
			}
			//System.out.println(ss[0].substring(1));
			Vector v = new Vector(feas,getlable_1_1(ss[0]));
			re.add(v);
		}
		br.close();
		return re;
	}
	
	/**
	 * +1和-1的lable
	 * @param s
	 * @return
	 */
	private static String getsvmlable(String s){
		if(s.startsWith("+")){
			return "1";
		}else{
			return "0";
		}
	}

	/**
	 * 1和0的lable
	 * @param s
	 * @return
	 */
	private static String getlable_1_0(String s){
		if(s.startsWith("0")){
			return "0";
		}else{
			return "1";
		}
	}


	/**
	 * 1和-1的lable
	 * @param s
	 * @return
	 */
	private static String getlable_1_1(String s){
		if(s.startsWith("-")){
			return "0";
		}else{
			return "1";
		}
	}

	/**
	 * 加载svm rank 有qid ,在test上使用，没有进行求序对差值处理，直接进行预测
	 * +1 qid:1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 # 
	 * @throws IOException 
	 */
	public static ArrayList<Vector> loadSVM_RankFeaToClassify(int feanub,File f) throws IOException{
	//	feanub++;//分类面
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			re.add(lineToVector(line,feanub));
		}
		br.close();
		return re;
	}
	
/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分类↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**/

	/**
	 * 加载svm rank 有qid 排序，加载train数据
	 * 将投票数作为标签进行加载 lineToVectorForvoteLable
	 * 1 qid:1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 #
	 * @throws IOException
	 */
	public static ArrayList<Vector> loadSVM_RankFea_votelable(int feanub,File f){
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.err.println("加载train文件错误：文件不存在！");
			e.printStackTrace();
		}
		String line = "";
		ArrayList<Vector> fealist = new ArrayList<>();//用于暂时存放一个qid的list
		try {
			String qidtemp="qid:-1";
			while((line = br.readLine())!=null){
				String qidStr  = parseQid(line);
				if(qidStr.equals(qidtemp)){
					fealist.add(lineToVectorForvoteLable(line,feanub));
				}else{
					qidtemp = qidStr;
					re.addAll(toClassifyFea(fealist));

					fealist= new ArrayList<>();
					fealist.add(lineToVectorForvoteLable(line,feanub));
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("加载train文件错误：文件错误！");
			e.printStackTrace();
		}
		//System.out.println(fealist);
		re.addAll(toClassifyFea(fealist));
		return re;
	}

	/**
	 * 加载svm rank 有qid 排序，加载train数据
	 * 1 qid:1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 #
	 * @throws IOException 
	 */
	public static ArrayList<Vector> loadSVM_RankFea(int feanub,File f){
	//	feanub++;//分类面
		ArrayList<Vector> re = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.err.println("加载train文件错误：文件不存在！");
			e.printStackTrace();
		}
		String line = "";
		ArrayList<Vector> fealist = new ArrayList<>();//用于暂时存放一个qid的list
		try {
			String qidtemp="qid:-1";
			while((line = br.readLine())!=null){
				//System.out.println(line);
				String qidStr  = parseQid(line);
				if(qidStr.equals(qidtemp)){
					fealist.add(lineToVector(line,feanub));
				}else{
					qidtemp = qidStr;
					re.addAll(toClassifyFea(fealist));
					
					fealist= new ArrayList<>();
					fealist.add(lineToVector(line,feanub));
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("加载train文件错误：文件错误！");
			e.printStackTrace();
		}
		//System.out.println(fealist);
		re.addAll(toClassifyFea(fealist));
		return re;
	}
	
	/**
	 * 用于处理vote的数据
	 * 加载svm rank 有qid 排序，加载train数据
	 * 1 qid:1 1:0.6222222222222222 2:0.5833333333333334 3:0.6021505376344085 4:0.5888538380651945 5:0.4547149699531194 6:0.3210931827826005 #
	 * 0 qid:1 1:0.6222222222222222
	 * @throws IOException
	 */
	public static ArrayList<Vector_vote> loadSVM_RankFea_vote(int feanub,File f){
	//	feanub++;//分类面
		ArrayList<Vector_vote> re = new ArrayList<Vector_vote>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.err.println("加载train文件错误：文件不存在！");
			e.printStackTrace();
		}
		String line = "";
		ArrayList<Vector_vote> fealist = new ArrayList<>();//用于暂时存放一个qid的list
		try {
			String qidtemp="qid:-1";
			while((line = br.readLine())!=null){
				String qidStr  = parseQid(line);
				if(qidStr.equals(qidtemp)){
					fealist.add(lineToVector_vote(line,feanub));
				}else{
					qidtemp = qidStr;
					re.addAll(getpairs(fealist));

					fealist= new ArrayList<>();
					fealist.add(lineToVector_vote(line,feanub));
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("加载train文件错误：文件错误！");
			e.printStackTrace();
		}
		//System.out.println(fealist);
		re.addAll(getpairs(fealist));
		return re;
	}
	
	private static ArrayList<Vector_vote> getpairs(ArrayList<Vector_vote> list){
		ArrayList<Vector_vote> newlist = new ArrayList<>();
		while(list.size()>0){
			//System.out.println(list.size()+"...less...");
			Vector_vote v1 = list.get(0);
			list.remove(0);
			for (Vector_vote v2:list) {
				newlist.addAll(v1.compare(v2));
			}
		}
		return newlist;
	}
	
	private static String parseQid(String line){
		return line.split(" ")[1];
	}


	private static Vector lineToVectorForvoteLable(String line,int feanub){
		String ss[] = line.split(" ");
		double feas[] = new double[feanub];
//		feas[0] =1.0;
		for (int i = 0; i <( feanub); i++) {
			feas[i]=Double.parseDouble(ss[i+2].split(":")[1]);
		}
		int vote = Math.abs(Integer.parseInt(ss[ss.length-1]));
		String lable ="0";
//		if(vote>=5){
//			lable="5";
//		}else{
//			lable=vote+"";
//		}

		if(vote>=5){
			lable="2";
		}else if(vote>=1){
			lable="1";
		}else{
			lable="0";
		}

		return new Vector(feas,(lable));
	}


	private static Vector lineToVector(String line,int feanub){
		String ss[] = line.split(" ");
		double feas[] = new double[feanub];
//		feas[0] =1.0;
		for (int i = 0; i <( feanub); i++) {
			feas[i]=Double.parseDouble(ss[i+2].split(":")[1]);
		}
		return new Vector(feas,(ss[0]));
	}
	
	private static Vector_vote lineToVector_vote(String line,int feanub){
		String ss[] = line.split(" ");
		double feas[] = new double[feanub];
		for (int i = 0; i <( feanub); i++) {
			feas[i]=Double.parseDouble(ss[i+2].split(":")[1]);
		}
		String lable = getlable_1_0(ss[0]);
		int vote = Integer.parseInt(ss[ss.length-1]);
		return new Vector_vote(vote,feas,lable);
	}
	/**
	 * 一组qid的特征转化为差值的分类特征
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
	
	static void printTime(){
		//Long d = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
		System.out.println(sdf.format(new Date()));
	}

	public static void main(String[] args) throws IOException {
//		System.out.println(loadSvmFeature(6,new File(
//				"/home/hao/实验/text_alignment/meteorsvm/svmlight/train/05/Meteor_Score.txt")));
		printTime();
		ArrayList<Vector_vote> r = loadSVM_RankFea_vote(20,
				new File("/home/hao/桌面/mylr/train/rankvoteToge.txt"));
		System.out.println(r.get(0).toString());
		System.out.println(r.get(0).getFeatures()[0]);
		System.out.println(r.get(0).getFeatures()[19]);
		printTime();
//		ArrayList<Vector> r2 = loadDefineFeature(20,new File(
//				"/home/hao/workspace/MyLR/Data/detect/feature/train/williams/normal/suspicious-document001.txt"));
//		System.out.println(r2.get(0).toString());
//		System.out.println(r2.get(1).toString());
	//	System.out.println(r.get(0).toString());
	//	System.out.println(r.get(1).toString());
	//	System.out.println(r.get(0).getFeatures().length);
	}

}
