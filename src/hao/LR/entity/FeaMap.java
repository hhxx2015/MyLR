package hao.LR.entity;

import java.util.HashMap;
/**
 * Created by hao on 16-8-9.
 */
public class FeaMap extends HashMap<Integer,Double>{
    public FeaMap(){
        super();
    }

    /**
     * 初始化weight数组
     *
     * @param feaNub
     */
    public FeaMap(int feaNub){
        super();
        for (int i=0;i<feaNub;i++){
            this.put(i,1.0);
        }
    }

    /**
     * 加入分类面
     */
    public void putB(){
        this.put(-1,1.0);
    }

}
