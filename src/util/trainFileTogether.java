package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月11日 下午3:58:29 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class trainFileTogether {
	public static void main(String[] args) throws Exception {
		String trainfs="/home/hao/桌面/vote_ranksvm/train/normal20/";//willams_vote
		String allPath="/home/hao/桌面/vote_ranksvm/train/normal20.txt";
//		String trainfs="../MyLR/Data/detect/feature/train/williams/newTrain/";
//		String allPath="../MyLR/Data/detect/feature/train/voterank.txt";
		File fs[] = new File(trainfs).listFiles();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath), "UTF-8"));
		int qid = 1;
		for (File f : fs) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String newLine  = line.replace(" 1:", " qid:"+qid+" 1:");
				bw.write(newLine);bw.flush();bw.newLine();
			}
			qid++;
			br.close();
		}
		bw.close();
	}
}
