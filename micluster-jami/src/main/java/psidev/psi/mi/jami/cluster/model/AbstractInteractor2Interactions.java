package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by maitesin on 15/08/2014.
 */
public abstract class AbstractInteractor2Interactions implements Interactor2Interactions {
    protected AbstractInteractor2Interactions(Interactor interactor) {
        this.interactor = interactor;
        this.interactions = new ArrayList<Interaction>();
    }

    protected Interactor interactor;
    protected Collection<Interaction> interactions;
}
