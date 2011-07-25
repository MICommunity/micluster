package uk.ac.ebi.enfin.mi.score.distribution;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 13:19:02
 */
public class FileMiscoreDistribution implements MiscoreDistribution {
    private double[] scores = null;
    private List scoreList = new ArrayList<String>();
    private String fileName;

    public FileMiscoreDistribution(String fileName) {
        this.fileName = fileName;
        try {
            FileReader fstream = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fstream);
            String line;
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List lineList = Arrays.asList(values);
                scoreList.addAll(lineList);
            }
            scores = new double[scoreList.size()];
            int i = 0;
            for(Object score:scoreList){
                scores[i] = Double.parseDouble(score.toString());
                i++;
            }
            fstream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] getScores() {
        return scores;
    }

    public void createChart() {
        MiscoreHistogram miH = new MiscoreHistogram();
        miH.setTitle("Score distribution for " + (scores != null ? scores.length : 0) + " clustered interactions from " + fileName);
        miH.setNumberOfBars(20);
        miH.setValues(getScores());
        miH.createChart();
    }
}
