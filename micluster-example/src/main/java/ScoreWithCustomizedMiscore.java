import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cluster and score binary interactions
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class ScoreWithCustomizedMiscore {
    public static void main(String[] args) throws ClusterServiceException, ConverterException, IOException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl,refseq";
        /* File */
        final File P37173_intact = new File( InputPsiMitabFile.class.getResource( "/P37173_intact.tsv" ).getFile() );
        final File P37173_mint = new File( InputPsiMitabFile.class.getResource( "/P37173_mint.tsv" ).getFile() );
        final File P37173_innatedb = new File( InputPsiMitabFile.class.getResource( "/P37173_innatedb.tsv" ).getFile() );

        /* Override default configuration for type score with custom values */
        Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
        customOntologyTypeScores.put("MI:0208", 0.05f); // cv1 // genetic interaction
        customOntologyTypeScores.put("MI:0403", 0.20f); // cv2 // colocalization
        customOntologyTypeScores.put("MI:0914", 0.20f); // cv3 // association
        customOntologyTypeScores.put("MI:0915", 0.40f); // cv4 // physical association
        customOntologyTypeScores.put("MI:0407", 1.00f); // cv5 // direct interaction
        customOntologyTypeScores.put("unknown", 0.02f); // cv6 // unknown

        /* Custom method score */
        Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
        customOntologyMethodScores.put("MI:0013", 1.00f); // cv1 // biophysical
        customOntologyMethodScores.put("MI:0090", 0.66f); // cv2 // protein complementation assay
        customOntologyMethodScores.put("MI:0254", 0.10f); // cv3 // genetic interference
        customOntologyMethodScores.put("MI:0255", 0.10f); // cv4 // post transcriptional interference
        customOntologyMethodScores.put("MI:0401", 1.00f); // cv5 // biochemical
        customOntologyMethodScores.put("MI:0428", 0.33f); // cv6 // imagining technique
        customOntologyMethodScores.put("unknown", 0.05f); // cv7 // unknown

         /* Number of publications for the highest score  */
        int customPublicationNumber = 10;

        /* Cluster data */
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(new File[] {P37173_intact,P37173_mint,P37173_innatedb}, false);
        iC.setScoreName("miscore");
        iC.setCustomOntologyTypeScores(customOntologyTypeScores);
        iC.setCustomOntologyMethodScores(customOntologyMethodScores);
        iC.setCustomPublicationNumberWithHighestScore(customPublicationNumber);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();

        /* Print score sample data */
        System.out.println("");
        System.out.println("Score sample data:");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        OutputScoreSampleData.print(iC);

        /* Print PSI MITAB */
        System.out.println("");
        System.out.println("PSI MITAB:");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        OutputPsiMitab.print(iC);

    }
}
