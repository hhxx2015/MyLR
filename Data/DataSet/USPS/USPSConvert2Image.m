clear all;
clc;
load('USPStrainingdata.mat');%读入训练样本数据库
[charNumbers,dimension]=size(traindata);%获得样本数据库中样本中个数charNumbers和每个字符向量维数dimension
image=zeros(16,16);%初始化归一化图像
P=mat2gray(traindata);%转换为灰度图像
fid = fopen('usps.bin','wb');
startx=0;
overx=0;
len=0;
for k=1:50
    class=find(traintarg(k,:)==1)-1;%获取当前字符的类别
    for i=1:16
        for j=1:16
            image(i,j)=P(k,(i-1)*16+j);
        end
    end
    th= graythresh(image);%求灰度图像的阈值th
    I=im2bw(image,th);%二值化字符图像
    I=~I;
    %      g=bwmorph(I,'thin',1);%细化二值化图形
    %      subplot(5,10,k),imshow(~I);%显示字符图像
    fwrite(fid,class,'uchar'); %记录字符图像的类别
    fwrite(fid,255,'uchar'); %记录字符图像的类别
    fwrite(fid,255,'uchar'); %记录字符图像的类别
    for i=1:16
        for j=1:16
            pixel=I(i,j);
            if (~startx & ~overx & pixel==0)
                beginx = j-1;
                beginy = i-1;
                startx = 1;
            end
            %遇到第一个白色像素
            if (~overx & startx && pixel==1)

                endx = j-1;
                overx = 1;
            end
            %已经到了行尾
            if (~overx & startx & pixel==0 && j==16)

                endx = j-1;
                overx = 1;
            end
            if (startx==1 && overx==1)
                length = endx - beginx;
                startx = 0;
                overx = 0;
                fwrite(fid,beginx,'uchar'); %写入图象的宽度和高度
                fwrite(fid,beginy,'uchar'); %写入图象的宽度和高度
                fwrite(fid,length,'uchar'); %写入图象的宽度和高度
                length=0;
                found=1;
            end
        end
    end
    %写入结束标志
    if (found==1)
        fwrite(fid,0,'uchar'); %写入图象的宽度和高度
        fwrite(fid,0,'uchar'); %写入图象的宽度和高度
        fwrite(fid,0,'uchar'); %写入图象的宽度和高度
    end
end
fclose(fid);







