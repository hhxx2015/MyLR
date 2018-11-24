package org.haohhxx.demo.text.classify;

import org.haohhxx.util.MapUtil;
import org.haohhxx.util.io.HaoWriter;
import org.haohhxx.util.io.IteratorReader;

import java.util.*;
import java.util.stream.Stream;

public class idfFreqExtract {

    public static String[] tokenAna(String line){
        line = line.toLowerCase();
        line = line.replaceAll("[\\,,\\.,\\(,\\)]"," ");
        String[] ls = line.split(" ");
        return ls;
    }

    public static void main(String[] args) {

        String textLinesn = "C:/data/ohsumed.91/neg.w.txt";
        String textLinesp = "C:/data/ohsumed.91/pos.w.txt";
        String dfPath = "C:\\code\\jdk8workspace\\MyLR\\src\\test\\resources\\df.txt";

        List<String> irLinesneg = IteratorReader.getIteratorReader(textLinesn).readLines();
        List<String> irLinespos = IteratorReader.getIteratorReader(textLinesp).readLines();
        irLinesneg.addAll(irLinespos);
        HashMap<String,Integer> freqMap = new HashMap<>();

        for (String line : irLinesneg){
            String[] ls = tokenAna(line);
            Stream.of(ls).distinct()
                    .map(String::trim)
                    .filter(term->term.length()>1)
                    .forEach(term -> {
                        int nub = freqMap.getOrDefault(term, 0) + 1;
                        freqMap.put(term, nub);
                    });
        }
        Map<String,Integer> sfreqMap = MapUtil.sortByValue(freqMap,true);
        HaoWriter hw = new HaoWriter(dfPath);
        for(Map.Entry<String,Integer> entSet: sfreqMap.entrySet()){
            hw.writeLine(entSet.getKey()+"\t"+entSet.getValue());
        }
        hw.close();



    }

}
