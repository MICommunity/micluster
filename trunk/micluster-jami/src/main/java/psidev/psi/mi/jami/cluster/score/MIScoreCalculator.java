package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;

/**
 * Interface that allows you to calculate the score of
 * an specific InteractionCluster.
 *
 * Created by maitesin on 04/08/2014.
 */
public interface MIScoreCalculator<T extends InteractionCluster> {
    /**
     *
     * @param interactions
     * @return
     */
    public double computeScore(T interactions);
}
