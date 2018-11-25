package org.haohhxx.demo.text.classify;

import org.haohhxx.util.io.HaoWriter;
import org.haohhxx.util.io.IteratorReader;

public class PrepareData {

    public static void main(String[] args) {
        prepareOhsumedData();
    }

    public static void prepareOhsumedData() {
        String ohsPath = "D:/data/t9.ohsumed91/ohsumed.91";
        String n_formatDataPath = "D:/data/t9.ohsumed91/neg.w.txt";
        String p_formatDataPath = "D:/data/t9.ohsumed91/pos.w.txt";

        HaoWriter nhw = new HaoWriter(n_formatDataPath);
        HaoWriter phw = new HaoWriter(p_formatDataPath);

        IteratorReader ohsReader = IteratorReader.getIteratorReader(ohsPath);

        while (ohsReader.hasNext()){

            if(ohsReader.next().startsWith(".M")){
                String mTextLine = "";
                String wTextLine = "";
                mTextLine = ohsReader.next();
                while (true){
                    if(ohsReader.next().startsWith(".W")){
                        break;
                    }
                }
                wTextLine = ohsReader.next();
                if (mTextLine.contains("Cardiovascular Diseases")){
                    phw.writeLine(wTextLine);
                }else {
                    nhw.writeLine(wTextLine);
                }
            }
        }
        nhw.close();phw.close();
    }

}
