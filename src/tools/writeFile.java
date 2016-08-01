package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class writeFile {

	
	public static void writeResult(HashSet <String>idList, String fileName) throws IOException {  
		BufferedWriter bw  = new BufferedWriter( new FileWriter(fileName));
	//	System.out.println(idList.size());
		for(String s:idList){ 
			bw.write(s);
			bw.newLine();
		}
		bw.flush();
		bw.close(); 
	}
	public static void writeResult(ArrayList idList, String fileName) throws IOException {  
		BufferedWriter bw  = new BufferedWriter( new FileWriter(fileName));
	//	System.out.println(idList.size());
		for(int i=0;i<idList.size();i++){ 
			bw.write(idList.get(i).toString());
			bw.newLine();
		}
		bw.flush();
		bw.close(); 
	}
	public static void writeResult(List<String> idList, String fileName,boolean sign) throws IOException {  
		if(sign){
		FileWriter fw = new FileWriter(fileName,true); 
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
	}
	public static void writeResult(ArrayList<String> idList,String dir, String FileName) throws IOException {  
		File f = new File(dir);
		f.mkdir();
		FileWriter fw = new FileWriter(dir+"\\"+FileName); 
		BufferedWriter bw  = new BufferedWriter(fw);
		
		for(int i=0;i<idList.size();i++){	
			bw.newLine();
			bw.write(idList.get(i));			
		}
		bw.flush();
		fw.close();
		bw.close(); 
	}
	

	public static void writeResult(String s, String fileName) throws IOException {  
		FileWriter fw = new FileWriter(fileName); 
		BufferedWriter bw  = new BufferedWriter(fw);
	//	System.out.println(idList.size());
		
			bw.write(s);
			bw.newLine();
		
		fw.flush();
		bw.flush();
		fw.close();
		bw.close(); 
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
	
}
