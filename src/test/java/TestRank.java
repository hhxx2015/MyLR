import org.haohhxx.util.MapUtil;
import org.haohhxx.util.core.svm.SupportVectorMachine;
import org.haohhxx.util.eval.NDCG;
import org.haohhxx.util.feature.RankVectorMatrixBuilder;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;
import org.haohhxx.util.io.IteratorReader;

import java.util.*;

public class TestRank {

    public static void main(String[] args) {

        String trainFilePath = "K:/数据/MQ2008/Fold1/vali.txt";
        String valFilePath = "K:/数据/MQ2008/Fold1/vali.txt";

        VectorMatrix trainData = VectorMatrix.loadSampleSVMRankFile(trainFilePath);

        trainData = trainData.cut(0,100);
        int iter = 100;
        double c = 10;
        double sigma = 2;

//        SupportVectorMachine.LinearKernalNoCache kernalFunction = new SupportVectorMachine.LinearKernalNoCache(trainData);
        SupportVectorMachine.LinearKernal kernalFunction = new SupportVectorMachine.LinearKernal(trainData);
        SupportVectorMachine svm = new SupportVectorMachine(c, kernalFunction);

        svm.fit(trainData,iter);

        RankVectorMatrixBuilder rankVectorMatrixBuilder = VectorMatrix.rankVectorMatrixBuilder();
        IteratorReader.getIteratorReader(valFilePath).forEach(rankVectorMatrixBuilder::add);
        //各个qid 要分开评价
        for (Map.Entry<Integer,VectorMatrix> qidRank : rankVectorMatrixBuilder.entrySet()){
            int qid = qidRank.getKey();
            VectorMatrix vectorLines = qidRank.getValue();
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

            double ndcgScore = NDCG.evalNDCG(new ArrayList<>(pre), 10);
            System.out.println(qid+"\t"+ndcgScore);
        }
    }
}
