
支持向量简介
------------

在线性分布的样本组成的特征空间中可以通过线性方程<img src="https://latex.codecogs.com/gif.latex?w^{T}x&plus;b=0" title="w^{T}x+b=0" />描述分类平面(超平面)与样本之间的关系。<img src="https://latex.codecogs.com/gif.latex?w" title="w" />表示法向量，决定超平面的方向;b是偏移量，决定超平面到样本x之间的距离。若超平面可以使全部的样本正确分类，则有

<a href="https://www.codecogs.com/eqnedit.php?latex=$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" title="$$\left\{\begin{matrix}w^{T}x_{i}+b\geq+1,y_{i}=+1\\ w^{T}x_{i}+b\leq-1,y_{i}=-1\end{matrix}\right.$$" /></a>

其中距超平面最近的几个样本使以上等式成立，它们被称为“支持向量”。

![avatar](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/test/java/org/haohhxx/demo/text/classify/pic/margin.PNG)
(图自《机器学习》P122)

一个正类支持向量与一个负类支持向量到超平面距离之和为<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>，这一距离被称为间隔。支持向量机的优化过程就是使<a href="https://www.codecogs.com/eqnedit.php?latex=\gamma" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\gamma" title="\gamma" /></a>最大化的过程。为了最大化间隔，显然最小化<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>的分母<a href="https://www.codecogs.com/eqnedit.php?latex=||w||" target="_blank"><img src="https://latex.codecogs.com/gif.latex?||w||" title="||w||" /></a>是等价的。于是最基本的支持向量机表示为

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." title="\underset{w,b}{min}\frac{1}{2} ||w ||^{2}\\ s.t. \ \ y^{_{i}}(w^{T}x_{i}+b)\geq 1 ,i=1,2,...,m." /></a>

核函数
------
以上讨论是在假设样本线性可分的情况下进行的。当样本的分布不符合线性规律时,需要使用核函数将其映射到线性可分的维度。
(正定核使用证明见《统计学习方法》P118)
那些常见的核函数实现网上一大堆

软间隔和HingeLoss
-----------------
在实际分类过程中，很难存在超平面使样本完美的分布在超平面两侧，为此SVM算法又引入了“软间隔”的概念。

![avatar](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/test/java/org/haohhxx/demo/text/classify/pic/soft_margin.PNG)
(图自《机器学习》P129)

在此基础上，我们的优化目标变成了**“最大化间隔的同时，使大多数样本满足约定条件。”**

此处引入惩罚参数C来控制模型训练时对那些噪声样本的容忍度。此时SVM优化目标的损失函数为0/1损失：

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}&plus;b)-1)))" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}&plus;b)-1)))" title="\underset{w,b}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}+b)-1)))" /></a>

这里以看一下 李宏毅 机器学习(2017)的第二十课SVM [bilibili av10590361](https://www.bilibili.com/video/av10590361/?p=31)

![avatar](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/test/java/org/haohhxx/demo/text/classify/pic/hinge_loss.PNG)
(图自《机器学习》P131)

0/1损失不易于直接求解<del>(数学性质不好、非凸、非连续、总之大佬都说不好)</del>

因此采用效果更佳的合页损失函数<a href="https://www.codecogs.com/eqnedit.php?latex=loss_{hinge}=max(0,1-\widehat{y})" target="_blank"><img src="https://latex.codecogs.com/gif.latex?loss_{hinge}=max(0,1-\widehat{y})" title="loss_{hinge}=max(0,1-\widehat{y})" /></a>。如图所示，因其函数形状看起来像合页而得名。

使用合页损失函数的SVM优化目标可以表示为：

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}&plus;b))" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}&plus;b))" title="\underset{w,b}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}+b))" /></a>

其中<a href="https://www.codecogs.com/eqnedit.php?latex=\frac{1}{2}||w||^{2}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\frac{1}{2}||w||^{2}" title="\frac{1}{2}||w||^{2}" /></a>
可以说是很眼熟了。就是我们熟知的<a href="https://www.codecogs.com/eqnedit.php?latex=L_{2}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?L_{2}" title="L_{2}" /></a>
正则。实现算法时其系数<a href="https://www.codecogs.com/eqnedit.php?latex=\frac{1}{2}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\frac{1}{2}" title="\frac{1}{2}" /></a> 可以换成参数进行调整。

再引入松弛变量<img src="https://latex.codecogs.com/gif.latex?\xi_{i}\geq&space;0" title="\xi_{i}\geq 0" />使<a href="https://www.codecogs.com/eqnedit.php?latex=y_{i}(w^{T}x_{i}&plus;b)\geqslant1-&space;\xi&space;_{i}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?y_{i}(w^{T}x_{i}&plus;b)\geqslant1-&space;\xi&space;_{i}" title="y_{i}(w^{T}x_{i}+b)\geqslant1- \xi _{i}" /></a>，这样就可以调整间隔的最大值。
(关于<img src="https://latex.codecogs.com/gif.latex?\xi_{i}" title="\xi_{i}" />的等价代换证明可以看《统计学习方法》P114)

则优化目标变更为

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b,\&space;\xi}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}\xi_{i}\\&space;.\qquad&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1-\xi_{i}\\&space;.\qquad\qquad&space;\xi_{i}\geq&space;0,\&space;i=1,2,...,m." target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b,\&space;\xi}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}\xi_{i}\\&space;.\qquad&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1-\xi_{i}\\&space;.\qquad\qquad&space;\xi_{i}\geq&space;0,\&space;i=1,2,...,m." title="\underset{w,b,\ \xi}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}\xi_{i}\\ .\qquad s.t. \ \ y^{_{i}}(w^{T}x_{i}+b)\geq 1-\xi_{i}\\ .\qquad\qquad \xi_{i}\geq 0,\ i=1,2,...,m." /></a>


Platt SMO 序列最小优化算法求解 SVM
----------------------------------
<del>求解过程需要先复习《高等数学 第六版 下册》P113《条件极值 拉格朗日乘数法》</del>  ⊙﹏⊙

根据优化目标的两个条件引入拉格朗日乘子
<img src="https://latex.codecogs.com/gif.latex?\alpha&space;_{i}\geq&space;0" title="\alpha _{i}\geq 0" />
和
<img src="https://latex.codecogs.com/gif.latex?\mu&space;_{i}\geq&space;0" title="\mu _{i}\geq 0" />
得到拉格朗日函数：

<img src="https://latex.codecogs.com/gif.latex?L(w,b,\alpha,\xi,\mu)=&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}\xi_{i}&space;&plus;&space;\sum_{i=1}^{m}\alpha_{i}(1-\xi_{i}-y_{i}(w^{T}x_{i}&plus;b))-\sum_{i=1}^{m}\mu_{i}\xi_{i}" title="L(w,b,\alpha,\xi,\mu)= \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}\xi_{i} + \sum_{i=1}^{m}\alpha_{i}(1-\xi_{i}-y_{i}(w^{T}x_{i}+b))-\sum_{i=1}^{m}\mu_{i}\xi_{i}" />

使偏L对偏<img src="https://latex.codecogs.com/gif.latex?w,b,\xi_{i}" title="w,b,\xi_{i}" />为0

<img src="http://quicklatex.com/cache3/27/ql_4777a6683e755ebdb9774c02ca761827_l3.png">

再将其带回L中，得到对偶问题

<img src="http://quicklatex.com/cache3/ce/ql_4190cde51e3b35771659999bd435d4ce_l3.png">

同时满足KKT条件

<img src="https://latex.codecogs.com/gif.latex?\left\{\begin{matrix}&space;&&space;\alpha_{i}\geq&space;0,\mu_{i}&space;\geq&space;0&space;,&space;\\&space;&&space;y_{i}f(x_{i})-1&plus;\xi_{i}&space;\geq&space;0&space;,&space;\\&space;&&space;\alpha_{i}(y_{i}f(x_{i})-1&plus;\xi_{i})=0,\\&space;&&space;\xi_{i}\geq&space;0,&space;\xi_{i}\mu_{i}=0&space;\end{matrix}\right." title="\left\{\begin{matrix} & \alpha_{i}\geq 0,\mu_{i} \geq 0 , \\ & y_{i}f(x_{i})-1+\xi_{i} \geq 0 , \\ & \alpha_{i}(y_{i}f(x_{i})-1+\xi_{i})=0,\\ & \xi_{i}\geq 0, \xi_{i}\mu_{i}=0 \end{matrix}\right." />


然后使用SMO算法对对偶问题求解。首先需要了解坐标上升(下降)法：

对于待优化无约束问题<img src="https://latex.codecogs.com/gif.latex?\underset{\alpha}{max}W(\alpha_{1},\alpha_{2},...,\alpha_{n})" title="\underset{\alpha}{max}W(\alpha_{1},\alpha_{2},...,\alpha_{n})" />
依次固定参数<img src="https://latex.codecogs.com/gif.latex?\alpha_{i}" title="\alpha_{i}" />以外的参数，令其偏导为0，
<img src="https://latex.codecogs.com/gif.latex?\frac{\partial&space;f}{\partial&space;\alpha_{i}}=0" title="\frac{\partial f}{\partial \alpha_{i}}=0" />
得到新的<img src="https://latex.codecogs.com/gif.latex?\alpha_{i}" title="\alpha_{i}" />，更新<img src="https://latex.codecogs.com/gif.latex?\alpha_{i}" title="\alpha_{i}" />的值并对固定下一参数，
不断迭代直至算法收敛，最终得到全部的参数。

SMO算法便是类似思路的启发式方法。因约束条件中存在对单<img src="https://latex.codecogs.com/gif.latex?\alpha_{i}" title="\alpha_{i}" />的约束，所以SMO每次的优化参数为两个。
每次迭代我们选择违背约束条件最大(根据直觉)的参数进行优化。
在选择参数<img src="https://latex.codecogs.com/gif.latex?\alpha_{1}" title="\alpha_{1}" />和<img src="https://latex.codecogs.com/gif.latex?\alpha_{2}" title="\alpha_{2}" />
的情况下，SMO最优化问题变为

```java
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

```


SMO算法可以读一下[《Sequential Minimal Optimization:A Fast Algorithm for Training Support Vector Machines》](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/main/java/org/haohhxx/util/core/svm/smo-book.pdf)
原文，感觉讲解比书上详尽，而且给出了算法的伪代码。



SVMlight
--------


测试与使用
----------


总结
----











