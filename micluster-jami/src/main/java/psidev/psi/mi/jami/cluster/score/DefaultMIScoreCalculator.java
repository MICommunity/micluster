package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;

/**
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<T extends DefaultInteractionCluster> extends AbstractMIScoreCalculator<T> {
    @Override
    public double computeScore(T interactions) {
        return 0;
    }
}
