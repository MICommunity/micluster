package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by maitesin on 25/07/2014.
 */
public interface InteractionClusterManager<I extends Interaction,T extends InteractionCluster<I>> {
    /**
     * Cluster this interaction with the ones that already have.
     *
     * @param interaction Interaction to process.
     */
    public void process(I interaction);

    /**
     * Traverse all Interactions in the Iterator and for each one call the method process for Interaction.
     *
     * @param iterator Iterator of Interaction to process.
     */
    public void process(Iterator<I> iterator);

    /**
     * Get the Iterator of the Collection of Interaction passed as parameter and calls the method process for Iterator.
     *
     * @param collection Collection of Interaction to process.
     */
    public void process(Collection<I> collection);

    /**
     * Clear the content of the data structures.
     */
    public void clear();

    /**
     * Return an Iterator of a class that extends InteractionCluster.
     *
     * @return Iterator of a class that extends InteractionCluster.
     */
    public Iterator<T> getResults();
}
