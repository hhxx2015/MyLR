package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import tools.writeFile;



public class readLRreold {
	/**
	 * williams tfidf l2r\
	 * cutLDA copyLDA
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		String testFile="../MyLR/Data/detect/feature/test/williams/normal/";
		String classifyPre="../MyLR/Data/detect/feature/classifyPre/test/";
		String longidrePath="../MyLR/Data/detect/feature/longidre/test/";
		
		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedReader prer = new BufferedReader(new FileReader(new File(classifyPre+f.getName())));
			
			HashSet<String> set = new HashSet<>();
			
			String longidline = "";
			while((longidline=br.readLine())!=null){
				String ld = prer.readLine();
				if(Double.parseDouble(ld)<0.5){
					
				}else{
					set.add(longidline.split("#")[1].replaceAll(" ", "\r\n"));
				}
			}
			prer.close();br.close();
			writeFile.writeResult(set, longidrePath+f.getName());
		}
	}
	
	public static void get(String testFile,String classifyPre,String longidrePath) throws IOException {
//		String testFile="../MyLR/Data/detect/feature/train/williams/normal/";
//		String classifyPre="../MyLR/Data/detect/feature/classifyPre/train/";
//		String longidrePath="../MyLR/Data/detect/feature/longidre/train/";
		
		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedReader prer = new BufferedReader(new FileReader(new File(classifyPre+f.getName())));
			
			HashSet<String> set = new HashSet<>();
			
			String longidline = "";
			while((longidline=br.readLine())!=null){
				String ld = prer.readLine();
				if(Double.parseDouble(ld)<0.5){
					
				}else{
					set.add(longidline.split("#")[1].replaceAll(" ", "\r\n"));
				}
			}
			prer.close();br.close();
			writeFile.writeResult(set, longidrePath+f.getName());
		}
	}
	
	public static void getrank(String testFile,String classifyPre,String longidrePath) throws IOException {

		File fs[] = new File(testFile).listFiles();
		for (File f : fs) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			BufferedReader prer = new BufferedReader(new FileReader(new File(classifyPre+f.getName())));
			
			ArrayList<String> scorelist = new ArrayList<String>();
			ArrayList<String> linelist = new ArrayList<String>();
			String longidline = "";
			while((longidline=br.readLine())!=null){
				String score = prer.readLine();
				scorelist.add(score);
				linelist.add(longidline);
			}
			prer.close();br.close();
			
			HashSet<String> set = new HashSet<>();
			writeFile.writeResult(set, longidrePath+f.getName());
		}
	}
}
