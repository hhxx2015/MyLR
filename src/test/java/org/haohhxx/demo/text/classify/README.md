支持向量
--------

在线性分布的样本组成的特征空间中可以通过线性方程$$w^{T}x+b=0$$描述分类平面(超平面)与样本之间的关系。$$w$$表示法向量，表示超平面的方向;$$b$$是偏移量，表示超平面到样本$$x$$之间的距离。若超平面可以使全部的样本正确分类，则有

<a href="https://www.codecogs.com/eqnedit.php?latex=$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" title="$$\left\{\begin{matrix}w^{T}x_{i}+b\geq+1,y_{i}=+1\\ w^{T}x_{i}+b\leq-1,y_{i}=-1\end{matrix}\right.$$" /></a>

其中距超平面最近的几个样本使以上等式成立，它们被称为“支持向量”。

![avatar](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/test/java/org/haohhxx/demo/text/classify/pic/margin.PNG)
(图自《机器学习》P122)

一个正类支持向量与一个负类支持向量到超平面距离之和为<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>，这一距离被称为间隔。支持向量机的优化过程就是使<a href="https://www.codecogs.com/eqnedit.php?latex=\gamma" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\gamma" title="\gamma" /></a>最大化的过程。为了最大化间隔，显然最小化<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>的分母<a href="https://www.codecogs.com/eqnedit.php?latex=||w||" target="_blank"><img src="https://latex.codecogs.com/gif.latex?||w||" title="||w||" /></a>是等价的。于是最基本的支持向量机表示为

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." title="\underset{w,b}{min}\frac{1}{2} ||w ||^{2}\\ s.t. \ \ y^{_{i}}(w^{T}x_{i}+b)\geq 1 ,i=1,2,...,m." /></a>


核函数
------
以上讨论是在假设样本线性可分的情况下进行的。当样本的分布不符合线性规律时,需要使用核函数将其映射到线性可分的维度。
(正定核使用证明见《统计学习方法》P118。)
那些常见的核函数实现网上一大堆



HingeLoss
---------
在实际分类过程中，很难存在超平面使样本完美的分布在超平面两侧，为此SVM算法又引入了“软间隔”的概念。

![avatar](https://raw.githubusercontent.com/hhxx2015/MyLR/MyLR_v4/src/test/java/org/haohhxx/demo/text/classify/pic/soft_margin.PNG)
(图自《机器学习》P129)

在此基础上，我们的优化目标变成了**“最大化间隔的同时，使大多数样本满足约定条件。”**
此处引入松弛变量<a href="https://www.codecogs.com/eqnedit.php?latex=\xi_{i}\geq&space;0" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\xi_{i}\geq&space;0" title="\xi_{i}\geq 0" /></a>使<a href="https://www.codecogs.com/eqnedit.php?latex=y_{i}(w^{T}x_{i}&plus;b)\geqslant1-&space;\xi&space;_{i}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?y_{i}(w^{T}x_{i}&plus;b)\geqslant1-&space;\xi&space;_{i}" title="y_{i}(w^{T}x_{i}+b)\geqslant1- \xi _{i}" /></a>，这样间隔的最大值就可以根据参数调整。


同时，引入惩罚参数C来控制模型训练时对那些噪声样本的容忍度。使用0/1损失函数的SVM优化目标可以表示为：

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}&plus;b)-1)))" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}&plus;b)-1)))" title="\underset{w,b}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}loss_{0/1}(y_{i}(w^{T}x_{i}+b)-1)))" /></a>

但0/1损失不易于直接求解<del>(数学性质不好、非凸、非连续、总之大佬都说不好就是不好)</del>

因此需要采用合页损失函数<a href="https://www.codecogs.com/eqnedit.php?latex=loss_{hinge}=max(0,1-\widehat{y})" target="_blank"><img src="https://latex.codecogs.com/gif.latex?loss_{hinge}=max(0,1-\widehat{y})" title="loss_{hinge}=max(0,1-\widehat{y})" /></a>。

使用合页损失函数的SVM优化目标可以表示为：

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}&plus;b))" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}&plus;b))" title="\underset{w,b}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}max(0,1-y_{i}(w^{T}x_{i}+b))" /></a>

则优化目标变更为

<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b,\&space;\xi}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}\xi_{i}\\&space;.\qquad&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1-\xi_{i}\\&space;.\qquad\qquad&space;\xi_{i}\geq&space;0,\&space;i=1,2,...,m." target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b,\&space;\xi}{min}&space;\&space;\frac{1}{2}&space;||w&space;||^{2}&plus;C\sum_{i=1}^{m}\xi_{i}\\&space;.\qquad&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1-\xi_{i}\\&space;.\qquad\qquad&space;\xi_{i}\geq&space;0,\&space;i=1,2,...,m." title="\underset{w,b,\ \xi}{min} \ \frac{1}{2} ||w ||^{2}+C\sum_{i=1}^{m}\xi_{i}\\ .\qquad s.t. \ \ y^{_{i}}(w^{T}x_{i}+b)\geq 1-\xi_{i}\\ .\qquad\qquad \xi_{i}\geq 0,\ i=1,2,...,m." /></a>

其中<a href="https://www.codecogs.com/eqnedit.php?latex=\frac{1}{2}||w||" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\frac{1}{2}||w||" title="\frac{1}{2}||w||" /></a>就是机器学习中常见的<a href="https://www.codecogs.com/eqnedit.php?latex=L_{2}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?L_{2}" title="L_{2}" /></a>正则。实现算法时通常<a href="https://www.codecogs.com/eqnedit.php?latex=\frac{1}{2}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\frac{1}{2}" title="\frac{1}{2}" /></a>也会换成参数进行调整。


SVM求解Platt SMO
----------------
SMO算法可以读一下《Sequential Minimal Optimization:A Fast Algorithm for Training Support Vector Machines》




SVMlight
--------







