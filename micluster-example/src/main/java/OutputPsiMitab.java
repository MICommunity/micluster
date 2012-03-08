import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
import uk.ac.ebi.enfin.mi.cluster.EncoreBinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Print PSI MITAB results from clustered data
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class OutputPsiMitab {
    public static void print(InteractionCluster iC) throws ConverterException, IOException {
        /* Convert EncoreInteractions into BinaryInteractions */
        List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
        for(EncoreBinaryInteraction eI:iC.getInteractionMapping().values()){
            binaryInteractions.add(iConverter.getBinaryInteraction(eI));
        }
        /* Print PSI MITAB clustered binary interactions */
        PsimiTabWriter writer = new PsimiTabWriter();
        writer.write(binaryInteractions, System.out);
    }
}
