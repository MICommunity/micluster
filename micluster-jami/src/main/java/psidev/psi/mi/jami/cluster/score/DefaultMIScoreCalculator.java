package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;

import java.util.Collection;

/**
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<T extends InteractionCluster> extends AbstractMIScoreCalculator<T> {
    @Override
    public double computeScore(Collection<T> interactions) {
        return 0;
    }
}
