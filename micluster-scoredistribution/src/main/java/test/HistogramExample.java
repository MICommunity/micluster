package test;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 15:54:19
 */
import java.io.*;

import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;
import uk.ac.ebi.enfin.mi.score.distribution.EncoreMiscoreDistribution;

public class HistogramExample {
    public static void main(String[] args) {

        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution("MatrixDB");
        double[] value = mD.getScores();
        int number = 10;
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("Histogram",value,number,0,1);
        String plotTitle = "Histogram";
        String xaxis = "number";
        String yaxis = "value";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                dataset, orientation, show, toolTips, urls);
        int width = 500;
        int height = 300;
        try {
            ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
        } catch (IOException e) {}

    }
}