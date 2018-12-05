package org.haohhxx.util.matric;

/**
 * @author zhenyuan_hao@163.com
 */
public interface VectorLine {

    /**
     * 平方
     * @return
     */
    double pow2();

    /**
     * 对两向量进行点积
     * @param x2 x2
     * @return
     */
    double dot(VectorLine x2);

    /**
     * 按位相减
     * @param x2 x2
     * @return
     */
    VectorLine sub(VectorLine x2);

    double get(int index);

    boolean put(int index, double val);

    default boolean containsKey(int index){
        return true;
    }


}
