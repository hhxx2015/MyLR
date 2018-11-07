import org.haohhxx.util.core.LogisticRegression;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;
import org.haohhxx.util.io.IteratorReader;



public class LrTest {


    public static void main(String[] args) {

        String fpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\a1a";

        VectorMatrix vm = new VectorMatrix();
        IteratorReader.getIteratorReader(fpath)
                .readLines()
                .forEach(line -> vm.add(new VectorLine(line)));

        LogisticRegression clr = new LogisticRegression();

        clr.fit(500,vm);

        double pre = clr.predict(vm.get(15));
        System.out.println(pre);
        System.out.println(vm.get(15).getTarget());

    }
}
