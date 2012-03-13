import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Cluster binary interactions from a PSICQUIC service URL
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class InputPsicquicServiceByUrl {
    public static void main(String[] args) throws ClusterServiceException, IOException, ConverterException {
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl";
        /* PSICQUIC URL */
        String query = "identifier:P37173";
        URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/" + query);
        PsimiTabReader mitabReader = new PsimiTabReader(false);
        List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
        binaryInteractions.addAll(mitabReader.read(intactQuery));

        /* Cluster data */
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(binaryInteractions.iterator());
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
