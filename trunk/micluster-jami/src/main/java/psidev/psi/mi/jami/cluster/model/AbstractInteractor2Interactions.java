package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class to implement Interactor2Interactions interface. Provides two
 * attributes to store the Interactor that "represents" at least one of the
 * participants of each Interactions. Those Interactions are stored in a
 * collection that is implemented by an ArrayList.
 *
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
