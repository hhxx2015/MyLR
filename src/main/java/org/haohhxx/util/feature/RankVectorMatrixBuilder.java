package org.haohhxx.util.feature;

import java.util.LinkedHashMap;

/**
 * @author zhenyuan_hao@163.com
 *
 * 这里使用 Link Map 保证预测时 样本的顺序和特征文件中的一致
 *
 */
public class RankVectorMatrixBuilder extends LinkedHashMap<Integer, VectorMatrix> {

    private VectorMatrix vectorMatrix = new VectorMatrix();

    public void add(String line){
        //去掉注释
        line = line.split("#")[0];
        String [] ls = line.split("\\s");
        double target = Double.parseDouble(ls[0]);
        int qid = Integer.parseInt(ls[1].split(":")[1]);
        VectorLine vectorLine  = new VectorLine(target);
        for (int i = 2; i <ls.length ; i++) {
            String[] node = ls[i].split(":");
            vectorLine.put(Integer.parseInt(node[0]),Double.parseDouble(node[1]));
        }
        VectorMatrix qidVecLines = this.getOrDefault(qid, new VectorMatrix());
        for(VectorLine qidVec:qidVecLines){
            compare(qidVec,vectorLine);
        }
        qidVecLines.add(vectorLine);
        this.put(qid,qidVecLines);
    }

    private void compare(VectorLine v1, VectorLine v2){
        VectorLine vlineNew = v1.sub(v2);
        VectorLine vlineNewS = v2.sub(v1);
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

    public VectorMatrix getVectorMatrix() {
        return vectorMatrix;
    }

}



