package tools;

import java.io.*;

public class readEval {
	public static void main(String[] args) throws Exception {
		double mapAll=0.0;
		for (int fold = 1; fold <=5; fold++) {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					"/home/hao/实验/lr/Fold"+fold+"/evaltest.txt")));//evalvali  evaltest
					//"/home/hao/实验/lr/Fold"+fold+"/evalvali.txt")));//evalvali  evaltest
			String line = "";
			while((line = br.readLine())!=null){
				if(line.startsWith("map ")){//P10 
					System.out.println(line);
					mapAll+=Double.parseDouble(line.split("\t")[2]);
				}
			}br.close();
		}
		System.out.println(mapAll/5.0);
	}
}
//P10            	all	0.2580+
//P10            	all	0.2410
//P10            	all	0.2318
//P10            	all	0.2414
//P10            	all	0.2981
//0.25406