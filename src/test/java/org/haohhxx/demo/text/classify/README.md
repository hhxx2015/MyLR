支持向量
--------

在样本组成的特征空间中可以通过线性方程$$w^{T}x+b=0$$描述分类平面(超平面)与样本之间的关系。$$w$$表示法向量，表示超平面的方向;$$b$$是偏移量，表示超平面到样本$$x$$之间的距离。若超平面可以使全部的样本正确分类，则有
<a href="https://www.codecogs.com/eqnedit.php?latex=$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\left\{\begin{matrix}w^{T}x_{i}&plus;b\geq&plus;1,y_{i}=&plus;1\\&space;w^{T}x_{i}&plus;b\leq-1,y_{i}=-1\end{matrix}\right.$$" title="$$\left\{\begin{matrix}w^{T}x_{i}+b\geq+1,y_{i}=+1\\ w^{T}x_{i}+b\leq-1,y_{i}=-1\end{matrix}\right.$$" /></a>
其中距超平面最近的几个样本使以上等式成立，它们被称为“支持向量”。一个正类支持向量到负类支持向量到超平面距离之和为<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>，这一距离被称为间隔。支持向量机的优化过程就是使<a href="https://www.codecogs.com/eqnedit.php?latex=\gamma" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\gamma" title="\gamma" /></a>最大化的过程。为了最大化间隔，显然最小化<a href="https://www.codecogs.com/eqnedit.php?latex=$$\gamma&space;=\frac{2}{||w||}$$" target="_blank"><img src="https://latex.codecogs.com/gif.latex?$$\gamma&space;=\frac{2}{||w||}$$" title="$$\gamma =\frac{2}{||w||}$$" /></a>的分母<a href="https://www.codecogs.com/eqnedit.php?latex=||w||" target="_blank"><img src="https://latex.codecogs.com/gif.latex?||w||" title="||w||" /></a>是等价的。于是支持向量机的基本型表示为
<a href="https://www.codecogs.com/eqnedit.php?latex=\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." target="_blank"><img src="https://latex.codecogs.com/gif.latex?\underset{w,b}{min}\frac{1}{2}&space;||w&space;||^{2}\\&space;s.t.&space;\&space;\&space;y^{_{i}}(w^{T}x_{i}&plus;b)\geq&space;1&space;,i=1,2,...,m." title="\underset{w,b}{min}\frac{1}{2} ||w ||^{2}\\ s.t. \ \ y^{_{i}}(w^{T}x_{i}+b)\geq 1 ,i=1,2,...,m." /></a>


HingeLoss
---------
在实际分类过程中，显然很难存在超平面使样本完美的分布在超平面两侧，为此SVM算法又引入了“软间隔”的概念。


核函数
------


求解：Platt SMO
----------------


SVMlight
--------







