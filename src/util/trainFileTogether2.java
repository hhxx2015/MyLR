package util;

import java.io.*;

/**
 * @author hao : 1347261894@qq.com
 * @version 1.0
 * @date 创建时间：2016年6月11日 下午3:58:29
 * @parameter
 * @return
 */
public class trainFileTogether2 {
    public static void main(String[] args) throws Exception {
        //String trainfs = "/home/hao/桌面/vote_ranksvm/test/willams_10vote/";//willams_vote
        //String allPath = "/home/hao/桌面/vote_ranksvm/test/willams21_10vote.txt";
        String trainfs = "/home/hao/桌面/mylr/old/test/willams_10vote/";
        String allPath = "/home/hao/桌面/mylr/old/test/willams_10vote.txt";
        File fs[] = new File(trainfs).listFiles();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allPath), "UTF-8"));
        int qid = 1;
        for (File f : fs) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                String newLine = "";
                newLine = line.replace(" 1:", " qid:" + qid + " 1:");
                bw.write(newLine);
                bw.flush();
                bw.newLine();

            }
            qid++;
            br.close();
        }
        bw.close();
    }
}
