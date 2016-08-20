package hao.LR.util.io;

import hao.LR.core.LR_hash;
import hao.LR.entity.FeaMap;
import hao.LR.entity.Feature;
import hao.LR.entity.Features;
import hao.LR.entity.FeaturesMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LoadFeatures {
    public static void main(String[] argbs) throws IOException, InterruptedException {

        double alpha = 0.1;
        int feaNub = 13;
        boolean b = true;
        int itea = 500;

        //ExecutorService threadPool = Executors.newCachedThreadPool();
        //ExecutorService threadPool = Executors.newFixedThreadPool(10);
        //String path="/home/hao/IdeaProjects/MyLR/Data/DataSet/4k2_far.txt";
        String path = "/home/hao/IdeaProjects/MyLR/Data/DataSet/wine.txt";

        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line = "";
        FeaturesMap feaMap = new FeaturesMap();

        while ((line = br.readLine()) != null) {
            String[] ls = line.split(",");
            FeaMap fm = new FeaMap();
            for (int i = 1; i < ls.length - 1; i++) {
                fm.put((i - 1), Double.parseDouble(ls[i]));
            }
            Feature fea = new Feature(ls[0], fm);
            feaMap.appendFeature(fea);
        }

        String lablearr[] = feaMap.getLables();

        System.out.println(lablearr.length);
        LR_hash[][] modelIndex = new LR_hash[lablearr.length][lablearr.length];

        for (int i = 0; i < lablearr.length; i++) {
            for (int j = 0; j < lablearr.length; j++) {
                if (j == i) {

                } else {
                    Features feas1 = feaMap.get(lablearr[i]);
                    Features feas2 = feaMap.get(lablearr[j]);

                    Features trainFea = new Features();
                    feas1.setLableValue(1.0);
                    feas2.setLableValue(0.0);


                    trainFea.addAll(feas1);
                    trainFea.addAll(feas2);

                    int ii = i, jj = j;
//					threadPool.execute(new Runnable() {
//						@Override
//						public void run() {
//							LR_hash lrc= new LR_hash(feaNub,alpha,b);
//							lrc.train(itea,trainFea);
//							modelIndex[ii][jj] = lrc;
//						}
//					});

                    LR_hash lrc = new LR_hash(feaNub, alpha, b);
                    lrc.train(itea, trainFea);
                    modelIndex[i][j] = lrc;
                }
            }
        }
        Thread.sleep(2000);

        for (int i = 0; i < lablearr.length; i++) {
            for (int j = 0; j < lablearr.length; j++) {
                if (j != i) {
                    System.out.println(modelIndex[i][j].getWeight());
                }
            }
        }


//		threadPool.shutdown();
//		System.out.println(threadPool.isTerminated());
//		w1:while(threadPool.isTerminated()){
        Features testFea = feaMap.toFeaList();
        ArrayList<String> classArr = new ArrayList<>();
        for (Feature ofea : testFea) {
            FeaMap fm = ofea.getFeaMap();
            int classLable = -1;
            int rightNub = 0;
            for (int i = 0; i < lablearr.length; i++) {
                int right = 0;
                for (int j = 0; j < lablearr.length; j++) {
                    if (j == i) {

                    } else {
                        double sc = modelIndex[i][j].classify(fm);
//							System.out.println(sc);
//							System.out.println(modelIndex[i][j].getWeight());
                        if (sc > 0.5) {
                            right++;
                        }
                    }
                }
                if (right > rightNub) {
                    rightNub = right;
                    classLable = i;
                }
            }
            classArr.add(lablearr[classLable]);
        }

        System.out.println("aaaaaa" + classArr);
        //		break w1;
        //	}

    }
}
