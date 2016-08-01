package l2r4sr2016.iotools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class ReadFile {
	

	public static HashMap<Integer, String> getPassage(String fileName)
			throws IOException {// 闁哄倸娲ｅ▎銏ゅ触瀹ュ懐鎽�
		HashMap<Integer, String> passage = new HashMap<Integer, String>();

		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);

		int num = 0;
		String s = "";
		String lastPass = "";
		while ((s = br.readLine()) != null) {
			if (s.equals("") && !lastPass.equals("")) {
				passage.put(num, lastPass.trim());
				lastPass = "";
				num++;
			} else {
				lastPass = (lastPass.trim() + " " + s).trim();
			}
		}
		if (!lastPass.equals("")) {
			// lastPass=Filter.operate(analyzer, lastPass);
			passage.put(num, lastPass.trim());
		}
		br.close();
		fr.close();
		return passage;
	}

	//
	public static HashMap<Integer, String> getSentence(String fileName)
			throws IOException {
		HashMap<Integer, String> Sentences = new HashMap<Integer, String>();
		HashMap<Integer, String> passage = getPassage(fileName);
		int num = 0;
		for (Integer key : passage.keySet()) {
			String pass = passage.get(key);
			String[] sen = pass.split("[.?!]");
			for (int i = 0; i < sen.length; i++) {
				if (sen[i].length() == 0) {
					continue;
				}
				// String s=Filter.operate(analyzer, sen[i]);
				Sentences.put(num, sen[i].trim());
				num++;
			}
		}

		return Sentences;
	}

	
	public static void printList(ArrayList<String> l) {
		for (String s : l) {
			System.out.println(s);
		}
	}

	

	public static StringBuffer getFileContent(String filedir)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filedir));
		StringBuffer sb = new StringBuffer("");
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s + "\n");
		}
		br.close();
		return sb;
	}

	public static ArrayList<String> getFileConList(String filedir)
			throws IOException {
		ArrayList<String> files = new ArrayList<String>();
		InputStreamReader fr = new InputStreamReader(new FileInputStream(
				filedir), "utf-8");
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		while ((s = br.readLine()) != null) {
			if(s.length()>0){
			files.add(s);
			}
		}
		br.close();
		return files;
	}

	public static String readFile(String textdir) throws IOException {

		StringBuilder text = new StringBuilder();
		InputStreamReader fr = new InputStreamReader(new FileInputStream(
				textdir), "utf-8");
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			text.append(line + " ");
		}
		br.close();
		fr.close();
		return text.toString();
	}
	public static HashSet<String> readFileToSet(String textdir) throws IOException {
        ArrayList<String>list=getFileConList(textdir);
        HashSet<String>set=new HashSet<String>();
        for(String line:list){
        	if(line.length()>0){
        	set.add(line.trim());
        	}
        }		
		return set;
	}

	public static void main(String arg[]) throws IOException {
		// //ArrayList<ClueWeb> s= clueWebReader("F:\\clueweb09\\00.warc") ;
		//
		// for(int i=0;i<s.size();i++){
		// // System.out.println( s.get(i).getId()+" "+s.get(i).getContent());
		// }
		// getDocStructure(getPassage("e:\\test\\examples.txt"));
		// System.out.println(getDocStructure(getPassage("e:\\test\\examples.txt")));
		String susdir = "e:\\test\\examples.txt";

		System.out.println(getSentence(susdir));
	}

	public static ArrayList<String> getSentenceList(String fileName)
			throws IOException {
		ArrayList<String> sentences = new ArrayList<String>();
		HashMap<Integer, String> passage = getPassage(fileName);

		for (Integer key : passage.keySet()) {
			String pass = passage.get(key);
			String[] sen = pass.split("[.?!]");
			for (int i = 0; i < sen.length; i++) {
				if (sen[i].length() == 0) {
					continue;
				}
				// String s=Filter.operate(analyzer, sen[i]);
				sentences.add(sen[i].trim());
			}
		}
		return sentences;
	}		
}
