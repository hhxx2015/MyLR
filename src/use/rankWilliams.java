package use;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hao.LR.core.LRclassifiation;
import hao.LR.entity.Vector;
import hao.LR.util.io.LoadFeatures;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月8日 下午6:11:16 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class rankWilliams {
	public static void main(String[] args) throws IOException {
		double alpha=0.1;
		int itea = 500;
		boolean b = true;

		String base = "willams_10vote";//normal20 willams_10vote
		int feaNub=20;

		String trainfile="/home/hao/桌面/mylr/train/"+base+".txt";
		String testFile="/home/hao/桌面/mylr/test/"+base+"/";
		String modelPath="../MyLR/Data/detect/feature/model/"+base+".txt";
		String rankPre="/home/hao/桌面/mylr/vote/"+base+"/";//willams20_vote willams_logvote

		//配置lr
		LRclassifiation lrc = new LRclassifiation(feaNub, alpha,b);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//System.out.println("loadbegin:"+sdf.format(System.currentTimeMillis()));
		//String trainFile="../MyLR/Data/detect/feature/train/willams_vote.txt";//trainWilliams.txt//voterank.txt
		//trainFile="../MyLR/Data/detect/feature/train/trainWilliams.txt";
		ArrayList<Vector> listtrain = LoadFeatures.loadSVM_RankFea(feaNub,new File(trainfile));
		//System.out.println("loadafter:"+sdf.format(System.currentTimeMillis()));
		//System.out.println(listtrain.get(0).getFeatures().length);
		//System.out.println(lrc.getWeight().length);
		lrc.train(itea, listtrain);
		new File(modelPath).mkdirs();
		lrc.writeWeight(modelPath+itea+"_"+alpha+"_21fearanklog.txt");
		
//
	}
}
//System.out.println(listtrain.get(6));
//lrc.train(itea, listtrain);
//
//writeFile.writeResult(lrc.classify(listtrain), "../MyLR/Data/pre.txt");