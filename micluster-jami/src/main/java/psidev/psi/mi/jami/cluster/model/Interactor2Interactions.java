package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Collection;

/**
 * Created by maitesin on 15/08/2014.
 */
public interface Interactor2Interactions {
    /**
     * Get the Interactor that represents as merged Interactor and is used to compare with others.
     *
     * @return Interactor that compares with others.
     */
    Interactor getInteractor();

    /**
     * Set the Interactor to compare with others.
     *
     * @param interactor Interactor to compare with others.
     */
    void setInteractor(Interactor interactor);

    /**
     * Get the interactions that contain this Interactor.
     *
     * @return Collection of Interaction.
     */
    Collection<Interaction> getInteractions();
}
