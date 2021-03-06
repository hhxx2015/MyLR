/**
 * Created by hao on 16-11-23.
 */


import org.haohhxx.util.ml.svm.SupportVectorMachine;
import org.haohhxx.util.matrics.feature.FeatureLine;
import org.haohhxx.util.matrics.feature.FeatureMatrix;
import org.haohhxx.util.matrics.feature.NormalFeatureLine;
import org.haohhxx.util.matrics.feature.SparseFeatureLine;
import org.haohhxx.util.io.IteratorReader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Alex Black
 */
public class PlotUtil {


    public static void main(String[] args) {

//        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\moon_data_train.csv";
        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\saturn_data_train.csv";
        FeatureMatrix trainData = new FeatureMatrix();
        List<String> lines = IteratorReader.getIteratorReader(trainpath).readLines();

        double[][] features_double = new double[lines.size()][2];
        double label[] = new double[lines.size()];

        for (int i = 0; i <lines.size() ; i++) {
            String line = lines.get(i);
            if(line.startsWith("0")){
                line = line.replaceFirst("0","-1");
            }
//            trainData.add(new SparseFeatureLine(FeatureLine.LineDataType.csv, line));
            trainData.add(new NormalFeatureLine(FeatureLine.LineDataType.csv, line));

            String ls[] = lines.get(i).split(",");
            features_double[i][0] = Double.parseDouble(ls[1]);
            features_double[i][1] = Double.parseDouble(ls[2]);
            label[i] = Double.parseDouble(ls[0]);
        }

        int iter = 10000;
        double c = 1;
        double sigma = 2;
        //配置lr
//        LogisticRegression lrh = new LogisticRegression(alpha);

        SupportVectorMachine.KernalClass kernalFunction = new SupportVectorMachine.RBFKernal(sigma, trainData);
//        SupportVectorMachine.KernalClass kernalFunction = new SupportVectorMachine.LinearKernal(trainData);
//        SupportVectorMachine.LinearKernalNoCache kernalFunction = new SupportVectorMachine.LinearKernalNoCache(trainData);
        SupportVectorMachine lrh = new SupportVectorMachine(c, kernalFunction);
//        LogisticRegression lrh = new LogisticRegression(0.1);

        lrh.fit(trainData, iter);
//        List<Double> pred_list = lrh.predict(feas);

        printTrain(features_double,label,lrh);
    }



    public static void printTrain(double features[][],double labels[] ,SupportVectorMachine lrh) {
        double[]  alpha = lrh.getAlpha();

//        double xMin = -1.6;
//        double xMax = 2.6;
//        double yMin = -1.1;
//        double yMax = 1.7;

        double xMin = -12.6;
        double xMax = 12.6;
        double yMin = -12.1;
        double yMax = 12.7;

        //Let's evaluate the predictions at every point in the x/y input space
        int nPointsPerAxis = 300;
        double[][] backgroundIn = new double[nPointsPerAxis*nPointsPerAxis][2];
        double backgroundOut[] = new double[nPointsPerAxis*nPointsPerAxis];
        int count = 0;
        for( int i=0; i<nPointsPerAxis; i++ ){
            for( int j=0; j<nPointsPerAxis; j++ ){
                double x = i * (xMax-xMin)/(nPointsPerAxis-1) + xMin;
                double y = j * (yMax-yMin)/(nPointsPerAxis-1) + yMin;

                backgroundIn[count][0] = x;
                backgroundIn[count][1] = y;
//                NormalFeatureLine vectorLine = new NormalFeatureLine(2);
                SparseFeatureLine vectorLine = new SparseFeatureLine(2);
                vectorLine.put(0,x);
                vectorLine.put(1,y);
                backgroundOut[count] = lrh.predict(vectorLine) + 1.0;
//                System.out.println(backgroundOut[count]);
                count++;
            }
        }

        XYDataset c = createDataSetTrain(features, labels, alpha);
        XYZDataset backgroundData = createBackgroundData(backgroundIn, backgroundOut);
        double[] mins = new double[]{xMin,yMin};
        double[] maxs = new double[]{xMax,yMax};
        JFreeChart j = createChart(backgroundData, mins, maxs, nPointsPerAxis, c);

        JPanel panel = new ChartPanel(j);
        JFrame f = new JFrame();
        f.add(panel);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.pack();
        f.setTitle("Train Data");
        f.setVisible(true);
    }

    private static XYDataset createDataSetTrain(double[][] features, double[] labels,double[] alpha){
        XYSeries[] series = new XYSeries[4];
        for( int i=0; i<series.length; i++){
            series[i] = new XYSeries("Class " + String.valueOf(i));
        }

        int nRows = features.length;
        for( int i=0; i<nRows; i++ ){
            if(alpha[i]>0&&labels[i]>0){
                int classIdx = 2;
                series[classIdx].add(features[i][0], features[i][1]);
            }else if(alpha[i]>0&&labels[i]==0){
                int classIdx = 3;
                series[classIdx].add(features[i][0], features[i][1]);
            }else {
                int classIdx = (int)labels[i];
                series[classIdx].add(features[i][0], features[i][1]);
            }

        }

        XYSeriesCollection c = new XYSeriesCollection();
        for( XYSeries s : series){
            c.addSeries(s);
        }
        return c;
    }

    //Test data
    private static XYDataset createDataSetTest(double[][] features, double[] labels ,double[] predicted ){
        int nRows = features.length;
        int nClasses = 2;

        XYSeries[] series = new XYSeries[nClasses*nClasses];    //new XYSeries("Data");
        for( int i=0; i<nClasses*nClasses; i++){
            int trueClass = i/nClasses;
            int predClass = i%nClasses;
            String label = "actual=" + trueClass + ", pred=" + predClass;
            series[i] = new XYSeries(label);
        }

        for( int i=0; i<nRows; i++ ){
            int classIdx = (int)labels[i];
            int predIdx = (int)predicted[i];
            int idx = classIdx * nClasses + predIdx;
            series[idx].add(features[i][0], features[i][1]);
        }


        XYSeriesCollection c = new XYSeriesCollection();
        for( XYSeries s : series) c.addSeries(s);
        return c;
    }

    /**
     * Create data for the background data set
     */
    private static XYZDataset createBackgroundData(double[][] backgroundIn, double[] backgroundOut) {
        int nRows = backgroundIn.length;
        double[] xValues = new double[nRows];
        double[] yValues = new double[nRows];
        double[] zValues = new double[nRows];
        for( int i=0; i<nRows; i++ ){
            xValues[i] = backgroundIn[i][0];
            yValues[i] = backgroundIn[i][1];
            zValues[i] = backgroundOut[i];
        }
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        dataset.addSeries("Series 1",new double[][]{xValues, yValues, zValues});
        return dataset;
    }


    private static JFreeChart createChart(XYZDataset dataset,
                                          double[] mins, double[] maxs,
                                          int nPoints,
                                          XYDataset xyData) {
        NumberAxis xAxis = new NumberAxis("X");
        xAxis.setRange(mins[0],maxs[0]);

        NumberAxis yAxis = new NumberAxis("Y");
        yAxis.setRange(mins[1], maxs[1]);

        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockWidth((maxs[0]-mins[0])/(nPoints-1));
        renderer.setBlockHeight((maxs[1] - mins[1]) / (nPoints - 1));
        PaintScale scale = new GrayPaintScale(0, 1.0);
        renderer.setPaintScale(scale);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
        JFreeChart chart = new JFreeChart("", plot);
        chart.getXYPlot().getRenderer().setSeriesVisibleInLegend(0, false);

        NumberAxis scaleAxis = new NumberAxis("Probability (class 0)");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));
        PaintScaleLegend legend = new PaintScaleLegend(new GrayPaintScale(),scaleAxis);
        legend.setStripOutlineVisible(false);
        legend.setSubdivisionCount(20);
        legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        legend.setAxisOffset(5.0);
        legend.setMargin(new RectangleInsets(5, 5, 5, 5));
        legend.setFrame(new BlockBorder(Color.red));
        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        legend.setStripWidth(10);
        legend.setPosition(RectangleEdge.LEFT);
        chart.addSubtitle(legend);

        ChartUtilities.applyCurrentTheme(chart);

        plot.setDataset(1, xyData);
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setBaseLinesVisible(false);
        plot.setRenderer(1, renderer2);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        return chart;
    }

}
