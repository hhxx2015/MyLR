package org.haohhxx.util.core;

import com.google.common.collect.Sets;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author zhenyuan_hao@163.com
 */
public class SupportVectorMachine {

    public enum KernalFunction{
        RBF()

    }
    private double gamma = 0.08;

    private double bias = 0.0;
    private double c = 0.1;
    private int n = 0;
    private double tolerance;
    private double eps;

    private double[] alpha = null;
    private double[] errorCache = null;
    private double[][] dotDache = null;
    private double[][] kernel = null;

    private final Random random;

    private VectorMatrix vectorMatrix;


    public SupportVectorMachine(double gamma, double c){
        this.random = new Random();
        this.gamma = gamma;
        this.tolerance = 0.001;
        this.eps= 0.001;
        this.c = c;
    }

    private void fitInit(VectorMatrix vectorMatrix){
        this.vectorMatrix = vectorMatrix;
        this.n = vectorMatrix.size();
        this.alpha = new double[n];
        this.errorCache = new double[n];
        this.dotDache = new double[n][n];
        this.kernel = new double[n][n];

        //初始化点积dotCache
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                this.dotDache[i][j] = dot(vectorMatrix.get(i), vectorMatrix.get(j));
            }
        }

        //初始化核函数
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.kernel[i][j] = kernelFunction(i, j);
            }
        }
    }

    public void fit(VectorMatrix vectorMatrix, int epoch){

        this.fitInit(vectorMatrix);

        int epochNub = 0;
        int numChanged = 0;
        boolean examineAll = true;

        //当迭代次数大于maxIter或者 所有样本中没有alpha对改变时，跳出循环
        while((epochNub < epoch) && (numChanged > 0 || examineAll)){
            numChanged = 0;

            if (examineAll) {
                //循环检查所有样本
                for (int i = 0; i < n; i++) {
                    if (examineExample(i)) {
                        numChanged ++;
                    }
                }
            }else{
                //只检查非边界样本
                for (int i = 0; i < n; i++) {
                    if (alpha[i] != 0 && alpha[i] != c) {
                        if (examineExample(i)) {
                            numChanged ++;
                        }
                    }
                }
            }
            epochNub ++;
            if (examineAll) {
                examineAll = false;
            }else if (numChanged == 0) {
                examineAll = true;
            }
        }

    }


    /**
     * 预测函数
     */
    public double predict(VectorLine vectorLine){
        double sum = 0.0;
        for (int j = 0; j < n; j++) {
            sum += alpha[j] * vectorMatrix.get(j).getTarget() * kernelFunction(vectorMatrix.get(j), vectorLine);
        }
        sum += bias;
        return sum;
    }

    private double kernelFunction(VectorLine x1,VectorLine x2){
        double i1i2 = this.dot(x1,x2);
        double i1i1 = this.dot(x1,x1);
        double i2i2 = this.dot(x2,x2);
        return Math.exp(- (i1i1 + i2i2 - 2 * i1i2) / (2 * Math.pow(gamma,2)));
    }

    /**
     * 训练时的核函数 exp(-gamma*|u-v|^2)
     * @param i1 i1
     * @param i2 i2
     * @return
     */
    private double kernelFunction(int i1, int i2){
        return Math.exp(- (Math.pow(dotDache[i1][i1],2) + Math.pow(dotDache[i2][i2],2) - 2 * dotDache[i1][i2]) / (2 * Math.pow(gamma,2)));
    }

    private boolean examineExample(int i1){
        double y1 = vectorMatrix.get(i1).getTarget();
        double alpha1 = alpha[i1];
        double E1 = 0;

        if (0 < alpha1 && alpha1 < c) {
            E1 = errorCache[i1];
        }else{
            E1 = calcError(i1);
        }

        /*
         * yi*f(i) >= 1 and alpha == 0 (正确分类)
         * yi*f(i) == 1 and 0<alpha < C (在边界上的支持向量)
         * yi*f(i) <= 1 and alpha == C (在边界之间)
         */
        double r1 = y1 * E1;
        if ((r1 < -tolerance && alpha1 < c) || (r1 > tolerance && alpha1 > 0)) {
            //选择 E1 - E2 差最大的两点
            int i2 = this.getMaxJ(E1);
            if (i2 >= 0) {
                if (takeStep(i1, i2)) {
                    return true;
                }
            }

            //先选择 0 < alpha < C的点
            int k0 = randomSelect(i1);
            for (int k = k0; k < n + k0; k++) {
                i2 = k % n;
                if (0 < alpha[i2] && alpha[i2] < c) {
                    if (takeStep(i1, i2)) {
                        return true;
                    }
                }
            }

            //如果不符合，再遍历全部点
            k0 = randomSelect(i1);
            for (int k = k0; k < n + k0; k++) {
                i2 = k % n;
                if (takeStep(i1, i2)) {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * 内循环，选择最大步长的两个点进行优化
     * @param i1 i1
     * @param i2 i2
     * @return boolean
     */
    private boolean takeStep(int i1, int i2){
        if (i1 == i2){
            return false;
        }

        double alpha1 = alpha[i1];
        double alpha2 = alpha[i2];
        double y1 = vectorMatrix.get(i1).getTarget();
        double y2 = vectorMatrix.get(i2).getTarget();
        double E1 = 0;
        double E2 = 0;
        double s = y1 * y2;
        double a1, a2; //新的a
        double L, H;

        if (0 < alpha1 && alpha1 < c) {
            E1 = errorCache[i1];
        }else{
            E1 = calcError(i1);
        }

        if (0 < alpha2 && alpha2 < c) {
            E2 = errorCache[i2];
        }else{
            E2 = calcError(i2);
        }

        if (y1 != y2) {
            L = Math.max(0, alpha2 - alpha1);
            H = Math.min(c, c + alpha2 - alpha1);
        }else{
            L = Math.max(0, alpha1 + alpha2 - c);
            H = Math.min(c, alpha1 + alpha2);
        }
        if (L >= H) {
            return false;
        }

        double k11 = kernel[i1][i1];
        double k12 = kernel[i1][i2];
        double k22 = kernel[i2][i2];

        double eta = 2 * k12 - k11 - k22;
        //根据不同情况计算出a2
        if (eta < 0) {
            //计算非约束条件下的最大值
            a2 = alpha2 - y2 * (E1 - E2) / eta;

            //判断约束的条件
            if (a2 < L) {
                a2 = L;
            } else if (a2 > H) {
                a2 = H;
            }
        }else {
            double c1 = eta / 2;
            double c2 = y2 * (E1 - E2) - eta * alpha2;

            //Lobj和Hobj可以根据自己的爱好选择不同的函数
            double Lobj = c1 * L * L + c2 * L;
            double Hobj = c1 * H * H + c2 * H;

            if (Lobj > Hobj + eps) {
                a2 = L;
            }else if (Lobj < Hobj - eps) {
                a2 = H;
            } else {
                a2 = alpha2;
            }
        }

        if (Math.abs(a2 - alpha2) < eps * (a2 + alpha2 + eps)) {
            return false;
        }

        //通过a2来更新a1
        a1 = alpha1 + s * (alpha2 - a2);

        if (a1 < 0) {
            a2 += s * a1;
            a1 = 0;
        }else if (a1 > c) {
            a2 += s * (a1 - c);
            a1 = c;
        }

        //update threshold b;
        double b1 = bias - E1 - y1 * (a1 - alpha1) * k11 - y2 * (a2 - alpha2) * k12;
        double b2 = bias - E2 - y1 * (a1 - alpha1) * k12 - y2 * (a2 - alpha2) * k22;

        double bNew = 0;
//		double deltaB = 0;
        if (0 < a1 && a1 < c) {
            bNew = b1;
        }else if (0 < a2 && a2 < c) {
            bNew = b2;
        }else {
            bNew = (b1 + b2) / 2;
        }
//		deltaB = bNew - this.b; //b的增量
        this.bias = bNew;

        //update error cache
        this.errorCache[i1] = calcError(i1);
        this.errorCache[i2] = calcError(i2);

        //store a1, a2 in alpha array
        alpha[i1] = a1;
        alpha[i2] = a2;

        return true;
    }


    private int getMaxJ(double E1){
        int i2 = -1;
        double tmax = 0.0;
        for (int k = 0; k < n; k++) {
            if (0 < alpha[k] && alpha[k] < c) {
                double E2 = errorCache[k];
                double tmp = Math.abs(E2 - E1);
                if (tmp > tmax) {
                    tmax = tmp;
                    i2 = k;
                }
            }
        }
        return i2;
    }


    /**
     * 对两向量进行点积
     * @param x1 x1
     * @param x2 x2
     * @return
     */
    private double dot(VectorLine x1, VectorLine x2){
        double sum = 0.0;
        Set<Integer> intersectionNodeSet = Sets.intersection(x1.keySet(), x2.keySet());
        for (Integer x1FeatureNodeIndex : intersectionNodeSet){
            sum += x1.get(x1FeatureNodeIndex) *  x2.get(x1FeatureNodeIndex);
        }
        return sum;
    }



    /**
     * 计算误差公式： error = ∑a[i]*y[i]*k(x,x[i]) - y[i]
     * @param k
     * @return
     */
    private double calcError(int k){
        return learnFunction(k) - this.vectorMatrix.get(k).getTarget();
    }

    /**
     * 学习函数u，算误差的时候要用
     * @param k k
     * @return sum
     */
    private double learnFunction(int k){
        double sum = 0.0;
        for (int i = 0; i < n; i++){
            sum += alpha[i] * vectorMatrix.get(i).getTarget() * kernel[i][k];
        }
        sum += this.bias;
        return sum;
    }

    /**
     * 随机选择一个样本i2，但要求i1 != i2
     * @param i1 i1
     * @return i2
     */
    private int randomSelect(int i1){
        int i2;
        do {
            i2 = random.nextInt(n);
        } while (i1 == i2);
        return i2;
    }


}
