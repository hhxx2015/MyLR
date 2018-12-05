package org.haohhxx.util.matric;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

/**
 * @author zhenyuan_hao@163.com
 *
 * 这里使用 Link Map 保证预测时 样本的顺序和特征文件中的一致
 *
 */
public class RankVectorMatrixBuilder extends LinkedHashMap<Integer, FeatureMatrix> {

    private FeatureMatrix vectorMatrix = new FeatureMatrix();

    public void add(String line,Class featureLine){
        //去掉注释
        line = line.split("#")[0];
        String [] ls = line.split("\\s");
        int feanub = ls.length-1;
        double target = Double.parseDouble(ls[0]);
        int qid = Integer.parseInt(ls[1].split(":")[1]);
        AbstractFeatureLine vectorLine = null;
        try {
            switch (featureLine.getName()){
                case "org.haohhxx.util.matric.NormalFeatureLine":
                    vectorLine = (AbstractFeatureLine)featureLine.getDeclaredConstructor(double.class, int.class).newInstance(target, feanub);
                    break;
                case "org.haohhxx.util.matric.SparseFeatureLine":
                    vectorLine = (AbstractFeatureLine)featureLine.getDeclaredConstructor(double.class, int.class).newInstance(target, feanub);
                    break;
                default:
                    vectorLine = (AbstractFeatureLine)featureLine.getDeclaredConstructor(double.class, int.class).newInstance(target, feanub);
                    break;
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        for (int i = 2; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            assert vectorLine != null;
            vectorLine.put(Integer.parseInt(node[0]),Double.parseDouble(node[1]));
        }
        FeatureMatrix qidVecLines = this.getOrDefault(qid, new FeatureMatrix());
        for(AbstractFeatureLine qidVec:qidVecLines){
            compare(qidVec,vectorLine);
        }
        qidVecLines.add(vectorLine);
        this.put(qid,qidVecLines);
    }

    private void compare(AbstractFeatureLine v1, AbstractFeatureLine v2){
        AbstractFeatureLine vlineNew = v1.sub(v2);
        AbstractFeatureLine vlineNewS = v2.sub(v1);
        if(v1.getTarget() > v2.getTarget()){
            vlineNew.setTarget(1);
            vlineNewS.setTarget(-1);
        }else if(v1.getTarget() < v2.getTarget()){
            vlineNew.setTarget(-1);
            vlineNewS.setTarget(1);
        }
        vectorMatrix.add(vlineNew);
        vectorMatrix.add(vlineNewS);
    }

    public FeatureMatrix getVectorMatrix() {
        return vectorMatrix;
    }

}



