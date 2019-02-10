package org.haohhxx.util.ml.knn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Te {

    public static void main(String[] args) throws ParseException {
        String s = "2014/03/12";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = sdf.parse(s);
        System.out.println(d);
    }
}
