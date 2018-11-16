package org.haohhxx.util.eval;

import java.util.*;

public class NDCG {

    public static double evalDCG(List<Double> preList){
        double dcgsum = 0;
        for (int i = 0; i <preList.size() ; i++) {
            double dcg = (Math.pow(2,preList.get(i))-1) / (Math.log(i+2)/Math.log(2));
            dcgsum += dcg;
        }
        return dcgsum;
    }

    public static double evalNDCG(List<Double> preList, int k){
        List<Double> tokKList = preList.subList(0,Math.min(k,preList.size()));
        double dcg = evalDCG(tokKList);
        tokKList.sort((o1, o2) -> {if(o1<=o2){return 1;}return -1;});
        double idcg = evalDCG(tokKList);
        if(idcg == 0){
            return 0;
        }
        return dcg / idcg;
    }

    public static void main(String[] args) {
        List<Double> preList = new ArrayList<>();
        preList.add(1.0);
        preList.add(3.0);
        preList.add(0.0);
        preList.add(0.0);
        preList.add(5.0);
        preList.add(0.0);
        preList.add(0.0);

        double ndcg = evalNDCG(preList,5);

        System.out.println(ndcg);
    }

}
