import org.haohhxx.util.MapUtil;
import org.haohhxx.util.ml.svm.SupportVectorMachine;
import org.haohhxx.util.eval.NDCG;
import org.haohhxx.util.matrics.feature.FeatureMatrix;
import org.haohhxx.util.matrics.feature.NormalFeatureLine;
import org.haohhxx.util.matrics.feature.RankVectorMatrixBuilder;
import org.haohhxx.util.io.IteratorReader;

import java.util.*;

public class TestRank {

    public static void main(String[] args) {

        String trainFilePath = "K:/数据/MQ2008/Fold1/train.txt";
        String valFilePath = "K:/数据/MQ2008/Fold1/vali.txt";

        FeatureMatrix trainData = FeatureMatrix.loadSampleSVMRankDataFile(trainFilePath, NormalFeatureLine.class);

        trainData = trainData.cut(0,10000);
        int iter = 1000;
        double c = 0.001;
        double sigma = 2;

//        SupportVectorMachine.LinearKernalNoCache kernalFunction = new SupportVectorMachine.LinearKernalNoCache(trainData);
        SupportVectorMachine.LinearKernal kernalFunction = new SupportVectorMachine.LinearKernal(trainData);
        SupportVectorMachine svm = new SupportVectorMachine(c, kernalFunction);

        svm.fit(trainData,iter);

        RankVectorMatrixBuilder rankVectorMatrixBuilder = FeatureMatrix.rankVectorMatrixBuilder();
        IteratorReader.getIteratorReader(valFilePath)
                .forEach(line -> rankVectorMatrixBuilder.add(line,NormalFeatureLine.class));


        double totalNDCG = 0.0;
        for (Map.Entry<Integer,FeatureMatrix> qidRank : rankVectorMatrixBuilder.entrySet()){
            int qid = qidRank.getKey();
            FeatureMatrix vectorLines = qidRank.getValue();
            List<Double> preList = svm.predict(vectorLines);
            List<Double> yList = vectorLines.getTargetList();

            Map<Integer,Double> preResult = new TreeMap<>();
            for (int i = 0; i < preList.size(); i++) {
                preResult.put(i,preList.get(i));
            }
            preResult = MapUtil.sortByValue(preResult, true);
            List<Double> pre = new ArrayList<>();
            for (int i : preResult.keySet()){
                pre.add(yList.get(i));
            }

            double ndcgScore = NDCG.evalNDCG(new ArrayList<>(pre), 5);
            totalNDCG += ndcgScore;
//            System.out.println(qid+"\t"+ndcgScore);
        }

        System.out.println(totalNDCG/rankVectorMatrixBuilder.size());

    }
}
