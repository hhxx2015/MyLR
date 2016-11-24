package hao.LR.util.io.iotools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 文件读取迭代器
 * Created by hao on 16-11-8.
 */
public class IteratorWriter {

    private BufferedWriter bw;
    private String CHARSET = "UTF-8";
    private String line;
    private String nextLine;
    private String path;

    public IteratorWriter(String path){
        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    public void setCharSet(String cha){
        this.CHARSET = cha;
    }

    public void writeOut(String line){
        try {
            bw.write(line);
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

}