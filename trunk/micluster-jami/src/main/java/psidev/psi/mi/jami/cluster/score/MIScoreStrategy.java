package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

/**
 * Created by maitesin on 18/07/2014.
 */
public interface MIScoreStrategy {
    public Double getInteractorScore(Interaction interactor);
}
