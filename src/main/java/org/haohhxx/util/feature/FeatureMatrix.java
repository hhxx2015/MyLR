package org.haohhxx.util.feature;

import org.haohhxx.util.io.IteratorReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhenyuan_hao@163.com
 */
public class FeatureMatrix extends ArrayList<AbstractFeatureLine> {

    public FeatureMatrix(){

    }

    private FeatureMatrix(List<AbstractFeatureLine> lines){
        this.addAll(lines);
    }

    private HashMap<Integer,Double> max = new HashMap<>(256);
    private HashMap<Integer,Double> min = new HashMap<>(256);

    public FeatureMatrix cut(int start, int end){
        return new FeatureMatrix(this.subList(start,end));
    }

    /**
     * todo 极大极小特征统计
     * @param vectorLine
     * @return
     */
    @Override
    public boolean add(AbstractFeatureLine vectorLine){
//        vectorLine.forEach((index,value)->{
//            double maxOrDefault = max.getOrDefault(index,Double.MIN_VALUE);
//            if(maxOrDefault<value){
//                max.put(index,value);
//            }
//            double minOrDefault = min.getOrDefault(index,Double.MAX_VALUE);
//            if(minOrDefault>value){
//                min.put(index,value);
//            }
//        });
        return super.add(vectorLine);
    }

    public HashMap<Integer, Double> getMax() {
        return max;
    }

    public HashMap<Integer, Double> getMin() {
        return min;
    }

    public List<Double> getTargetList(){
        return this.stream().map(FeatureLine::getTarget).collect(Collectors.toList());
    }

    public static RankVectorMatrixBuilder rankVectorMatrixBuilder(){
        return new RankVectorMatrixBuilder();
    }

    public static FeatureMatrix loadSampleSVMRankDataFile(String filePath, Class featureType){
        RankVectorMatrixBuilder rankVectorMatrixBuilder = new RankVectorMatrixBuilder();
        IteratorReader.getIteratorReader(filePath).forEach(line -> rankVectorMatrixBuilder.add(line,featureType));
        return rankVectorMatrixBuilder.getVectorMatrix();
    }

}
