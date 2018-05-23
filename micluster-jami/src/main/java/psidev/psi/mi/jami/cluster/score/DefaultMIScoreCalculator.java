package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;
import uk.ac.ebi.enfin.mi.score.scores.*;
import uk.ac.ebi.enfin.mi.score.scores.MIScore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for the MIScoreCalculator Interface.
 * <p/>
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<I extends Interaction, T extends InteractionCluster<I>> extends AbstractMIScoreCalculator<T> {
    protected MIScore miscore;
    protected  OLSCache olsCache;
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultMIScoreCalculator(String filename) {
        super(filename);
        olsCache=OLSCache.getInstance();
        if (this.miscore == null){
            this.miscore = new MIScore();
        }
    }

    @Override
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
        ArrayList<String> publications = new ArrayList<String>();
        Map<String, Map<String,String>> mapOfMethodTerms=olsCache.getMapOfMethodTermsCache();
        Map<String, Map<String,String>> mapOfTypeTerms=olsCache.getMapOfTypeTermsCache();

        for (I interaction : interactions.getInteractions()) {
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                methods.add(evidence.getExperiment().getInteractionDetectionMethod().getMIIdentifier());
            }
            if (interaction.getInteractionType() != null) {
                types.add(interaction.getInteractionType().getMIIdentifier());
            }
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                publications.add(evidence.getExperiment().getPublication().getPubmedId());
            }
        }

        int numberOfPubmedIds=publications.size();

        if (this.miscore != null){
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

}
