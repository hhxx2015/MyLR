package libsvmt;

import libsvm.*;
import org.haohhxx.util.core.LogisticRegression;
import org.haohhxx.util.feature.VectorLine;
import org.haohhxx.util.io.CSVReader;
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

public class PlotLibsvm {

    private static double maxminum(double d){
        double max0 = 14 ;
        double min0 = 4;
        return (d-min0) / (max0-min0);
    }

    public static void main(String[] args) {



        double[][] features_double = new double[44][2];
        double label[] = new double[44];

        String trainpath = "E:\\code\\jdk8workspace\\ml\\src\\test\\resources\\CampusData.csv";

        svm_problem prob = new svm_problem();

        CSVReader csvReader = CSVReader.getCSVReader(trainpath);
        int i = 0;
        while (csvReader.hasNext()){
            List<String> line = csvReader.next();
            double x1 = Double.parseDouble(line.get(0).trim());
            double x2 = Double.parseDouble(line.get(1).trim());
            double target = Double.parseDouble(line.get(5).trim());


            x1 = maxminum(x1);
            x2 = maxminum(x2);

            if(target<=1){
                target = 1.0;
            }
            if(target>=2){
                target = 0.0;
            }

            features_double[i][0] = x1;
            features_double[i][1] = x2;
            label[i] = target;
            i++;
        }

        prob.l = i;
        prob.y = new double[i];
        prob.x = new svm_node[i][];

        csvReader = CSVReader.getCSVReader(trainpath);
        i = 0;
        while (csvReader.hasNext()){
            List<String> line = csvReader.next();
            double target = Double.parseDouble(line.get(5).trim());

            if(target<=1){
                target = 1.0;
            }
            if(target>=2){
                target = 0.0;
            }

            prob.y[i] = target;
            svm_node[] x = new svm_node[2];
            for (int j = 0; j < 2; j++) {
                x[j] = new svm_node();
                x[j].index = j;
                x[j].value = Double.parseDouble(line.get(j).trim());
                x[j].value = maxminum(x[j].value);
            }
            prob.x[i] = x;
            i++;
        }

        svm_parameter param = new svm_parameter();

        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
//        param.kernel_type = svm_parameter.LINEAR;
        param.degree = 3;
        param.gamma = 2;	// 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 0.1;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 1;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];

        svm_model model = svm.svm_train(prob,param);

        int nr_class = svm.svm_get_nr_class(model);
        double[] prob_estimates = new double[nr_class];

        printTrain(features_double,label,model, prob_estimates);
    }


    public static void printTrain(double features[][],double labels[] ,svm_model model, double[] prob_estimates) {
        double xMin = 0;
        double xMax = 1.0;
        double yMin = 0;
        double yMax = 1;

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

                svm_node[] textX = new svm_node[2];

                textX[0] = new svm_node();
                textX[0].index = 0;
                textX[0].value = x;

                textX[1] = new svm_node();
                textX[1].index = 1;
                textX[1].value = y;
                double pre = svm.svm_predict_probability(model,textX,prob_estimates);

                backgroundOut[count] = pre;

                System.out.println(x+"-----"+y+"----"+backgroundOut[count] );
                count++;
            }
        }

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
