package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.MethodTypePair;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;
import uk.ac.ebi.enfin.mi.score.scores.MIScore;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Default implementation for the MIScoreCalculator Interface.
 * <p>
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<I extends Interaction, T extends InteractionCluster<I>> extends AbstractMIScoreCalculator<T> {
    protected MIScore miscore;
    protected OLSCache olsCache;
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultMIScoreCalculator(String filename) {
        super(filename);
        olsCache = OLSCache.getInstance();
        if (this.miscore == null) {
            this.miscore = new MIScore();
        }
    }


    public double computeScore(T interactions) {

        double score = 0.0d;

        float a = 0.0f;
        float b = 0.0f;
        float typeScore = 0.0f;
        float methodScore = 0.0f;
        float publicationScore = 0.0f;

        Float typeWeight = 1.0f;
        Float methodWeight = 1.0f;
        Float publicationWeight = 1.0f;
        ArrayList<String> methods = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        Set<String> publications = new HashSet<String>();
        Map<String, Map<String, String>> mapOfMethodTerms = olsCache.getMapOfMethodTermsCache();
        Map<String, Map<String, String>> mapOfTypeTerms = olsCache.getMapOfTypeTermsCache();
        Map<MethodTypePair, Set<String>> methodTypeToPudmed = createMethodTypePublicationMapping(interactions);

        for (MethodTypePair methodType : methodTypeToPudmed.keySet()) {
            Set<String> uniquePublications = methodTypeToPudmed.get(methodType);

            String method = methodType.getMethod();
            String type = methodType.getType();

            for (int i = 0; i < uniquePublications.size(); i++) {
                methods.add(method);
                types.add(type);
            }

            publications.addAll(uniquePublications);
        }

        int numberOfPubmedIds = publications.size();

        if (this.miscore != null) {
            this.miscore.setTypeWeight(typeWeight);
            this.miscore.setMethodWeight(methodWeight);
            this.miscore.setPublicationWeight(publicationWeight);
            this.miscore.setMethodScore(methods, mapOfMethodTerms, null);
            this.miscore.setTypeScore(types, mapOfTypeTerms);
            this.miscore.setPublicationScore(numberOfPubmedIds, null);
            score = this.miscore.getScore();

            score = Double.valueOf(new DecimalFormat("#0.00").format(score));
        }

        return score;
    }

    public Map<MethodTypePair, Set<String>> createMethodTypePublicationMapping(T interactions) {

        Map<MethodTypePair, Set<String>> methodTypePublicationsMap = new HashMap<MethodTypePair, Set<String>>();

        for (I interaction : interactions.getInteractions()) {

            String method = null;
            String type = null;
            String publicationId = null;
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                method = evidence.getExperiment().getInteractionDetectionMethod().getMIIdentifier();
            }
            if (interaction.getInteractionType() != null) {
                type = interaction.getInteractionType().getMIIdentifier();
            }
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                publicationId = evidence.getExperiment().getPublication().getPubmedId();
            }
            MethodTypePair methodTypePair = new MethodTypePair(method, type);
            if (methodTypePublicationsMap.get(methodTypePair) == null) {
                Set<String> uniquePublications = new HashSet<String>();
                uniquePublications.add(publicationId);
                methodTypePublicationsMap.put(methodTypePair, uniquePublications);
            } else {
                methodTypePublicationsMap.get(methodTypePair).add(publicationId);
            }

        }
        return methodTypePublicationsMap;
    }

}
