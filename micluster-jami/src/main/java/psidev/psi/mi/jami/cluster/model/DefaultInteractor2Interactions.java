package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Collection;

/**
 * Created by maitesin on 15/08/2014.
 */
public class DefaultInteractor2Interactions extends AbstractInteractor2Interactions {
    public DefaultInteractor2Interactions(Interactor interactor) {
        super(interactor);
    }

    @Override
    public Interactor getInteractor() {
        return this.interactor;
    }

    @Override
    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }
    @Override
    public Collection<Interaction> getInteractions() {
        return this.interactions;
    }
}
