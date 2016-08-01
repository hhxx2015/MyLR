package tools;

import java.io.*;

import java.util.*;
/**
 * Created by hao on 16-7-4.
 */
public class MaxMinFeatures {
//    public static void main(String[] args) throws Exception {
//        String filePath="/home/hao/桌面/DBQA/all/all/";
//        String newPath="/home/hao/桌面/DBQA/all/allmaxmin/";
//        File fs[] = new File(filePath).listFiles();
//        for (File f:fs){
//
//            BufferedReader br = new BufferedReader(new FileReader(f));
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(newPath+f.getName())));
//            String line="";
//            double max = 100000.0;
//            double min = -100000.0;
//
//            ArrayList<Double> allList = new ArrayList<Double>();
//
//            while((line = br.readLine())!=null){
//                double scoreLine = Double.parseDouble(line);
//                allList.add(scoreLine);
//                if(min>scoreLine){
//                    min=scoreLine;
//                }
//                if(max<scoreLine){
//                    max=scoreLine;
//                }
//            }
//
//            for (double d:allList){
//                Double scnew = (d-min)/(max-min);
//                bw.write(scnew.toString());
//                bw.newLine();bw.flush();
//            }
//            bw.close();
//        }
//    }

    public static void main(String[] args) throws Exception {
        together();
    }
    public static void together() throws Exception {
        String filePath="/home/hao/桌面/DBQA/all/all/";
        String newPath="/home/hao/桌面/DBQA/all/togenormal.fea";
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(newPath)));
        File fs[] = new File(filePath).listFiles();

        double allFea[][] = new double[32][181882];

        int i = 0;
        for (File f:fs){
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line  = "";

            int j=0;
            while((line = br.readLine())!=null){
                double sc = Double.parseDouble(line);
                allFea[i][j]=sc;
                j++;
            }
            i++;
        }

        for (int j=0;j<181882;j++) {
            for (i = 0; i < 32; i++) {
                bw.write("" + allFea[i][j]);
                bw.write(" ");
            }
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

}
