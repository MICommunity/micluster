import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import java.util.List;
import java.util.Map;

/**
 * Print example results from clustered data
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class OutputSampleData {
    public static void print(InteractionCluster iC){
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();

        /* PRINT SOME RESULTS */
        String fisrtSeparator = "\t";
        String secondSeparator = ",";

        /* Print interactors for clustered binary interactions including publication accessions */
        System.out.println("Print interactors for clustered binary interactions including publication accessions");
        System.out.println("interaction_location"+ fisrtSeparator + "interactor_A" + fisrtSeparator + "interactor_B" + fisrtSeparator + "publications");
        for(Integer i:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(i);

            /* Get publications */
            String publications = "";
            for(String publication:eI.getExperimentToPubmed().values()){
                publications += publication + secondSeparator;
            }
            publications = publications.substring(0,publications.length()-1);

            System.out.println(i + fisrtSeparator + eI.getInteractorA() + fisrtSeparator + eI.getInteractorB() + fisrtSeparator + publications);
        }
        System.out.println("");

        /* Print interactors and its location in the interactionMapping */
        System.out.println("Print interactors and its location in the interactionMapping");
        System.out.println("interactor"+ fisrtSeparator + "interaction_location");
        for(String interactor:interactorMapping.keySet()){
            System.out.println(interactor + fisrtSeparator + interactorMapping.get(interactor));
        }
        System.out.println("");

        /* Print interactors and publication */
        System.out.println("Print synonyms for interactors");
        System.out.println("synonym"+ fisrtSeparator + "interactor");
        for(String synonym:interactorSynonyms.keySet()){
            System.out.println(interactorSynonyms.get(synonym) + fisrtSeparator + synonym);
        }
    }
}
