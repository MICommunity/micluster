package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Collection;

/**
 * Created by maitesin on 15/08/2014.
 */
public interface Interactor2Interactions {
    Interactor getInteractor();
    void setInteractor(Interactor interactor);
    Collection<Interaction> getInteractions();
}
