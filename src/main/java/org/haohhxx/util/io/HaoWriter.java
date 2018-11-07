package org.haohhxx.util.io;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author hao
 * @date 2017/3/22
 *
 */
public class HaoWriter {
    private BufferedWriter bw;
    public String CHARSET = "utf-8";
    private String path;

    public HaoWriter(String path,String CHARSET,boolean append){
        this.CHARSET = CHARSET;
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path
                    ,append),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    public HaoWriter(String path,String CHARSET){
        this.CHARSET = CHARSET;
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }
    public HaoWriter(File path){
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path.getPath();
    }

    public HaoWriter(String path){
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }
    public HaoWriter(String path,boolean append){
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,append),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }
    public void write(String line){
        try {
            bw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeLine(Object line){
        try {
            bw.write(line.toString()+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOut(String line){
        try {
            bw.write(line);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeOut(Collection<String> lines){
        try {
            for(String line: lines){
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeOut(List<String> list , String path){
        HaoWriter hw = new HaoWriter(path);
        hw.writeOut(list);
        hw.close();
    }

    public static void print(Object line){
        HaoWriter hw = new HaoWriter(
                "../out.txt","utf-8",true);
        hw.write(line.toString());
        hw.close();
    }

    public static void println(Object line){
        HaoWriter hw = new HaoWriter(
                "../out.txt","utf-8",true);
        hw.writeLine(line.toString());
        hw.close();
    }


}
