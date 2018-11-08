/**
 * Created by hao on 16-11-23.
 */


import org.haohhxx.util.core.LogisticRegression;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.feature.VectorMatrix;
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

/**Simple plotting methods for the MLPClassifier examples
 * @author Alex Black
 */
public class PlotUtil {


    public static void main(String[] args) {
        double[][] features_double = new double[1000][2];
        double label[] = new double[1000];

        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\lineartrain.csv";
        VectorMatrix trainvm = new VectorMatrix();
        List<String> lines = IteratorReader.getIteratorReader(trainpath).readLines();
        for (int i = 0; i <lines.size() ; i++) {
            trainvm.add(new VectorLine(VectorLine.LineDataType.csv, lines.get(i)));
            String ls[] = lines.get(i).split(",");
            features_double[i][0] = new Double(ls[1]);
            features_double[i][1] = new Double(ls[2]);
            label[i] =new Double(ls[0]);
        }

        int iter = 5000;
        double alpha = 0.1;
        //配置lr
        LogisticRegression lrh = new LogisticRegression(alpha);
        lrh.fit(iter,trainvm);
//        List<Double> pred_list = lrh.predict(feas);

        printTrain(features_double,label,lrh);
    }



    public static void printTrain(double features[][],double labels[] ,LogisticRegression lrh) {
        double xMin = 0;
        double xMax = 1.0;
        double yMin = -0.2;
        double yMax = 0.8;

        //Let's evaluate the predictions at every point in the x/y input space
        int nPointsPerAxis = 100;
        double[][] backgroundIn = new double[nPointsPerAxis*nPointsPerAxis][2];
        double backgroundOut[] = new double[nPointsPerAxis*nPointsPerAxis];
        int count = 0;
        for( int i=0; i<nPointsPerAxis; i++ ){
            for( int j=0; j<nPointsPerAxis; j++ ){
                double x = i * (xMax-xMin)/(nPointsPerAxis-1) + xMin;
                double y = j * (yMax-yMin)/(nPointsPerAxis-1) + yMin;

                backgroundIn[count][0] = x;
                backgroundIn[count][1] = y;

                VectorLine vectorLine = new VectorLine();
                vectorLine.put(0,x);
                vectorLine.put(1,y);
                backgroundOut[count] = lrh.predict(vectorLine);
                System.out.println(x+"-----"+y+"----"+backgroundOut[count] );
                count++;
            }
        }
//        Features feas = new Features();
//        for(int i =0;i<count;i++){
//            FeaMap feamap = new FeaMap();
//            feamap.put(0,backgroundIn[i][0]);
//            feamap.put(1,backgroundIn[i][1]);
//            feas.add(new Feature("1",feamap));
//        }
//        System.out.println(feas);
//        ArrayList<Double> pred_list = lrh.classify(feas);
//        System.out.println(pred_list);
//        double backgroundOut[] = new double[count];
//        for (int i = 0; i < count; i++) {
//            backgroundOut[i]=pred_list.get(i);
//        }

        XYDataset c = createDataSetTrain(features, labels);
        XYZDataset backgroundData = createBackgroundData(backgroundIn, backgroundOut);
        double[] mins=new double[]{0.0,-0.2};
        double[] maxs=new double[]{1.0,0.8};
        JFreeChart j = createChart(backgroundData, mins, maxs, nPointsPerAxis,c);

        JPanel panel = new ChartPanel(j);
        JFrame f = new JFrame();
        f.add(panel);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.pack();
        f.setTitle("Test Data");
        f.setVisible(true);
    }

    private static XYDataset createDataSetTrain(double[][] features, double[] labels ){

        XYSeries[] series = new XYSeries[2];
        for( int i=0; i<series.length; i++){
            series[i] = new XYSeries("Class " + String.valueOf(i));
        }

        int nRows = features.length;
        for( int i=0; i<nRows; i++ ){
            int classIdx = (int)labels[i];
            series[classIdx].add(features[i][0], features[i][1]);
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


    /**Create data for the background data set
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



    private static JFreeChart createChart(XYZDataset dataset, double[] mins, double[] maxs, int nPoints, XYDataset xyData) {
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
