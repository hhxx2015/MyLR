package org.haohhxx.util.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author haozhenyuan
 */
public class CSVReader implements Iterable<List<String>>, Iterator<List<String>> {

    private static String ENCODE = "utf-8";
    private BufferedReader br;
    private String nextStrLine = null;

    private CSVReader(String path) throws IOException {
        br = new BufferedReader( new InputStreamReader(new FileInputStream(path), ENCODE));
        nextStrLine = br.readLine();
    }

    public static CSVReader getCSVReader(String path){
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(path);
        } catch (FileNotFoundException e) {
            System.err.println("file not exit:"+path);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvReader;
    }

    @Override
    public Iterator<List<String>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return nextStrLine!=null;
    }

    @Override
    public List<String> next() {
        StringBuilder readLine = new StringBuilder();
        boolean bReadNext = true;

        while (bReadNext) {
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = nextStrLine;
            readLine.append(strReadLine);
            // 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
            bReadNext = countChar(readLine.toString(), '"', 0) % 2 == 1;
        }
        try {
            nextStrLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fromCSVLinetoArray(readLine.toString());
    }


    /**
     *计算指定文字的个数。
     *
     * @param str 文字列
     * @param c 文字
     * @param start  开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    /**
     * 查询下一个逗号的位置。
     *
     * @param source 文字列
     * @param st  检索开始位置
     * @return 下一个逗号的位置。
     */
    private int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } else if ('"' == ch) {
                inquote = !inquote;
            }
            st++;
        }
        return st;
    }

    /**
     * 取得下一个字符串
     */
    private String nextToken(String source, int st, int nextComma) {
        StringBuffer strb = new StringBuffer();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }

    /**
     * 在字符串的外侧加双引号。如果该字符串的内部有双引号的话，把"转换成""。
     *
     * @param item  字符串
     * @return 处理过的字符串
     */
    private static String addQuote(String item) {
        if (item == null || item.length() == 0) {
            return "\"\"";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int idx = 0; idx < item.length(); idx++) {
            char ch = item.charAt(idx);
            if ('"' == ch) {
                sb.append("\"\"");
            } else {
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     *把CSV文件的一行转换成字符串数组。指定数组长度，不够长度的部分设置为null。
     */
    public String[] fromCSVLine(String source, int size) {
        ArrayList tmpArray = fromCSVLinetoArray(source);
        if (size < tmpArray.size()) {
            size = tmpArray.size();
        }
        String[] rtnArray = new String[size];
        tmpArray.toArray(rtnArray);
        return rtnArray;
    }

    /**
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。
     */
    public ArrayList<String> fromCSVLinetoArray(String source) {
        if (source == null || source.length() == 0) {
            return new ArrayList<>();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        ArrayList<String> rtnArray = new ArrayList<>();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }


    /**
     * 把字符串类型的数组转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(String[] strArray) {
        if (strArray == null) {
            return "";
        }
        StringBuilder cvsLine = new StringBuilder();
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = addQuote(strArray[idx]);
            cvsLine.append(item);
            if (strArray.length - 1 != idx) {
                cvsLine.append(',');
            }
        }
        return cvsLine.toString();
    }

    /**
     * 字符串类型的List转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(ArrayList strArrList) {
        if (strArrList == null) {
            return "";
        }
        String[] strArray = new String[strArrList.size()];
        for (int idx = 0; idx < strArrList.size(); idx++) {
            strArray[idx] = (String) strArrList.get(idx);
        }
        return toCSVLine(strArray);
    }
}
