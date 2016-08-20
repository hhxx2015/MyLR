package hao.LR.entity;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by hao on 16-8-9.
 */
public class FeaturesMap extends TreeMap<String, Features> {
    public FeaturesMap() {
        super();
    }

    public String[] getLables() {
        ArrayList<String> lableList = new ArrayList<>();
        lableList.addAll(this.keySet());
        String[] larr = lableList.toArray(new String[lableList.size()]);
        return larr;
    }

    public void appendFeature(Feature fea) {
        String lable = fea.getLable();
        if (!this.containsKey(lable)) {
            Features fs = new Features();
            this.put(lable, fs);
        }
        Features feas = this.get(lable);
        feas.add(fea);
        this.put(lable, feas);
    }


    public Features toFeaList() {
        Features re = new Features();
        for (String key : this.keySet()) {
            re.addAll(this.get(key));
        }
        return re;
    }

//    public static void main(String[] args) {
//        Feature a = new Feature();
//        Features as = new Features();
//        as.add(a);
//    }
}

