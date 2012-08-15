package uk.ac.ebi.enfin.mi.score.distribution;

import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Client to help users to use the command line to get the charts and scores
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.2
 */
public class PsimitabClient {
    public static void main(String[] args) {
        String fileSetName = "";
        File[] mitabFiles;
        boolean mitabHeader = false;
        if(args.length == 3){
            fileSetName = args[0];
            String folderWithPsimiFiles = args[1];
            File folder = new File(folderWithPsimiFiles);
            mitabFiles = folder.listFiles();
            if(args[2].equalsIgnoreCase("true")){
                mitabHeader = true;
            }
            //String fileSetName, File[] mitabFiles, boolean mitabHeader
            MiscoreDistribution mD = new MiscoreDistributionImp(fileSetName, mitabFiles, mitabHeader);
            mD.createChart();
            mD.saveScores();
            System.out.println("Look for your results in the results folder");
        } else {
            System.out.println("Here some help about how to run this client");
            System.out.println("Execute the following command providing the right number of arguments.");
            System.out.println("mvn exec:java -Dexec.mainClass=\"uk.ac.ebi.enfin.mi.score.distribution.PsimitabClient\" -Dexec.args=\"My_dataset /scratch/tmp/mitab false\"");
            System.out.println("   - First argument: Dataset name");
            System.out.println("   - Second argument: Folder path with PSI MITAB files");
            System.out.println("   - Third argument: Do the files have a header? false/true");
            System.out.println("NOTES:");
            System.out.println("   - Remmember to execute first 'mvn compile'");
            System.out.println("   - You might also want to increase memory for this process:");
            System.out.println("          - SET MAVEN_OPTS=\"-Xmx1024m -Xms512m\"\n");
        }
    }
}
