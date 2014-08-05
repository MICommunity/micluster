package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;

/**
 * Created by maitesin on 04/08/2014.
 */
public interface MIScoreCalculator<T extends InteractionCluster> {
    double computeScore(T interactions);
}
