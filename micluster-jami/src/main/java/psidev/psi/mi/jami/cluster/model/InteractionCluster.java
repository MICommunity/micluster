package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.impl.DefaultInteraction;

import java.util.Collection;
import java.util.Set;

/**
 * Represents all the Interactions that are equivalent.
 *
 * Created by maitesin on 25/07/2014.
 */
public interface InteractionCluster<T extends Interaction> {
    /**
     * Get the ID of the cluster.
     *
     * @return ID for this cluster
     */
    public Long getId();

    /**
     * Get all the interactions that have this cluster.
     *
     * @return Collection of a class that extends Interaction.
     */
    public Collection<T> getInteractions();

    /**
     * Add interaction to the cluster (if it is no already) and return false or true if it was or not already in the
     * Collection.
     *
     * @param interaction class that extends Interaction to add.
     * @return true if the interaction is properly added. False is it was there already.
     */
    public boolean addInteraction(T interaction);

    /**
     * Add interactions to the cluster (if they are not already).
     *
     * @param interactions Collection of a class that extends Interaction.
     */
    public void addInteractions(Collection<T> interactions);
}
