package l2r4sr2016.iotools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class writeFile {

	public static void writeResult(ArrayList<String> idList, String fileName) throws IOException {
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"); 
		BufferedWriter bw  = new BufferedWriter(fw);
	//	System.out.println(idList.size());
		for(int i=0;i<idList.size();i++){ 
			bw.write(idList.get(i));
			bw.newLine();
		}
		fw.flush();
		bw.flush();
		fw.close();
		bw.close(); 
	}
	public static void writeResult(HashSet<String> set, String fileName) throws IOException {  
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"); 
		BufferedWriter bw  = new BufferedWriter(fw);
	//	System.out.println(idList.size());
		Iterator<String> iterator=set.iterator();	
		while(iterator.hasNext()){						
			bw.write(iterator.next());
			bw.newLine();
		}
		fw.flush();
		bw.flush();
		fw.close();
		bw.close(); 
	}
	public static void writeResult(HashMap<String,String> map, String dir) throws IOException {  
		//File f = new File(dir);
		//f.mkdir();
		//FileWriter fw = new FileWriter(dir);
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(dir),"UTF-8"); 

		BufferedWriter bw  = new BufferedWriter(fw);
		for(String key:map.keySet()){
			bw.write(key+"\t"+map.get(key));
			
			bw.newLine();
	    }
	    
		bw.flush();
		fw.close();
		bw.close(); 
	}
	public static void writeResult(ArrayList<String> idList,String dir, String FileName) throws IOException {  
		File f = new File(dir);
		f.mkdir();
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(dir),"UTF-8"); 
		BufferedWriter bw  = new BufferedWriter(fw);
		
		for(int i=0;i<idList.size();i++){			
			bw.write(idList.get(i));
			bw.newLine();
		}
		bw.flush();
		fw.close();
		bw.close(); 
	}
	
	public static void writeResult(HashMap<Integer,ArrayList<String>> doc,String filedir,String fileName) throws IOException{
		File f = new File(filedir);
		f.mkdir();
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(filedir+"\\"+fileName),"UTF-8"); 

//		FileWriter fw = new FileWriter(filedir+"\\"+fileName); 
		BufferedWriter bw  = new BufferedWriter(fw);
		
		for(Integer num:doc.keySet()){
			ArrayList<String> keyList = doc.get(num);			
			String keyString = keyList.toString();
			keyString  = keyString.substring(1,keyString.length()-1).replaceAll(", "," ");
			bw.write(keyString);
			bw.newLine();
		}
		 
		bw.flush();
		fw.flush();
		fw.close();
		bw.close(); 
	} 
	public static void writeResult(String s, String fileName) throws IOException {  
		//FileWriter fw = new FileWriter(fileName); 
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"); 
		BufferedWriter bw  = new BufferedWriter(fw);
	//	System.out.println(idList.size());
		
			bw.write(s);
			bw.newLine();
		
		fw.flush();
		bw.flush();
		fw.close();
		bw.close(); 
	}
	public static void writeResult(ArrayList<String> idList,BufferedWriter out,
			int sign) {		
		if(sign==1){
		try {
			
			for (int i = 0; i < idList.size(); i++) {
				out.write(idList.get(i));
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		}
		
	}
	public static void writeResult(ArrayList<String> idList, String fileName,
			int sign) {
		BufferedWriter out = null;
		if(sign==1){
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true),"UTF-8"));
			for (int i = 0; i < idList.size(); i++) {
				out.write(idList.get(i));
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
	}

	
	public static void main(String []arg){
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("a");
		arr.add("b");
		arr.add("c");
		System.out.println(arr);
		String keyString = arr.toString();
		keyString  = keyString.substring(1,keyString.length()-1).replaceAll(", "," ");
		System.out.println(keyString);
	}
	public static void writeResult(List<String> idList, String dir) throws IOException {
		
		OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(dir),"UTF-8"); 
		BufferedWriter bw  = new BufferedWriter(fw);
		
		for(int i=0;i<idList.size();i++){			
			bw.write(idList.get(i));
			bw.newLine();
		}
		bw.flush();
		fw.close();
		bw.close(); 
		
	}
	public static void writeResult(HashSet<String> refset, String refAnsfile,
			int sign) {
		ArrayList<String>list=new ArrayList<String>();
		for(String line:refset){
			list.add(line);
		}
		writeResult(list,refAnsfile,1);
	}
	
}
