package org.haohhxx.demo.text.classify;

import org.haohhxx.util.core.svm.SupportVectorMachine;
import org.haohhxx.util.feature.FeatureLine;
import org.haohhxx.util.feature.FeatureMatrix;
import org.haohhxx.util.feature.SparseFeatureLine;
import org.haohhxx.util.io.IteratorReader;

import java.util.List;

public class TextCLFDemo {

    public static void main(String[] args) {
        String svmFeaPath = "C:\\code\\jdk8workspace\\MyLR\\src\\test\\resources\\text\\train.svm.txt";
        FeatureMatrix allTrain = new FeatureMatrix();
        IteratorReader.getIteratorReader(svmFeaPath).readLines(3000).forEach(
                line -> allTrain.add(new SparseFeatureLine(FeatureLine.LineDataType.svm,line))
        );

        double sigma = 0.91;
        double c = 50;
        int iter = 10000;
        SupportVectorMachine.KernalClass kernal = new SupportVectorMachine.RBFKernalNoCatch(sigma, allTrain);
        SupportVectorMachine clf = new SupportVectorMachine(c, kernal);

        clf.fit(allTrain, iter);

        List<Double> pres = clf.predict(allTrain);
    }

}
