package org.haohhxx.demo.text.classify;

import org.haohhxx.util.ml.svm.SupportVectorMachine;
import org.haohhxx.util.matrics.FeatureMatrix;
import org.haohhxx.util.matrics.SparseFeatureLine;

public class TextClassifyDemo {

    public static void main(String[] args) {
        String svmFeaPath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\text\\train.svm.txt";

        FeatureMatrix allTrain = FeatureMatrix
                .loadSampleSVMdataFile(svmFeaPath,SparseFeatureLine.class)
//                .cut(0,9337)
                ;//70 min

        double gamma = 0.91;
        double c = 50;
        int iter = 10000;

        SupportVectorMachine.KernalClass kernal = new SupportVectorMachine.RBFKernalNoCatch(gamma, allTrain);
        SupportVectorMachine clf = new SupportVectorMachine(c, kernal);

        long timeStart = System.currentTimeMillis();
        clf.fit(allTrain, iter);
        long timeEnd = System.currentTimeMillis();

        System.out.println(timeEnd-timeStart);

        clf.save("E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\text\\svm.model.txt");
//        List<Double> pres = clf.predict(allTrain);


        // 13min c=0.0003  70min c=50  9337条数据
        // 9.5h  c=50 50000条数据  25.3h
        //
        // svm-light  19.6h    70669.80s
    }

}
