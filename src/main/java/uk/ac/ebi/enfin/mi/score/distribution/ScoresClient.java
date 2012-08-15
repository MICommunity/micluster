package uk.ac.ebi.enfin.mi.score.distribution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Client to help users to use the command line to get the charts and scores.
 * Example:
 *    mvn compile
 *    mvn exec:java -Dexec.mainClass="uk.ac.ebi.enfin.mi.score.distribution.PsicquicClient" -Dexec.args="*:* MatrixDB
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.2
 */
public class ScoresClient {
    public static void main(String[] args) {
        File file;
        String dataSetName = "";
        if(args.length == 2){
            String fileLocation = args[1];
            dataSetName = args[0];
            file = new File(fileLocation);
            MiscoreDistributionFromCsvScores mD = new MiscoreDistributionFromCsvScores(file);
            mD.createChart(dataSetName, "results/" + dataSetName + ".png");
            System.out.println("Look for your results in the results folder");
        } else {
            System.out.println("Here some help about how to run this client");
            System.out.println("Execute the following command providing the right number of arguments.");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.ScoresClient\" -Dexec.args=\"Dataset_from_MatrixDB /scratch/tmp/scores/MatrixDB_Scores.txt\"");
            System.out.println("NOTES:");
            System.out.println("   - Remmember to execute first 'mvn compile'");
            System.out.println("   - You might also want to increase memory for this process:");
            System.out.println("          - SET MAVEN_OPTS=\"-Xmx1024m -Xms512m\"\n");
        }
    }
}
