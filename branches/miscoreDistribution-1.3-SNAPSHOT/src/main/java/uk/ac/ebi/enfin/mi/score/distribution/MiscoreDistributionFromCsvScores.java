package uk.ac.ebi.enfin.mi.score.distribution;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 13:19:02
 */
public class MiscoreDistributionFromCsvScores implements MiscoreDistribution {
    private double[] scores = null;
    private List scoreList = new ArrayList<String>();


    public MiscoreDistributionFromCsvScores(String csvScores) {
        setScores(csvScores);
    }

    public MiscoreDistributionFromCsvScores(File file) {
        try {
            FileReader fstream = new FileReader(file);
            BufferedReader br = new BufferedReader(fstream);
            String line = br.readLine();
            fstream.close();
            setScores(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScores(String csvScores){
        try {
            String[] values = csvScores.split(",");
            List lineList = Arrays.asList(values);
            scoreList.addAll(lineList);
            scores = new double[scoreList.size()];
            int i = 0;
            for(Object score:scoreList){
                scores[i] = Double.parseDouble(score.toString());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[] getScores() {
        return scores;
    }

    public void createChart() {
        createChart("");
    }

    public void saveScores() {
    }

    public void createChart(String fileName) {
        createChart("", fileName);
    }

    public void createChart(String dataSetName, String fileName) {
        MiscoreHistogram miH = new MiscoreHistogram();
        if(dataSetName.length() == 0){
            miH.setTitle("Score distribution for " + (scores != null ? scores.length : 0) + " clustered interactions");

        } else {
            miH.setTitle("Score distribution for " + (scores != null ? scores.length : 0) + " clustered interactions from " + dataSetName);
        }
        miH.setNumberOfBars(20);
        miH.setValues(getScores());
        if(fileName.length() == 0){
            miH.createChart();
        } else {
            miH.createChart(fileName);
        }
    }
}
