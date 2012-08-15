import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import psidev.psi.mi.tab.model.Confidence;
import java.util.List;
import java.util.Map;

/**
 * Print example results from clustered data with miscore information
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class OutputScoreSampleData {
    public static void print(InteractionCluster iC){
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();

        /* PRINT SOME RESULTS */
        String fisrtSeparator = "\t";
        String secondSeparator = ",";

        /* Print interactors for clustered binary interactions including publication accessions */
        System.out.println("Print interactors for clustered binary interactions including publication accessions");
        System.out.println("interaction_location"+ fisrtSeparator + "interactor_A" + fisrtSeparator + "interactor_B" + fisrtSeparator +
                "score" + fisrtSeparator + "publications"+ fisrtSeparator + "types" + fisrtSeparator + "methods");
        for(Integer i:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(i);

            /* Get score */
            String score = "";
            List<Confidence> confidenceScores = eI.getConfidenceValues();
            for(Confidence confidenceScore:confidenceScores){
                if(confidenceScore.getType().equalsIgnoreCase("miscore")){
                    score = confidenceScore.getValue();
                }
            }

            /* Get publications */
            String publications = "";
            for(String publication:eI.getExperimentToPubmed().values()){
                publications += publication + secondSeparator;
            }
            publications = publications.substring(0,publications.length()-1);

            /* Get types */
            String types = "";
            for(String type:eI.getTypeToPubmed().keySet()){
                types += type + secondSeparator;
            }
            types = types.substring(0,types.length()-1);

            /* Get methods */
            String methods = "";
            for(String method:eI.getMethodToPubmed().keySet()){
                methods += method + secondSeparator;
            }
            methods = methods.substring(0,methods.length()-1);

            System.out.println(i + fisrtSeparator + eI.getInteractorA() + fisrtSeparator + eI.getInteractorB() + fisrtSeparator +
                    score + fisrtSeparator + publications + fisrtSeparator + types + fisrtSeparator + methods);
        }
        System.out.println("");

    }
}
