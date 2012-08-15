package uk.ac.ebi.enfin.mi.score.distribution;

import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.IOException;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 12:48:33
 */
public class MiscoreHistogram {
    private int maximumScore = 1;
    private int minimumScore = 0;
    private int numberOfBars = 10;
    private double[] values = null;
    private String title = "Histogram";
    int width = 500;
    int height = 300;

    public void createChart(){
        String pngFileName = "scoreDistribution.png";
        createChart(pngFileName);
    }

    public void createChart(String pngFileName){
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries(title,values,numberOfBars,minimumScore,maximumScore);
        String plotTitle = title;
        String xaxis = "Score";
        String yaxis = "% of interactions";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                dataset, orientation, show, toolTips, urls);
        try {
            ChartUtilities.saveChartAsPNG(new File(pngFileName), chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(int maximumScore) {
        this.maximumScore = maximumScore;
    }

    public int getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(int minimumScore) {
        this.minimumScore = minimumScore;
    }

    public int getNumberOfBars() {
        return numberOfBars;
    }

    public void setNumberOfBars(int numberOfBars) {
        this.numberOfBars = numberOfBars;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
