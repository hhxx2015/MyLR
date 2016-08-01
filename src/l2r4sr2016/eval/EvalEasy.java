package l2r4sr2016.eval;

import l2r4sr2016.iotools.ReadFile;
import l2r4sr2016.iotools.writeFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class EvalEasy {
	static String ansDir = "/home/hao/桌面/分类任务/ans/ourownanswerlongid/";
	static String refansDir = "/home/hao/桌面/分类任务/ans/refans/williams/";
	static String oracleDir = "/home/hao/桌面/分类任务/ans/refans/oracle/";

	public static void main(String[] args) throws IOException {//featureQueryString featureAll featureNeighbor featurePos
	// String ourResultDir ="/home/hao/文档/mylr/longid/featureAll/";
	//	String ourResultDir ="/home/hao/workspace/UseLDA_py/data/longidresult/";
		//String ourResultDir ="/home/hao/workspace/MyLR/Data/detect/feature/ranklongidre/test/";
	//	String ourResultDir ="/home/hao/桌面/vote_ranksvm/longid/willams_10vote/";//willams_10vote normal20
		String ourResultDir ="/home/hao/桌面/mylr/votelongidre/willams_10vote_votelable/";//vote_willams_10vote
	//	String ourResultDir ="/home/hao/桌面/mylr/longid/normal20/";
	// String ourResultDir ="/home/hao/文档/lr/longid/featurePos/";/
		//String ourResultDir ="/home/hao/桌面/分类任务/svml/longid/featureQueryString/";

	//	evalResultByOracle(ourResultDir);		
		File fs[] = new File(ourResultDir).listFiles();
		for (File f:fs) {
			System.out.print(f.getName().replaceAll("[top,score]","").replace("_","\t")+"\t");
			evalResultByOracle(f.getPath()+"/");		
		}
	//		System.out.println(ourResultDir);
	}

	// ������Ľ����Ŀ¼�����۽��
	public static String evalResultByOracle(String ourResultDir)
			throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String[] susfile = new File(ourResultDir).list();
		double sumP = 0.0;
		double sumR = 0.0;
		double sumF = 0.0;

		//System.out.println(ourResultDir);
		for (int i = 0; i < susfile.length; i++) {
			//System.out.println(susfile[i]);
			String susNum = susfile[i].replace("suspicious-document", "")
					.replace("-batch1.txt", "");

			ArrayList<String> dsrcList = ReadFile.getFileConList(ansDir
					+ susfile[i].replace("-batch1.txt", ".txt"));
			ArrayList<String> ourLongidList = ReadFile
					.getFileConList(ourResultDir + susfile[i]);
			//System.out.println(ourLongidList);
			HashMap<String, Double> dsrcMap = getAnsMap(dsrcList);
			ArrayList<String> refList = ReadFile.getFileConList(refansDir
					+ susfile[i]);

			HashSet<String> dpieSrcSet = ReadFile.readFileToSet(ansDir
					+ susfile[i]);
			HashSet<String> oracleSet = ReadFile.readFileToSet(oracleDir
					+ susfile[i]);
			dpieSrcSet.addAll(oracleSet);
			double numP = 0.0;
			double numR = 0.0;

			// precision
			for (String ourlid : ourLongidList) {
				if (dpieSrcSet.contains(ourlid)) {
					numP++;
				}
				for (String lid2 : refList) {
					// System.out.println("***"+lid2);
					String[] ans = lid2.split(" ");
					if (ourlid.equals(ans[0]) || ourlid.equals(ans[1])) {
						dsrcMap.put(ans[0], 1.0);
					}
				}
			}
			for (String r : dsrcMap.keySet()) {
				if (dsrcMap.get(r) == 1.0) {
					numR += 1.0;
				}
			}
			double prec = 0.0;
			if (ourLongidList.size() > 0) {
				prec = numP / ourLongidList.size();
			}
			double recall = 0.0;
			if (dsrcList.size() > 0) {
				recall = numR / dsrcList.size();
			}
			double fscore = 0.0;
			if ((prec + recall) != 0.0) {
				fscore = 2 * prec * recall / (prec + recall);
			}
			list.add(susNum + " " + fscore + " " + prec + " " + recall);
			sumR += recall;
			sumP += prec;
			sumF += fscore;
		//	System.out.println(susNum + " " + fscore + " " + prec + " "+ recall);
		}
		
	        String s=sumF / list.size() + "\t" + sumP / list.size() + "\t"+ sumR / list.size();
	        System.out.println(s);
        //System.out.println(list.size());
		return s;
	}

	// ����һ�����ݣ�����ÿ�еļ������
	public static double[] evalResultOneLineByOracle(
			HashSet<String> dpieSrcSetori, HashSet<String> oracleSet,
			ArrayList<String> dsrcList, ArrayList<String> refList,
			ArrayList<String> ourLongidList) throws IOException {
		HashSet<String> dpieSrcSet = new HashSet<String>();
		for (String id : dpieSrcSetori) {
			dpieSrcSet.add(id);
		}
		dpieSrcSet.addAll(oracleSet);
		HashMap<String, Double> dsrcMap = getAnsMap(dsrcList);

		double numP = 0.0;
		double numR = 0.0;

		// precision
		for (String ourlid : ourLongidList) {
			if (dpieSrcSet.contains(ourlid)) {
				numP++;
			}
			for (String lid2 : refList) {
				// System.out.println("***"+lid2);
				String[] ans = lid2.split(" ");
				if (ourlid.equals(ans[0]) || ourlid.equals(ans[1])) {
					dsrcMap.put(ans[0], 1.0);
				}
			}
		}
		for (String r : dsrcMap.keySet()) {
			if (dsrcMap.get(r) == 1.0) {
				numR += 1.0;
			}
		}
		double prec = 0.0;
		if (ourLongidList.size() > 0) {
			prec = numP / ourLongidList.size();
		}
		double recall = 0.0;
		if (dsrcList.size() > 0) {
			recall = numR / dsrcList.size();
		}
		double fscore = 0.0;
		if ((prec + recall) != 0.0) {
			fscore = 2 * prec * recall / (prec + recall);
		}
		double[] score = { fscore, prec, recall };
		return score;
	}

	private static HashMap<String, Double> getAnsMap(ArrayList<String> dsrcList) {
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (String l : dsrcList) {
			map.put(l, 0.0);
		}
		return map;
	}

	public static void evalResult() throws IOException {
		String output = "F:\\l2rfinal\\format\\test\\2fold\\maxmin\\veri\\";
		new File(output).mkdirs();

		String root = "F:\\l2rfinal\\result\\test\\2fold\\maxmin\\veri\\";
		// String root =
		// "F:\\l2rfinal\\result\\test\\10fold\\maxmin\\fscore-allbest\\";

		String[] sub = new File(root).list();
		ArrayList<String> avglist = new ArrayList<String>();
		for (String subdir : sub) {
			String ourResultDir = root + subdir + "\\";
			ArrayList<String> list = evalResult(ourResultDir);
			avglist.add(subdir + " " + getAvg(list));
			writeFile.writeResult(list, output + subdir + ".txt");
		}
		writeFile.writeResult(avglist, output + "avgScore.txt");
	}

	// ������Ľ����Ŀ¼�����۽��
	public static ArrayList<String> evalResult(String ourResultDir)
			throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String[] susfile = new File(ourResultDir).list();
		double sumP = 0.0;
		double sumR = 0.0;
		double sumF = 0.0;
		for (int i = 0; i < susfile.length; i++) {
			// System.out.println(susfile[i]);
			String susNum = susfile[i].replace("suspicious-document", "")
					.replace("-batch1.txt", "");

			ArrayList<String> dsrcList = ReadFile.getFileConList(ansDir
					+ susfile[i].replace("-batch1.txt", ".txt"));
			ArrayList<String> ourLongidList = ReadFile
					.getFileConList(ourResultDir + susfile[i]);

			ArrayList<String> refAnsList = ReadFile.getFileConList(refansDir
					+ susfile[i]);
			HashMap<String, HashSet<String>> refAnsmap = getRefAnsMap(refAnsList);
			double[] score = evalOneLine(ourLongidList, dsrcList, refAnsList,
					refAnsmap);
			list.add(susNum + " " + score[0] + " " + score[1] + " " + score[2]);
			sumR += score[0];
			sumP += score[1];
			sumF += score[2];
			System.out.println(susNum + " " + score[0] + " " + score[1] + " "
					+ score[2]);
		}
		System.out.println(sumR / list.size() + " " + sumP / list.size() + " "
				+ sumF / list.size());
		return list;
	}

	// ����Ϊfeature�ļ���outputΪ����feature�ļ���ÿһ�У�Ŀ��������ÿһ�е�ǰn�����������recall��precision��fscore
	public static void evalResult(String inputDir, String outputDir, int topnum)
			throws IOException {
		String[] susfile = new File(inputDir).list();
		String[] has = new File(outputDir).list();
		ArrayList<String> hasList = new ArrayList<String>();
		for (int i = 0; i < has.length; i++) {
			hasList.add(has[i]);
		}
		for (int i = 0; i < susfile.length; i++) {
			// System.out.println(susfile[i]);
			ArrayList<String> wlist = new ArrayList<String>();
			if (!hasList.contains(susfile[i])) {
				String susNum = susfile[i].replace("suspicious-document", "")
						.replace("-batch1.txt", "");
				ArrayList<String> rlist = ReadFile.getFileConList(inputDir
						+ susfile[i]);
				ArrayList<String> dsrcList = ReadFile.getFileConList(ansDir
						+ "suspicious-document" + susNum + ".txt");

				int num = rlist.size();
				int n = 1;
				ArrayList<String> refAnsList = ReadFile
						.getFileConList(refansDir + susfile[i]);
				HashMap<String, HashSet<String>> refAnsmap = getRefAnsMap(refAnsList);
				for (String line : rlist) {
					// System.out.println(n++ + "/" + num);
					String[] str = line.split("longid=");
					double[] score = { 0.0, 0.0, 0.0 };
					if (str.length == 2 && str[1].length() > 0) {
						ArrayList<String> ourLongidList = getLongidList(str[1],
								topnum);
						// System.out.println(ourLongidList);

						score = evalOneLine(ourLongidList, dsrcList,
								refAnsList, refAnsmap);
						// wlist.add(score[0] + "\t" + score[1] + "\t" +
						// score[2]
						// + "\t" + str[0] + "\tlongid=" + str[1]);
					}

					String[] str1 = line.split("qid");
					wlist.add(score[0] + "\t" + score[1] + "\t" + score[2]
							+ "\tqid" + str1[1]);
				}
			}
			writeFile.writeResult(wlist, outputDir + susfile[i]);
		}
	}

	private static double[] evalOneLine(ArrayList<String> ourAnsList,
			ArrayList<String> ansList, ArrayList<String> refAnsList,
			HashMap<String, HashSet<String>> refAnsmap) throws IOException {
		double score[] = { 0.0, 0.0, 0.0 };

		double dpieret_dsrc = 0.0;
		double dret_dpiesrc = 0.0;

		// recall
		dpieret_dsrc = getInsectRecall(refAnsmap, refAnsList, ourAnsList);
		if (refAnsmap.size() > 0) {
			score[0] = dpieret_dsrc / refAnsmap.size();
		}

		// precision
		dret_dpiesrc = getInsectPrecision(ansList, refAnsList, ourAnsList);

		if (ourAnsList.size() > 0) {
			score[1] = dret_dpiesrc * 1.0 / ourAnsList.size();
		}

		// fscore
		if ((score[0] + score[1]) > 0) {
			score[2] = 2 * score[0] * score[1] / (score[0] + score[1]);
		}

		return score;
	}

	private static HashMap<String, HashSet<String>> getRefAnsMap(
			ArrayList<String> refAnsList) {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		for (String ref : refAnsList) {
			String[] str = ref.split(" ");
			if (map.containsKey(str[0])) {
				HashSet<String> set = map.get(str[0]);
				set.add(str[1]);
				map.put(str[0], set);
			} else {
				HashSet<String> set = new HashSet<String>();
				set.add(str[1]);
				map.put(str[0], set);
			}
		}
		return map;
	}

	private static double getInsectRecall(HashMap<String, HashSet<String>> map,
			ArrayList<String> refAnsList, ArrayList<String> ourAnsList) {
		double num = 0.0;
		for (String ans : map.keySet()) {
			for (String ourAns : ourAnsList) {
				if (ourAns.length() > 0) {
					if (ans.equals(ourAns) || map.get(ans).contains(ourAns)) {
						num += 1.0;
						break;
					}
				}
			}
		}
		return num;
	}

	private static double getInsectPrecision(ArrayList<String> ansList,
			ArrayList<String> refAnsList, ArrayList<String> ourAnsList) {
		double num = 0.0;
		HashSet<String> set = new HashSet<String>();
		for (String ref : refAnsList) {
			String[] str = ref.split(" ");
			set.add(str[0]);
			set.add(str[1]);
		}
		for (String ourAns : ourAnsList) {
			if (set.contains(ourAns) && ourAns.length() > 0) {
				num += 1.0;
			}
		}
		return num;
	}

	private static ArrayList<String> getLongidList(String longid, int topnum) {
		ArrayList<String> list = new ArrayList<String>();
		String[] longidArr = longid.split(" ");
		for (int i = 0; i < Math.min(topnum, longidArr.length); i++) {
			list.add(longidArr[i]);
		}
		return list;
	}

	private static String getAvg(ArrayList<String> list) {
		double recall = 0.0;
		double precision = 0.0;
		double fscore = 0.0;
		double num = 0.0;
		for (String line : list) {
			if (line.length() > 5) {
				String[] str = line.split(" ");
				recall += Double.parseDouble(str[1]);
				precision += Double.parseDouble(str[2]);
				fscore += Double.parseDouble(str[3]);
				num += 1.0;
			}
		}

		return recall / num + " " + precision / num + " " + fscore / num;
	}
}
