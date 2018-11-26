package org.haohhxx.util.core.svm;

import org.haohhxx.util.feature.AbstractFeatureLine;
import org.haohhxx.util.feature.FeatureMatrix;
import org.haohhxx.util.io.HaoWriter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhenyuan_hao@163.com
 *
 * https://blog.csdn.net/maoersong/article/details/24315633
 * https://github.com/apachecn/AiLearning/blob/dev/src/py3.x/ml/6.SVM/svm-complete.py
 * https://github.com/yinchuandong/SMO4SVM
 */
public class SupportVectorMachine {


    public interface KernalClass {

        /**
         * 核方法  处理一对样本
         * @param vectorLine1 x1
         * @param vectorLine2 x2
         * @return kernalVal
         */
        double kernalFunction(AbstractFeatureLine vectorLine1, AbstractFeatureLine vectorLine2);

        /**
         * 核方法  训练时调用的核方法，用于事先缓存加速
         * @param i1 x1
         * @param i2 x2
         * @return kernalVal
         */
        double getKernalCatch(int i1,int i2);
    }

    /**
     * todo sigmoid kernal
     */
    public static class SigmoidKernalNoCatch implements KernalClass{

        @Override
        public double kernalFunction(AbstractFeatureLine vectorLine1, AbstractFeatureLine vectorLine2) {
            return 0;
        }

        @Override
        public double getKernalCatch(int i1, int i2) {
            return 0;
        }
    }


    public static class LinearKernalNoCache implements KernalClass{

        private FeatureMatrix vectorMatrix;

        public LinearKernalNoCache(FeatureMatrix vectorMatrix){
            this.vectorMatrix = vectorMatrix;
        }

        @Override
        public double getKernalCatch(int i1,int i2) {
            return vectorMatrix.get(i1).dot(vectorMatrix.get(i2));
        }

        @Override
        public double kernalFunction(AbstractFeatureLine x1, AbstractFeatureLine x2) {
            return x1.dot(x2);
        }
    }

    public static class LinearKernal implements KernalClass{

        private Map<String,Double> kernelCache;

        public LinearKernal(FeatureMatrix vectorMatrix){
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
        public double kernalFunction(AbstractFeatureLine x1, AbstractFeatureLine x2) {
            return x1.dot(x2);
        }
    }

    public static class RBFKernalNoCatch implements KernalClass{
        private double gamma;
        private FeatureMatrix vectorMatrix;

        public RBFKernalNoCatch(double gamma, FeatureMatrix vectorMatrix){
            this.gamma = gamma;
            this.vectorMatrix = vectorMatrix;
        }

        @Override
        public double getKernalCatch(int i1,int i2) {
            return kernalFunction(vectorMatrix.get(i1),vectorMatrix.get(i2));
        }

        @Override
        public double kernalFunction(AbstractFeatureLine x1, AbstractFeatureLine x2) {
            double i1i2 = x1.dot(x2);
            double i1i1 = x1.pow2();
            double i2i2 = x2.pow2();
            return Math.exp(-gamma * (i1i1 + i2i2 - 2 * i1i2));
        }
    }


    public static class RBFKernal implements KernalClass{
        private double gamma;
        private double[][] kernelCache;

        public RBFKernal(double gamma, FeatureMatrix vectorMatrix){
            this.gamma = gamma;
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
        public double kernalFunction(AbstractFeatureLine x1, AbstractFeatureLine x2) {
            double i1i2 = x1.dot(x2);
            double i1i1 = x1.pow2();
            double i2i2 = x2.pow2();
            return Math.exp(-gamma * (i1i1 + i2i2 - 2 * i1i2));
        }

    }

    private double b = 0.0;

    /**
     * 样本数量
     */
    private int n = 0;

    /**
     * 惩罚因子
     */
    private double c;

    /**
     * 松弛变量
     */
    private double tolerance;

    /**
     * 终止条件的差值
     */
    private double eps;

    /**
     * 拉格朗日乘子
     */
    private double[] alpha = null;

    /**
     * 缓存的 E 随迭代更新
     */
    private double[] errorCache = null;

    private final Random random;

    private FeatureMatrix trainVectorMatrix;

    private KernalClass kernalClass;

    public SupportVectorMachine(double c, KernalClass kernalClass){
        this.kernalClass = kernalClass;
        this.random = new Random();
        this.tolerance = 0.001;
        this.eps= 0.001;
        this.c = c;
    }

    private void fitInit(FeatureMatrix vectorMatrix){
        this.trainVectorMatrix = vectorMatrix;
        this.n = vectorMatrix.size();
        this.alpha = new double[n];
        this.errorCache = new double[n];
    }

    public void fit(FeatureMatrix vectorMatrix, int epoch) {
        this.fitInit(vectorMatrix);
        int epochNub = 0;
        int alphaPairsChanged  = 0;
        boolean examineAll = true;

        //当迭代次数大于maxIter或者 所有样本中没有alpha对改变时，跳出循环
        while((epochNub < epoch) && (alphaPairsChanged  > 0 || examineAll)){
            alphaPairsChanged  = 0;
            if (examineAll) {
                //循环检查所有样本
                for (int i = 0; i < n; i++) {
                    if (examineExample(i)) {
                        alphaPairsChanged ++;
                    }
                }
                epochNub ++;
            }else{
                //只检查非边界样本
                for (int i = 0; i < n; i++) {
//                    if (alpha[i] != 0 && alpha[i] != c) {
                    if (alpha[i] > 0 && alpha[i] < c) {
                        if (examineExample(i)) {
                            alphaPairsChanged  ++;
                        }
                    }
                }
                epochNub ++;
            }

            //如果找到alpha对，就优化非边界alpha值，否则，就重新进行寻找，如果寻找一遍 遍历所有的行还是没找到，就退出循环。
            if (examineAll) {
                examineAll = false;
            }else if (alphaPairsChanged == 0) {
                examineAll = true;
            }
        }
    }

    /**
     * Platt SMO
     *
     *  统计学习方法 P128 7.4.2章
     *  Fast Training of Support Vector Machines using Sequential Minimal Optimization
     *
     * @param i1 i1
     * @return 是否优化
     */
    private boolean examineExample(int i1){
        double y1 = trainVectorMatrix.get(i1).getTarget();
        double alpha1 = alpha[i1];
        double E1 = 0;

        if (0 < alpha1 && alpha1 < c) {
            E1 = errorCache[i1];
        }else{
            E1 = calcErrorCatch(i1);
        }

        /*
            检验第一个点是否满足 KKT条件

            《统计学习方法》
                                7.111
                                7.112
                                7.113

          yi*g(xi) >= 1 and alpha == 0 (正确分类)
          yi*g(xi) == 1 and 0 < alpha < C (在边界上的支持向量)
          yi*g(xi) <= 1 and alpha == C (在边界之间)

         */
        double r1 = y1 * E1;
        if ((r1 < -tolerance && alpha1 < c) || (r1 > tolerance && alpha1 > 0)) {
            //选择 E1 - E2 差最大的两点
            //选择最大的误差对应的j进行优化。效果更明显
            int i2 = this.getMaxJ(E1);
            if (i2 >= 0) {
                if (takeStep(i1, i2)) {
                    return true;
                }
            }

            //先选择 0 < alpha < C的点
            //loop over all non-zero and non-C alpha, starting at random point
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
            //loop over all possible i1, starting at a random point
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
     * 优化步骤
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
        double y1y2 = y1 * y2;
        double alpha1new, alpha2new; //新的a
        double L; //下界
        double H; //上界

        if (0 < alpha1 && alpha1 < c) {
            E1 = errorCache[i1];
        }else{
            E1 = calcErrorCatch(i1);
        }

        if (0 < alpha2 && alpha2 < c) {
            E2 = errorCache[i2];
        }else{
            E2 = calcErrorCatch(i2);
        }

        /*
            统计学习方法P126
         */
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
        // eta是alphas[j]的最优修改量，如果eta==0，需要退出for循环的当前迭代过程
        //《统计学习方法》7.107
        double eta = 2 * k12 - k11 - k22;
        //根据不同情况计算出新的alpha2
        if (eta < 0) {
            //计算非约束条件下的最大值
            alpha2new = alpha2 - y2 * (E1 - E2) / eta;

            //《统计学习方法》7.108  判断约束的条件
            if (alpha2new < L) {
                alpha2new = L;
            } else if (alpha2new > H) {
                alpha2new = H;
            }
        }else {
            /*
                Lobj = objective function at a2=L
                Hobj = objective function at a2=H
             */

            double c1 = eta / 2;
            double c2 = y2 * (E1 - E2) - eta * alpha2;
            double Lobj = c1 * L * L + c2 * L;
            double Hobj = c1 * H * H + c2 * H;

            if (Lobj > Hobj + eps) {
                alpha2new = L;
            }else if (Lobj < Hobj - eps) {
                alpha2new = H;
            } else {
                alpha2new = alpha2;
            }
        }

        if(alpha2new< 1e-8){
            alpha2new = 0;
        }else if(alpha2new > c-1e-8){
            alpha2new = c;
        }

        if (Math.abs(alpha2new - alpha2) < eps * (alpha2new + alpha2 + eps)) {
            return false;
        }

        //通过a2来更新a1
        alpha1new = alpha1 + y1y2 * (alpha2 - alpha2new);

//        if (alpha1new < 1e-8) {
//            alpha2new += y1y2 * alpha1new;
//            alpha1new = 0;
//        }else if (alpha1new > c-1e-8) {
//            alpha2new += y1y2 * (alpha1new - c);
//            alpha1new = c;
//        }

        /*
            7.115
            7.116
            选择满足    0<alpha1new<c 的b来更新
         */
        if (0 < alpha1new && alpha1new < c) {
            this.b = b - E1 - y1 * (alpha1new - alpha1) * k11 - y2 * (alpha2new - alpha2) * k12;
        }else if (0 < alpha2new && alpha2new < c) {
            this.b = b - E2 - y1 * (alpha1new - alpha1) * k12 - y2 * (alpha2new - alpha2) * k22;
        }else {
            double b1 = b - E1 - y1 * (alpha1new - alpha1) * k11 - y2 * (alpha2new - alpha2) * k12;
            double b2 = b - E2 - y1 * (alpha1new - alpha1) * k12 - y2 * (alpha2new - alpha2) * k22;
            this.b = (b1 + b2) / 2;
        }

        // 更新误差缓存
        this.errorCache[i1] = calcErrorCatch(i1);
        this.errorCache[i2] = calcErrorCatch(i2);

        alpha[i1] = alpha1new;
        alpha[i2] = alpha2new;
        return true;
    }

    /**
     * 《统计学习方法》 7.105
     * 预测值 减去 目标值
     * @param i
     * @return Ei
     */
    private double calcErrorCatch(int i){
        double yi = this.trainVectorMatrix.get(i).getTarget();
        return predicTrain(i) - yi;
    }

    /**
     * 学习函数
     * @param i i
     * @return sum
     */
    private double predicTrain(int i){
        double sum = 0.0;
        for (int j = 0; j < n; j++){
            double yj = trainVectorMatrix.get(j).getTarget();
            sum += alpha[j] * yj * kernalClass.getKernalCatch(j,i);
        }
        sum += this.b;
        return sum;
    }

    /**
     * 预测函数
     */
    public double predict(AbstractFeatureLine vectorLine){
        double sum = 0.0;
        for (int j = 0; j < n; j++) {
            double yj = trainVectorMatrix.get(j).getTarget();
            sum += alpha[j] * yj * kernalClass.kernalFunction(trainVectorMatrix.get(j), vectorLine);
        }
        sum += b;
        return sum;
    }


    /**
     * 预测函数
     */
    public List<Double> predict(FeatureMatrix vectorLines){
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

    public void save(String modelPath){
        HaoWriter hw = new HaoWriter(modelPath);
        hw.writeLine(String.valueOf(this.b));
        for (int i = 0; i <alpha.length ; i++) {
            hw.writeOut(String.valueOf(alpha[i]));
        }
        hw.close();
    }


}
