package org.haohhxx.demo.text.classify;

import org.haohhxx.util.core.svm.SupportVectorMachine;
import org.haohhxx.util.feature.FeatureMatrix;
import org.haohhxx.util.feature.SparseFeatureLine;

import java.util.List;

public class TextClassifyDemo {

    public static void main(String[] args) {
        String svmFeaPath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\text\\train.svm.txt";

        FeatureMatrix allTrain = FeatureMatrix
                .loadSampleSVMdataFile(svmFeaPath,SparseFeatureLine.class)
                .cut(0,9337);//70 min

        double sigma = 0.91;
        double c = 50;
        int iter = 10000;

        SupportVectorMachine.KernalClass kernal = new SupportVectorMachine.RBFKernalNoCatch(sigma, allTrain);
        SupportVectorMachine clf = new SupportVectorMachine(c, kernal);

        long timeStart = System.currentTimeMillis();
        clf.fit(allTrain, iter);
        long timeEnd = System.currentTimeMillis();

        System.out.println(timeEnd-timeStart);

        List<Double> pres = clf.predict(allTrain);

    }

}
