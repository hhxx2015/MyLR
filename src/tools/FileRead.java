package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


public class FileRead{
	public static ArrayList<String> FileReader(String idFile) throws IOException{
		ArrayList<String> idList = new ArrayList<String>();		
		ArrayList<String> alist = new ArrayList<String>();		
		FileReader fr = new FileReader(idFile);
		BufferedReader br = new BufferedReader(fr);		
		String line = "";		
		while((line=br.readLine())!=null){ 			
			idList.add(line);			
		}
		return idList;
	}
	 
	public static String readFile(File f) throws IOException {
		BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
		char[] buffer = new char[2048];
		int len = -1;
		StringBuilder sb = new StringBuilder();
		while ((len = br.read(buffer)) > -1) {
			sb.append(buffer, 0, len);
		}
		return  sb.toString();
	}
	
}
