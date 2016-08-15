package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月11日 下午3:22:58 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class makeLRTrainfile_votefea {
	public static void main(String[] args) throws Exception {
		String oldTrain="../MyLR/Data/detect/feature/test/williams/normal/";
		String longidmap="../MyLR/Data/detect/feature/test/willianSta/";
		String newTrain = "../MyLR/Data/detect/feature/test/willams21_vote/";
		new File(newTrain).mkdirs();
		File fs[] = new File(oldTrain).listFiles();
		for (File f : fs) {
			BufferedReader brto = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newTrain+f.getName()), "UTF-8"));
			HashMap<String,Integer>lablemap=load_lable_map("utf-8",longidmap+f.getName());
			//System.out.println(lablemap);
			String oldLine = "";
			while ((oldLine = brto.readLine()) != null) {
				String longid=oldLine.split("#")[1];

				//System.out.println(longid);
				int newlable = lablemap.get(longid);
				String fea21 = " 21:" + newlable / 10 + " #";
				StringBuilder sb = new StringBuilder();
				sb.append(oldLine.split("#")[0] + fea21);
				sb.append(longid + " " + newlable);
				bw.write(sb.toString());
				bw.flush();
				bw.newLine();
			}
			brto.close();bw.close();
		}
	}
	private static HashMap<String, Integer> load_lable_map(String sc,String path) throws IOException {
		HashMap<String, Integer> map= new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(new File(path)),sc));
		String line="";
		while((line=br.readLine())!=null){
			String lable = line.split("\t")[0];
			int times =Integer.parseInt( line.split("\t")[1]);
			times=Math.abs(times);
			if(times>5){
				map.put(lable,2);
			}else{
				map.put(lable,1);
			}
			map.put(lable, times);
		}
	
		br.close();
		return map;
	}
}
