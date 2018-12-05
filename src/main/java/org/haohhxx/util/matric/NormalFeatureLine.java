package org.haohhxx.util.matric;


/**
 * @author zhenyuan_hao@163.com
 */
public class NormalFeatureLine extends AbstractFeatureLine{

    private double target;
    private double[] featureLine;
    private int featureNub;


    public NormalFeatureLine(LineDataType lineDataType, String line){
        switch (lineDataType){
            case svm: this.loadSVMLine(line);break;
            case csv: this.loadCSVLine(line);break;
            default:System.out.println("wrong data type!");
        }
    }

    public NormalFeatureLine(int featureNub){
        this.featureNub = featureNub;
        this.featureLine = new double[featureNub];
    }

    public NormalFeatureLine(double target, int featureNub){
        this.target=target;
        this.featureNub = featureNub;
        this.featureLine = new double[featureNub];
    }

    private void loadCSVLine(String csvLine){
        String [] ls = csvLine.split(",");
        this.featureNub = ls.length-1;
        featureLine = new double[this.featureNub];
        this.target = Double.parseDouble(ls[0]);
        for (int i = 1; i <ls.length ; i++) {
            featureLine[i-1] = Double.parseDouble(ls[i]);
        }
    }

    private void loadSVMLine(String svmLine){
        svmLine = svmLine.split("#")[0].trim();
        String [] ls = svmLine.split("\\s");
        this.target = Double.parseDouble(ls[0]);

        this.featureNub = ls.length-1;
        featureLine = new double[this.featureNub];
        for (int i = 1; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            featureLine[Integer.parseInt(node[0])] = Double.parseDouble(node[1]);
        }
    }

    @Override
    public boolean put(int featureNodeIndex, double val){
        featureLine[featureNodeIndex] = val;
        return true;
    }

    @Override
    public double get(int featureNodeIndex){
        return featureLine[featureNodeIndex];
    }

    @Override
    public AbstractFeatureLine sub(VectorLine sx2){
        NormalFeatureLine vectorLine = new NormalFeatureLine(featureNub);
        for (int featureNodeIndex = 0; featureNodeIndex < this.featureNub ; featureNodeIndex++) {
            double sub = this.get(featureNodeIndex) -  sx2.get(featureNodeIndex);
            vectorLine.put(featureNodeIndex,sub);
        }
        return vectorLine;
    }


    @Override
    public double dot(VectorLine x2){
        double sum = 0.0;
        for (int featureNodeIndex = 0; featureNodeIndex < this.featureNub ; featureNodeIndex++) {
            sum += this.get(featureNodeIndex) *  x2.get(featureNodeIndex);
        }
        return sum;
    }

    /**
     * 平方
     * @return double
     */
    @Override
    public double pow2(){
        double sum = 0.0;
        for (int featureNodeIndex = 0; featureNodeIndex < this.featureNub ; featureNodeIndex++) {
            sum += Math.pow(this.get(featureNodeIndex), 2);
        }
        return sum;
    }

    @Override
    public double getTarget() {
        return target;
    }

    @Override
    public void setTarget(double target) {
        this.target = target;
    }


}



