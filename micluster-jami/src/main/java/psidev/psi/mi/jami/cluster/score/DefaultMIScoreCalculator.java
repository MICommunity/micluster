package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;

/**
 * Default implementation for the MIScoreCalculator Interface.
 * <p/>
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<I extends Interaction, T extends InteractionCluster<I>> extends AbstractMIScoreCalculator<T> {
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

        double a = 0.0;
        double b = 0.0;
        double typeScore = 0.0;
        double methodScore = 0.0f;
        double publicationScore = 0.0f;

        Float typeWeight = 1.0f;
        Float methodWeight = 1.0f;
        Float publicationWeight = 1.0f;


        for (I interaction : interactions.getInteractions()) {
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                this.getMiScore().addMethod(evidence.getExperiment().getInteractionDetectionMethod().getMIIdentifier());
            }
            if (interaction.getInteractionType() != null) {
                this.getMiScore().addType(interaction.getInteractionType().getMIIdentifier());
            }
            if (interaction instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) interaction;
                this.getMiScore().addPublication(evidence.getExperiment().getPublication().getPubmedId());
            }
        }

        methodScore = this.getMiScore().getMethodsScore();
        typeScore = this.getMiScore().getTypesScore();

        a = a + (typeScore * typeWeight);
        b = b + typeWeight;

        a = a + (methodScore * methodWeight);
        b = b + methodWeight;

        a = a + (publicationScore * publicationWeight);
        b = b + publicationWeight;

        return score;
    }

}
