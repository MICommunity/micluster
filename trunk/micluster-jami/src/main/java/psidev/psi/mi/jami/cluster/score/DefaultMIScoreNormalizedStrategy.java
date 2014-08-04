package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.model.Interaction;

/**
 * Created by maitesin on 18/07/2014.
 */
public class DefaultMIScoreNormalizedStrategy extends AbstractMIScoreStrategy {
    public DefaultMIScoreNormalizedStrategy(MIScore miScore) {
        this.miScore = miScore;
    }
    public DefaultMIScoreNormalizedStrategy(String filename) {
        this.miScore = new DefaultNormalizedMIScore(filename);
    }

    @Override
    public Double getInteractorScore(Interaction interaction) {
        Double result = 0.0d;
        String type = null;//TODO: get type from the interator
        String method = null;//TODO: get method from the interactor
        if (type != null) result += this.miScore.getTypeScore(type);
        if (method != null) result += this.miScore.getMethodScore(method);
        return result;
    }
}
