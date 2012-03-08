import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import java.io.File;
import java.io.IOException;

/**
 * Cluster binary interactions from a PSI MITAB file
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class InputPsiMitabFile {
    public static void main(String[] args) throws ClusterServiceException, ConverterException, IOException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl,refseq";
        /* File */
        final File P37173_intact = new File( InputPsiMitabFile.class.getResource( "/P37173_intact.tsv" ).getFile() );

        /* Cluster data */
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();

        /* Print sample data */
        System.out.println("");
        System.out.println("Sample data:");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        OutputSampleData.print(iC);

        /* Print PSI MITAB */
        System.out.println("");
        System.out.println("PSI MITAB:");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        OutputPsiMitab.print(iC);

    }
}
