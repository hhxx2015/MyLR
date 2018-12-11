import org.haohhxx.util.matrics.feature.FeatureLine;
import org.haohhxx.util.matrics.feature.NormalFeatureLine;
import org.haohhxx.util.matrics.feature.SparseFeatureLine;
import org.junit.Test;

public class SparseDotTest {

    @Test
    public void testSparseLineDot(){

        String l1 = "-1 1:0.005462 2:0.142857 3:0.000000 4:0.000000 5:0.005622 6:0.000000 7:0.000000 8:0.000000 9:0.000000 10:0.000000 11:0.103630 12:0.073089 13:0.000000 14:0.000000 15:0.105013 16:0.005432 17:0.368421 18:0.090909 19:0.081081 20:0.005855 21:0.682772 22:0.929076 23:0.724377 24:0.899885 25:0.037402 26:0.119345 27:0.013924 28:0.039278 29:0.000000 30:0.000000 31:0.000000 32:0.000000 33:0.000000 34:0.000000 35:0.000000 36:0.000000 37:0.673169 38:0.915633 39:0.721596 40:0.888061 41:0.111111 42:0.081967 43:0.000000 44:0.023230 45:0.020362 46:0.031524 #docid = GX000-02-6768272 inc = -1 prob = 0.0922603";
        String l2 = "-1 1:0.002892 2:0.142857 3:0.200000 4:0.000000 5:0.003213 6:0.000000 7:0.000000 8:0.000000 9:0.000000 10:0.000000 11:0.102622 12:0.159988 13:0.353890 14:0.000000 15:0.104292 16:0.009280 17:0.210526 18:0.196970 19:0.135135 20:0.009704 21:0.268950 22:0.591937 23:0.469253 24:0.288482 25:0.132378 26:0.479682 27:0.152345 28:0.220794 29:0.177140 30:0.376420 31:0.172894 32:0.267937 33:0.000000 34:0.000000 35:0.000000 36:0.000000 37:0.282201 38:0.582145 39:0.475340 40:0.294397 41:0.444444 42:0.094262 43:0.000000 44:0.003968 45:0.006787 46:0.022767 #docid = GX001-30-2544696 inc = -1 prob = 0.0895238";

        SparseFeatureLine v1 = new SparseFeatureLine(FeatureLine.LineDataType.svm, l1);
        SparseFeatureLine v2 = new SparseFeatureLine(FeatureLine.LineDataType.svm, l2);

        long timeStart = System.currentTimeMillis();
        for (int i = 0; i <100000 ; i++) {
            v1.dot(v2);
//            System.out.println();
        }
        long timeEnd = System.currentTimeMillis();

        System.out.println(timeEnd-timeStart);
    }


    public double dot(double v1[], double v2[]){
        double sum = 0.0;
        for (int i = 0; i <46 ; i++) {
            sum+=(v1[i] * v2[i]);
        }
        return sum;
    }

    @Test
    public void testNormalDot(){
        String l1 = "-1 1:0.005462 2:0.142857 3:0.000000 4:0.000000 5:0.005622 6:0.000000 7:0.000000 8:0.000000 9:0.000000 10:0.000000 11:0.103630 12:0.073089 13:0.000000 14:0.000000 15:0.105013 16:0.005432 17:0.368421 18:0.090909 19:0.081081 20:0.005855 21:0.682772 22:0.929076 23:0.724377 24:0.899885 25:0.037402 26:0.119345 27:0.013924 28:0.039278 29:0.000000 30:0.000000 31:0.000000 32:0.000000 33:0.000000 34:0.000000 35:0.000000 36:0.000000 37:0.673169 38:0.915633 39:0.721596 40:0.888061 41:0.111111 42:0.081967 43:0.000000 44:0.023230 45:0.020362 46:0.031524 #docid = GX000-02-6768272 inc = -1 prob = 0.0922603";
        String l2 = "-1 1:0.002892 2:0.142857 3:0.200000 4:0.000000 5:0.003213 6:0.000000 7:0.000000 8:0.000000 9:0.000000 10:0.000000 11:0.102622 12:0.159988 13:0.353890 14:0.000000 15:0.104292 16:0.009280 17:0.210526 18:0.196970 19:0.135135 20:0.009704 21:0.268950 22:0.591937 23:0.469253 24:0.288482 25:0.132378 26:0.479682 27:0.152345 28:0.220794 29:0.177140 30:0.376420 31:0.172894 32:0.267937 33:0.000000 34:0.000000 35:0.000000 36:0.000000 37:0.282201 38:0.582145 39:0.475340 40:0.294397 41:0.444444 42:0.094262 43:0.000000 44:0.003968 45:0.006787 46:0.022767 #docid = GX001-30-2544696 inc = -1 prob = 0.0895238";

        NormalFeatureLine v1 = new NormalFeatureLine(FeatureLine.LineDataType.svm, l1);
        NormalFeatureLine v2 = new NormalFeatureLine(FeatureLine.LineDataType.svm, l2);

        long timeStart = System.currentTimeMillis();
        for (int i = 0; i <100000 ; i++) {
            v1.dot(v2);
//            System.out.println();
        }
        long timeEnd = System.currentTimeMillis();

        System.out.println(timeEnd-timeStart);

    }


    @Test
    public void testDoubleDot(){
        double v1[] = new double[46];
        double v2[] = new double[46];

        for (int i = 0; i <46 ; i++) {
            v1[i] = Math.random();
            v2[i] = Math.random();
        }

        long timeStart = System.currentTimeMillis();
        for (int i = 0; i <100000 ; i++) {
            dot(v1,v2);
        }
        long timeEnd = System.currentTimeMillis();

        System.out.println(timeEnd-timeStart);

    }


}
