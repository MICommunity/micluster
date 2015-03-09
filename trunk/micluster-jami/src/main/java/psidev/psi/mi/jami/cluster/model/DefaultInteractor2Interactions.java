package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Collection;

/**
 * DefaultInteractor2Interactions is the default way to represent an Interactor2Interactions.
 * It just uses the attributes provided by the abstract class AbstractInteractor2Interactions
 *
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
