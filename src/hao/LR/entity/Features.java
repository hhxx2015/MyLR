package hao.LR.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by hao on 16-8-9.
 */
public class Features extends ArrayList<Feature> {

    public Features() {
        super();
    }

    public void setLableValue(Double value) {
        for (int i = 0; i < this.size(); i++) {
            Feature nf = this.get(i);
            nf.setLableValue(value);
            this.remove(i);
            this.add(i, nf);
        }
    }

}

