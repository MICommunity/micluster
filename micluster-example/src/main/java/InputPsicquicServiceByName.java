import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;

import java.io.IOException;


/**
 * Cluster binary interactions from a PSICQUIC service
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class InputPsicquicServiceByName {
    public static void main(String[] args) throws ClusterServiceException, ConverterException, IOException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl,refseq";
        /* MIQL Query */
        String query = "identifier:P37173";
        /* PSICQUIC service */
        String psicquicService = "IntAct";

        /* Cluster data */
        InteractionCluster iC = new InteractionCluster();
        iC.addMIQLQuery(query);
        iC.addQuerySource(psicquicService);
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
