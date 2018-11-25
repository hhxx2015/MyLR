package org.haohhxx.demo.text.classify;

import org.haohhxx.util.io.HaoWriter;
import org.haohhxx.util.io.IteratorReader;

import java.util.*;
import java.util.stream.Stream;

public class ExtractTFIDFFeature2Text {

    private static String[] tokenAna(String line){
        line = line.toLowerCase();
        line = line.replaceAll("[\\,,\\.,\\(,\\)]"," ");
        String[] ls = line.split(" ");
        return ls;
    }

    private static LinkedHashMap<String,Double> loadIDFfile(String dfPath){
        LinkedHashMap<String,Double> idfMap = new LinkedHashMap<>();
        IteratorReader ir = IteratorReader.getIteratorReader(dfPath);
        for(String line : ir){
            String ls[] = line.split("\t");
            idfMap.put(ls[0], Math.log(50217.0/Double.parseDouble(ls[1])));
        }
        return idfMap;
    }

    public static void extract(String fpath, String target,HaoWriter tfidfFeatureWriter
            ,Set<String> stopSet,List<String> inUseVocabList,Map<String,Double> idfMap){
        IteratorReader.getIteratorReader(fpath)
                .forEach(line -> {

                    tfidfFeatureWriter.write(target.trim()+" ");

                    Map<String,Double> tfMap = new HashMap<>();
                    Stream.of(tokenAna(line))
                            .filter(term -> !stopSet.contains(term))
                            .forEach(term ->{
                                double tfVal = tfMap.getOrDefault(term,0.0)+1;
                                tfMap.put(term,tfVal);
                            });

                    tfMap.forEach((term,tfVal)->{

                        if (inUseVocabList.contains(term)){
                            //+1 是防止 index是0的情况出现
                            int feaIndex = inUseVocabList.indexOf(term)+1;
                            double idfVal = idfMap.getOrDefault(term, 14.0);
                            double tfidf = tfVal * idfVal;
                            tfidfFeatureWriter.write(feaIndex + ":"+String.format("%.2f",tfidf)+" ");
                        }

                    });

                    tfidfFeatureWriter.write("\n");
                });
    }

    public static void main(String[] args) {
        String negPath = "D:/data/t9.ohsumed91/neg.w.txt";
        String posPath = "D:/data/t9.ohsumed91/pos.w.txt";

        String trainFilePath = "./text/train.svm.txt";
        HaoWriter tfidfFeatureWriter = new HaoWriter(trainFilePath);

        String dfPath = "./text/df.txt";
        String stopPath = "./text/stoplist.dft";

        Set<String> stopSet = IteratorReader.getIteratorReader(stopPath).readHashSet();
        LinkedHashMap<String, Double> idfMap = loadIDFfile(dfPath);
        List<String> vocabList = new ArrayList<>(idfMap.keySet());
        vocabList.removeAll(stopSet);
        List<String> inUseVocabList = vocabList.subList(0,15000);

        extract(posPath,"1",tfidfFeatureWriter,stopSet,inUseVocabList,idfMap);
        extract(negPath,"-1",tfidfFeatureWriter,stopSet,inUseVocabList,idfMap);

        tfidfFeatureWriter.close();
    }

}
