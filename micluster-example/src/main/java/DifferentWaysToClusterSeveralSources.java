import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;

import java.io.File;

/**
 * Two different ways to cluster several sources
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class DifferentWaysToClusterSeveralSources {
    public static void main(String[] args) throws ClusterServiceException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl";
        /* File */
        final File P37173_intact = new File( InputPsiMitabFile.class.getResource( "/P37173_intact.tsv" ).getFile() );
        final File P37173_mint = new File( InputPsiMitabFile.class.getResource( "/P37173_mint.tsv" ).getFile() );
        final File P37173_innatedb = new File( InputPsiMitabFile.class.getResource( "/P37173_innatedb.tsv" ).getFile() );
        /* Create interaction cluster */
        InteractionCluster iC;

        /* Cluster data: way1 */
        iC = new InteractionCluster();
        iC.setMappingIdDbNames(allMappingNames);
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.runService();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();
        System.out.println("Way1, number of clustered interactions: " + iC.getInteractionMapping().size());

        /* Cluster data: way2 */
        iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(new File[] {P37173_intact,P37173_mint,P37173_innatedb}, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        System.out.println("Way2, number of clustered interactions: " + iC.getInteractionMapping().size());
    }
}
