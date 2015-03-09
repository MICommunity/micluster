package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.model.Interactor;

/**
 * Interface that allows the cluster algorithm to know if two different
 * interactors are the same and also provides a way to merge two interactors
 * if both are the same.
 *
 * Created by maitesin on 31/07/2014.
 */
public interface InteractorMerger {
    /**
     * Returns a boolean value that depends if the interactors are the same.
     *
     * @param in1 First interactor to check if these are the same.
     * @param in2 Second interactor to check if these are the same.
     * @return Returns true when both are the same. Otherwise returns false.
     * If both parameters are null returns true. If one the parameters is null
     * return false.
     */
    boolean areSame(Interactor in1, Interactor in2);

    /**
     * Returns a new Interactor based on the ones provided in the parameters.
     *
     * @param in1 First interactor to merge if these are the same.
     * @param in2 Second interactor to merge if these are the same.
     * @return Returns a merged Interactor based on the attributes of the
     * ones provided. If the interactors provided are not the same returns
     * null.
     */
    Interactor merge(Interactor in1, Interactor in2);
}
