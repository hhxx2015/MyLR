package org.haohhxx.util.io;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 文件读取
 *
 * @author hao
 * @date 16-11-8
 */
public class IteratorReader implements Iterable<String>, Iterator<String> {

    private BufferedReader br;
    private String CHARSET = "UTF-8";
    private String nextLine;
    private String path;
    private Integer lineNub=-1;

    public static IteratorReader getIteratorReader(String path){
        return new IteratorReader(path);
    }
    public static IteratorReader getIteratorReader(File path){
        return new IteratorReader(path);
    }

    private IteratorReader(File f){
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(f),CHARSET));
            this.nextLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = f.getPath();
    }

    private IteratorReader(String path){
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(path),CHARSET));
            this.nextLine = br.readLine();
        } catch (IOException e) {
            System.err.println("file not exit: " + path);
            e.printStackTrace();
        }
        this.path = path;
    }

    public String readAll(){
        this.reSet();
        StringBuilder sall = new StringBuilder();
        while(this.hasNext()){
            sall.append(this.next());
        }
        return sall.toString();
    }

    public ArrayList<String> readLines(int nub){
        ArrayList<String> re = new ArrayList<>();
        for(int i =0;i<nub;i++){
            re.add(this.next());
        }
        return re;
    }

    public void reSet(){
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path),CHARSET));
            this.nextLine = br.readLine();
            lineNub=-1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readLines(){
        ArrayList<String> re = new ArrayList<>();
        while(this.hasNext()){
            re.add(this.next());
        }
        return re;
    }

    public HashSet<String> readHashSet(){
        this.reSet();
        HashSet<String> re = new HashSet<>();
        while(this.hasNext()){
            re.add(this.next());
        }
        return re;
    }

    public Integer lineNub(){
        return lineNub;
    }


    public TreeSet<String> readTreeSet(){
        this.reSet();
        TreeSet<String> re = new TreeSet<>();
        while(this.hasNext()){
            re.add(this.next());
        }
        return re;
    }

    public String getPath() {
        return path;
    }

    public IteratorReader charSet(String charset){
        this.CHARSET = charset;
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(path),CHARSET));
            this.nextLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        String line = this.nextLine;
        try {
            this.nextLine = br.readLine();
            lineNub++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    @Override
    public void remove() {

    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

}