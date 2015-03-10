package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;

/**
 * Default implementation for the MIScoreCalculator Interface.
 *
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<I extends Interaction,T extends InteractionCluster<I>> extends AbstractMIScoreCalculator<T> {
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultMIScoreCalculator(String filename) {
        super(filename);
    }

    @Override
    public double computeScore(T interactions) {
        this.getMiScore().clear();
        double score = 0.0d;

        for(I interaction : interactions.getInteractions()){
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                this.getMiScore().addMethod(evidence.getExperiment().getInteractionDetectionMethod().getMIIdentifier());
            }
            if (interaction.getInteractionType() != null) {
                this.getMiScore().addType(interaction.getInteractionType().getMIIdentifier());
            }
        }

        score += this.getMiScore().getMethodsScore();
        score += this.getMiScore().getTypesScore();

        return score;
    }

}
