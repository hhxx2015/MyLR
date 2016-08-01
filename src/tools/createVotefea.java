package tools;

import java.io.*;

/**
 * Created by hao on 16-7-14.
 *
 */
public class createVotefea {
    //创建21fea
    public static void main(String[] args) throws Exception {
        String infile = "/home/hao/桌面/vote_ranksvm/test/votetest/";
        String outfile="/home/hao/桌面/vote_ranksvm/test/normal20/";//willams_vote
        new File(outfile).mkdirs();

        //String outfile="../MyLR/Data/detect/feature/test/willams_vote/";
        File fs[] = new File (infile).listFiles();
        for (File f:fs){
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile+f.getName())));
            String line="";
            while((line = br.readLine())!=null){
                String ofea = line.split("#")[0];
                String longid =  line.split("#")[1];
                String vote = longid.split(" ")[1];
                //double v = Math.log(Double.parseDouble(vote));
              //  double v = Double.parseDouble(vote)/10;
              //  String newLine = ofea+"21:"+vote+" #"+longid;
                String newLine = ofea+"#"+longid;
                String qid="1";
                newLine  = newLine.replace(" 1:", " qid:"+qid+" 1:");
                newLine = newLine.replace("-1 qid:","0 qid:");
                newLine = newLine.replace("+1 qid:","1 qid:");
                bw.write(newLine);
                bw.flush();bw.newLine();
            }
            bw.close();br.close();
        }
    }


    /**
     * 用于train
     * @param args
     * @throws Exception
     */

    public static void main1(String[] args) throws Exception {
        String infile = "/home/hao/桌面/vote_ranksvm/train/votetrain/";
        String outfile="/home/hao/桌面/vote_ranksvm/train/normal20/";
        new File(outfile).mkdirs();

        //String outfile="../MyLR/Data/detect/feature/test/willams_vote/";
        File fs[] = new File (infile).listFiles();
        for (File f:fs){
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile+f.getName())));
            String line="";
            while((line = br.readLine())!=null){
                String ofea = line.split("#")[0];
                String longid =  line.split("#")[1];
                String vote = longid.split(" ")[1];
              //  double v = Math.log(Double.parseDouble(vote));
              //  double v = Double.parseDouble(vote)/10;
              //  String newLine = ofea+"21:"+v+" #"+longid;
                String newLine = ofea+"#"+longid;
                newLine = newLine.replace("-1 1:","0 1:");
                newLine = newLine.replace("+1 1:","1 1:");
                bw.write(newLine);
                bw.flush();bw.newLine();
            }
            bw.close();br.close();
        }
    }
}
