package org.haohhxx.demo.text.classify;

import org.haohhxx.util.io.HaoWriter;
import org.haohhxx.util.io.IteratorReader;

import java.util.*;
import java.util.stream.Stream;

public class ExtractBERTFeature2Text {


    public static void main(String[] args) {
        String negPath = "D:/data/t9.ohsumed91/neg.bert.txt";
        String posPath = "D:/data/t9.ohsumed91/pos.bert.txt";

        String trainFilePath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\text\\train.bert.svm.txt";
        HaoWriter featureWriter = new HaoWriter(trainFilePath);

        IteratorReader.getIteratorReader(posPath).forEach(line -> {
                featureWriter.writeOut("1 ");
                String[] ls = line.split(" ");
                for (int i = 1; i <=ls.length ; i++) {
                    featureWriter.writeOut(i+":"+ls[i-1]+" ");
                }
                featureWriter.writeOut("\n");
        });

        IteratorReader.getIteratorReader(negPath).forEach(line -> {
            featureWriter.writeOut("-1 ");
            String[] ls = line.split(" ");
            for (int i = 1; i <=ls.length ; i++) {
                featureWriter.writeOut(i+":"+ls[i-1]+" ");
            }
            featureWriter.writeOut("\n");
        });

        featureWriter.close();
    }

}
