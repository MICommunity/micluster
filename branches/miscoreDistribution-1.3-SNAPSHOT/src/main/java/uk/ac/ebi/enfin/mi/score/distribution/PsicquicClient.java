package uk.ac.ebi.enfin.mi.score.distribution;

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
public class PsicquicClient {
    public static void main(String[] args) {
        String miqlQuery = "";
        String databases = "";
        if(args.length == 2){
            miqlQuery = args[0];
            databases = args[1];
            List<String> databaseList = new ArrayList<String>();
            String[] databaseArray = databases.split(",");
            for(String database:databaseArray){
                databaseList.add(database);
            }
            MiscoreDistribution mD = new MiscoreDistributionImp(miqlQuery,databaseList);
            mD.createChart();
            mD.saveScores();
            System.out.println("Look for your results in the results folder");
        } else {
            System.out.println("Here some help about how to run this client");
            System.out.println("Execute the following command providing the right number of arguments.");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.PsicquicClient\" -Dexec.args=\"*:* MatrixDB\"");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.PsicquicClient\" -Dexec.args=\"P99999\\ OR\\ P30480 MatrixDB\"");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.PsicquicClient\" -Dexec.args=\"P99999 MatrixDB,IntAct\"");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.PsicquicClient\" -Dexec.args=\"P99999 all\"");
            System.out.println("NOTES:");
            System.out.println("   - Remmember to execute first 'mvn compile'");
            System.out.println("   - You might also want to increase memory for this process:");
            System.out.println("          - SET MAVEN_OPTS=\"-Xmx1024m -Xms512m\"\n");
        }
    }
}
