import org.haohhxx.util.core.LogisticRegression;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;
import org.haohhxx.util.io.IteratorReader;



public class LrTest {


    public static void main(String[] args) {

//        String fpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\a1a";
//        String fpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\train_transduction.dat";
        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\linear_data_train.csv";
        String testpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\linear_data_eval.csv";

        VectorMatrix trainData = new VectorMatrix();
        IteratorReader.getIteratorReader(trainpath)
                .readLines()
                .stream()
                .filter(line->!line.startsWith("#"))
                .forEach(line -> trainData.add(new VectorLine(VectorLine.LineDataType.csv, line)));

        VectorMatrix testData = new VectorMatrix();
        IteratorReader.getIteratorReader(testpath)
                .readLines()
                .stream()
                .filter(line->!line.startsWith("#"))
                .forEach(line -> testData.add(new VectorLine(VectorLine.LineDataType.csv, line)));


        LogisticRegression clr = new LogisticRegression(0.1);

        clr.fit(500,trainData);

        clr.predict(testData);
        for (int i : new int[]{0,10,15}){
            VectorLine vl = testData.get(i);
            double pre = clr.predict(vl);
            System.out.println(pre);
            System.out.println(vl.getTarget());
        }

    }
}
