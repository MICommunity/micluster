package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.model.Interactor;

/**
 * Created by maitesin on 31/07/2014.
 */
public interface InteractorMerger {
    boolean areSame(Interactor in1, Interactor in2);
    Interactor merge(Interactor in1, Interactor in2);
}
