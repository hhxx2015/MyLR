package hao.LR.util.io.iotools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * 文件读取迭代器
 * Created by hao on 16-11-8.
 */
public class IteratorReader {

    private BufferedReader br;
    private String CHARSET = "UTF-8";
    private String line;
    private String nextLine;
    private String path;

    public IteratorReader(String path){
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(path),CHARSET));
            this.nextLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    public boolean hasNextLine(){
        return nextLine != null;
    }

    public String nextLine(){
        this.line = this.nextLine;
        try {
            this.nextLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.line;
    }

    public void reSet(){
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path),CHARSET));
            this.nextLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getList(){
        this.reSet();
        ArrayList<String> re = new ArrayList<>();
        while(this.hasNextLine()){
            re.add(this.nextLine());
        }
        this.reSet();
        return re;
    }

    public HashSet<String> getHashSet(){
        this.reSet();
        HashSet<String> re = new HashSet<>();
        re.addAll(this.getList());
        return re;
    }

    public TreeSet<String> getTreeSet(){
        this.reSet();
        TreeSet<String> re = new TreeSet<>();
        re.addAll(this.getList());
        return re;
    }

    public String getPath() {
        return path;
    }

    public void setCHARSET(String charset){
        this.CHARSET = charset;
    }


}