import org.haohhxx.util.ml.LogisticRegression;
import org.haohhxx.util.matric.AbstractFeatureLine;
import org.haohhxx.util.matric.FeatureMatrix;
import org.haohhxx.util.matric.SparseFeatureLine;
import org.haohhxx.util.io.IteratorReader;



public class LrTest {


    public static void main(String[] args) {

//        String fpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\a1a";
//        String fpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\train_transduction.dat";
        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\linear_data_train.csv";
        String testpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\linear_data_eval.csv";

        FeatureMatrix trainData = new FeatureMatrix();
        IteratorReader.getIteratorReader(trainpath)
                .readLines()
                .stream()
                .filter(line->!line.startsWith("#"))
                .forEach(line -> trainData.add(new SparseFeatureLine(SparseFeatureLine.LineDataType.csv, line)));

        FeatureMatrix testData = new FeatureMatrix();
        IteratorReader.getIteratorReader(testpath)
                .readLines()
                .stream()
                .filter(line->!line.startsWith("#"))
                .forEach(line -> testData.add(new SparseFeatureLine(SparseFeatureLine.LineDataType.csv, line)));


        LogisticRegression clr = new LogisticRegression(0.1);

        clr.fit(trainData,500);

        clr.predict(testData);
        for (int i : new int[]{0,10,15}){
            AbstractFeatureLine vl = testData.get(i);
            double pre = clr.predict(vl);
            System.out.println(pre);
            System.out.println(vl.getTarget());
        }

    }
}
