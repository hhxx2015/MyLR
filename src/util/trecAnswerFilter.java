package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.TreeMap;

public class trecAnswerFilter {
	public static void main(String[] args) throws Exception {
		for (int fold = 1; fold <=5; fold++) {
			
			BufferedReader br = new BufferedReader(new FileReader(new File(
					"/home/hao/实验/MQ2008/Fold"+fold+"/test.txt")));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					"/home/hao/实验/lr/Fold"+fold+"/test_answer.txt")));
			//queryid iter docid rank
			String line ="";
			while((line = br.readLine())!=null){
				String doc_id = line.split("#")[1].split(" ")[2];
				String sim = line.split("#")[1].split(" ")[8];
				String qid = line.split(" ")[1].replaceAll("qid:", "");
				String rank = line.split(" ")[0];
				
				StringBuilder sb = new StringBuilder();
				sb.append(qid);sb.append(" ");
				sb.append(0);sb.append(" ");
				sb.append(doc_id);sb.append(" ");
				sb.append(rank);
				
				bw.write(sb.toString());
				bw.flush();bw.newLine();
			}
			bw.close();br.close();
		}
	}
}
