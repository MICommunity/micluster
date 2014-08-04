package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.model.Interaction;

/**
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreUnNormalizedStrategy extends AbstractMIScoreStrategy {
    public DefaultMIScoreUnNormalizedStrategy(MIScore miScore) {
        this.miScore = miScore;
    }

    public DefaultMIScoreUnNormalizedStrategy(String filename) {
        this.miScore = new DefaultUnNormalizedMIScore(filename);
    }

    @Override
    public Double getInteractorScore(Interaction interactor) {
        return null;
    }
}
