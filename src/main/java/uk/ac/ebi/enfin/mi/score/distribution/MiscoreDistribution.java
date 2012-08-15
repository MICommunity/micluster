package uk.ac.ebi.enfin.mi.score.distribution;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 13:16:14
 */
public interface MiscoreDistribution {
    double[] getScores();
    void createChart(String s);
    void createChart();
    void saveScores();
}
