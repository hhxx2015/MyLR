package org.haohhxx.util.core.svm;

import com.google.common.collect.Sets;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhenyuan_hao@163.com
 */
public class SupportVectorMachine {

    public interface KernalClass {
        double getKernalCatch(int i1,int i2);
        double kernalFunction(VectorLine vectorLine1, VectorLine vectorLine2);
    }

    public static class LinearKernalNoCache implements KernalClass{

        private VectorMatrix vectorMatrix;

        public LinearKernalNoCache(VectorMatrix vectorMatrix){
            this.vectorMatrix = vectorMatrix;
        }

        @Override
        public double getKernalCatch(int i1,int i2) {
            return vectorMatrix.get(i1).dot(vectorMatrix.get(i2));
        }

        @Override
        public double kernalFunction(VectorLine x1, VectorLine x2) {
            return x1.dot(x2);
        }
    }

    public static class LinearKernal implements KernalClass{

        private Map<String,Double> kernelCache;

        public LinearKernal(VectorMatrix vectorMatrix){
            int n = vectorMatrix.size();
            this.kernelCache = new HashMap<>(n);
            for (int i =  0; i < n; i++){
                for (int j = i; j < n; j++){
                    kernelCache.put(i+" "+j,kernalFunction(vectorMatrix.get(i), vectorMatrix.get(j)));
                }
            }
        }

        @Override
        public double getKernalCatch(int i1,int i2) {
            return kernelCache.containsKey(i1+" "+i2)?
                            kernelCache.get(i1+" "+i2):kernelCache.get(i2+" "+i1);
        }

        @Override
        public double kernalFunction(VectorLine x1, VectorLine x2) {
            return x1.dot(x2);
        }
    }

    public static class RBFKernal implements KernalClass{
        private double sigma;
        private double[][] kernelCache;

        public RBFKernal(double sigma, VectorMatrix vectorMatrix){
            this.sigma = sigma;
            int n = vectorMatrix.size();
            this.kernelCache = new double[n][n];

            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++){
                    this.kernelCache[i][j] = kernalFunction(vectorMatrix.get(i), vectorMatrix.get(j));
                }
            }
        }

        @Override
        public double getKernalCatch(int i1,int i2) {
            return kernelCache[i1][i2];
        }

        @Override
        public double kernalFunction(VectorLine x1, VectorLine x2) {
            double i1i2 = x1.dot(x2);
            double i1i1 = x1.pow2();
            double i2i2 = x2.pow2();
            return Math.exp(- (i1i1 + i2i2 - 2 * i1i2) / (2 * Math.pow(sigma,2)));
        }

    }

    private double b = 0.0;
    private int n = 0;

    private double c;
    private double tolerance;
    private double eps;

    private double[] alpha = null;
    private double[] errorCache = null;

    private final Random random;

    private VectorMatrix trainVectorMatrix;

    private KernalClass kernalClass;

    public SupportVectorMachine(double c, KernalClass kernalClass){
        this.kernalClass = kernalClass;
        this.random = new Random();
        this.tolerance = 0.001;
        this.eps= 0.001;
        this.c = c;
    }

    private void fitInit(VectorMatrix vectorMatrix){
        this.trainVectorMatrix = vectorMatrix;
        this.n = vectorMatrix.size();
        this.alpha = new double[n];
        this.errorCache = new double[n];
    }

    public void fit(VectorMatrix vectorMatrix, int epoch) {
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

    private boolean examineExample(int i1){
        double y1 = trainVectorMatrix.get(i1).getTarget();
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
        double y1 = trainVectorMatrix.get(i1).getTarget();
        double y2 = trainVectorMatrix.get(i2).getTarget();
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

        double k11 = kernalClass.getKernalCatch(i1,i1);
        double k12 = kernalClass.getKernalCatch(i1,i2);
        double k22 = kernalClass.getKernalCatch(i2,i2);

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
        double b1 = b - E1 - y1 * (a1 - alpha1) * k11 - y2 * (a2 - alpha2) * k12;
        double b2 = b - E2 - y1 * (a1 - alpha1) * k12 - y2 * (a2 - alpha2) * k22;

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
        this.b = bNew;
        //update error cache
        this.errorCache[i1] = calcError(i1);
        this.errorCache[i2] = calcError(i2);
        //store a1, a2 in alpha array
        alpha[i1] = a1;
        alpha[i2] = a2;
        return true;
    }

    /**
     * 计算误差公式： error = ∑a[i]*y[i]*k(x,x[i]) - y[i]
     * @param k
     * @return
     */
    private double calcError(int k){
        return predicTrain(k) - this.trainVectorMatrix.get(k).getTarget();
    }

    /**
     * 学习函数
     * @param k k
     * @return sum
     */
    private double predicTrain(int k){
        double sum = 0.0;
        for (int i = 0; i < n; i++){
            sum += alpha[i] * trainVectorMatrix.get(i).getTarget() * kernalClass.getKernalCatch(i,k);
        }
        sum += this.b;
        return sum;
    }

    /**
     * 预测函数
     */
    public double predict(VectorLine vectorLine){
        double sum = 0.0;
        for (int j = 0; j < n; j++) {
            sum += alpha[j] * trainVectorMatrix.get(j).getTarget()* kernalClass.kernalFunction(trainVectorMatrix.get(j), vectorLine);
        }
        sum += b;
        return sum;
    }


    /**
     * 预测函数
     */
    public List<Double> predict(VectorMatrix vectorLines){
        return vectorLines.stream().map(this::predict).collect(Collectors.toList());
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
     * 返回拉格朗日乘子得到支持向量
     * @return
     */
    public double[] getAlpha() {
        return alpha;
    }
}
