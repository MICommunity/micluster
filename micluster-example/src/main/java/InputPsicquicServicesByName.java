import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster binary interactions from several PSICQUIC services
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class InputPsicquicServicesByName {
    public static void main(String[] args) throws ClusterServiceException, ConverterException, IOException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,ddbj/embl/genbank,chebi,irefindex";
        /* MIQL Query */
        String query = "identifier:P51587 AND identifier:Q06609";
        /* PSICQUIC service */
        List psicquicServices = new ArrayList();
        psicquicServices.add("IntAct");
//        psicquicServices.add("MINT");

        /* Cluster data */
        InteractionCluster iC = new InteractionCluster();
        iC.addMIQLQuery(query);
        iC.setQuerySources(psicquicServices);
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
