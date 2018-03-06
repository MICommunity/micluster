package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.impl.DefaultConfidence;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;

import java.util.Collection;
import java.util.Iterator;

/**
 * Default implementation for the MIScoreManager
 * and that stores the calculated score within
 * the Interaction.
 *
 * Created by maitesin on 05/03/2015.
 */
public class DefaultMIScoreManager extends AbstractMIScoreManager {

    @Override
    public void calculateScoreAndSetIt(InteractionCluster interaction) {
        double score = this.miScoreCalculator.computeScore(interaction);
        for (Interaction i : (Collection<Interaction>) interaction.getInteractions()) {
            if (i instanceof InteractionEvidence) {
                InteractionEvidence evidence = (InteractionEvidence) i;
                evidence.getConfidences().add(new DefaultConfidence(new DefaultCvTerm("intact-miscore"), String.valueOf(score)));
            }
        }
    }

    @Override
    public void calculateScoreAndSetIt(Iterator iterator) {
        while (iterator.hasNext()) {
            this.calculateScoreAndSetIt((InteractionCluster) iterator.next());
        }
    }

    @Override
    public void calculateScoreAndSetIt(Collection collection) {
        for (InteractionCluster interactionCluster : (Collection<InteractionCluster>) collection) {
            this.calculateScoreAndSetIt(interactionCluster);
        }
    }

    @Override
    public double calculateScoreAndReturn(InteractionCluster interaction) {
        double score = this.miScoreCalculator.computeScore(interaction);
        return score;
    }
}
