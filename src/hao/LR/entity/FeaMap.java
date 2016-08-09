package hao.LR.entity;

import java.util.HashMap;
/**
 * Created by hao on 16-8-9.
 */
public class FeaMap extends HashMap<Integer,Double>{
    public FeaMap(){
        super();
    }

    public FeaMap(int feaNub){
        super();
        for (int i=0;i<feaNub;i++){
            this.put(i,1.0);
        }
    }

    public void putB(){
        this.put(-1,1.0);
    }

}
