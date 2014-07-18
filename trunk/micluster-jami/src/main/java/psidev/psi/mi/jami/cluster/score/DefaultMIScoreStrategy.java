package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.model.Interactor;

/**
 * Created by maitesin on 18/07/2014.
 */
public class DefaultMIScoreStrategy extends AbstractMIScoreStrategy {
    public DefaultMIScoreStrategy(MIScore miScore) {
        super(miScore);
    }

    @Override
    public Double getInteractorScore(Interactor interactor) {
        Double result = 0.0d;
        String type = null;//TODO: get type from the interator
        String method = null;//TODO: get method from the interactor
        if (type != null) result += this.miScore.getTypeScore(type);
        if (method != null) result += this.miScore.getMethodScore(method);
        return result;
    }
}
