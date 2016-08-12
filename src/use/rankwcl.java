package use;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;
import tools.writeFile;

/**
 * @author hao : 1347261894@qq.com
 * @date 创建时间：2016年6月11日 下午4:50:32
 * @version 1.0
 * @parameter
 * @since
 * @return
*/
public class rankwcl {
	static void printTime(){
		//Long d = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ss");
		System.out.println(sdf.format(new Date()));
	}

	public static void main(String[] args) throws IOException {
		double alpha=0.1;
		int itea = 500;
		int b = 1;

		int feaNub=21;
		String base = "willams_10vote";//normal20 willams_10vote
		String vote="_votelable2";//_vote

		String trainfile="/home/hao/桌面/mylr/train/"+base+".txt";
		String testFile="/home/hao/桌面/mylr/test/"+base+"/";
		String modelPath="../MyLR/Data/detect/feature/model/"+base+"_"+alpha+"_"+itea+vote+".txt";
		String rankPre="/home/hao/桌面/mylr/vote/"+base+vote+"/";//willams20_vote willams_logvote

		new File(rankPre).mkdirs();

		System.out.println(base+"");
		System.out.print("loadfeatures......");printTime();
//		ArrayList<Vector> trainfeaList = LoadFeatures.loadSVM_RankFea(
//				feaNub,new File(trainfile));

		ArrayList<Vector> trainfeaList = LoadFeatures.loadSVM_RankFea_votelable(
				feaNub,new File(trainfile));


		System.out.print("initlrv......");printTime();
		LRclassifiation lrc = new LRclassifiation(feaNub, alpha, b);
		System.out.print("train......");
		System.out.print(trainfeaList.size()+"...");printTime();
		lrc.train(itea, trainfeaList);
		lrc.writeWeight(modelPath);

		//LRclassifiation lrc = new LRclassifiation(modelPath);
		System.out.print("testclassify......");printTime();
		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			ArrayList<Vector> listtest = LoadFeatures.loadDefineFeature(feaNub,f);
			//System.out.println(listtest.get(0));

			ArrayList<Double> re =lrc.classify(listtest);
			writeFile.writeResult(re, rankPre+f.getName());
		}
		System.out.print("end.");
		printTime();
	}
}
