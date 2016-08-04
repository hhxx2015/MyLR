package hao.LR.entity;

import java.util.ArrayList;

/** 
 * 特征类  lable Stringlist
 * 
* @author  hao : 1347261894@qq.com 
* @date 创建时间：2016年6月12日 下午3:05:11 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/
public class Feature {
	
	private String lable="";
	private ArrayList<String> wordList;
	
	public Feature(String lable, ArrayList<String> wordList) {
		//super();
		this.lable = lable;
		this.wordList = wordList;
	}
	
	@Override
	public String toString() {
		return "Feature [lable=" + lable + ", wordList=" + wordList + "]";
	}

	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public ArrayList<String> getWordList() {
		return wordList;
	}
	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}
	
}
