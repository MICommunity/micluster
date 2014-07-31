package psidev.psi.mi.jami.cluster.strategy;

import psidev.psi.mi.jami.model.Interaction;

/**
 * Created by maitesin on 31/07/2014.
 */
public interface InteractorMerger {
    boolean areSame(Interaction in1, Interaction in2);
    Interaction merge(Interaction in1, Interaction in2);
}
