package util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

/** 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月6日 下午7:52:42 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class StatisticTrainingFea {
	/**
	 * longid -- lable -- freq
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String path = "../MyLR/Data/detect/feature/test/williams/normal";
		String pathout="../MyLR/Data/detect/feature/test/willianSta/";
		new File(pathout).mkdirs();
		File fs[] = new File(path).listFiles();
		
		int lineNub=0;
		HashMap<Integer, Integer> sizeMap = new HashMap<Integer, Integer>();
		HashMap<String, ArrayList<String>> mapLable = new HashMap<String, ArrayList<String>>();
		
		for (File f : fs) {
		//File f= new File(path);
		//	System.out.println(f.getName());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathout+f.getName()), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				lineNub++;
				String longid = line.split("#")[1].trim();
				String lable = line.split(" ")[0];
				if(mapLable.containsKey(longid)){
					ArrayList<String > list = mapLable.get(longid);
					list.add(lable);
					mapLable.put(longid, list);
				}else{
					ArrayList<String > list = new ArrayList<>();
					list.add(lable);
					mapLable.put(longid,list);
				}
			}
			
			for (String longid:mapLable.keySet()) {
				ArrayList<String> list = mapLable.get(longid);
				if(list.size()>0){
					int tt = Integer.parseInt(list.get(0))*(list.size());
					
					bw.write(longid+"\t");
					bw.write(tt+"\t");
				//System.out.print(longid+"\t");
					for (String lable : list) {
						//System.out.print(lable+"\t");
						bw.write(lable+"\t");
					}
					//System.out.println();
					bw.flush();bw.newLine();
				}
			}
			br.close();bw.close();
		}
	///	System.out.println(sizeMap);
		for (int i :sizeMap.keySet()) {
			System.out.print(i+"\t");
			System.out.println(sizeMap.get(i));
		}
		System.out.println(lineNub);
	}
}


