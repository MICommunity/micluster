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
     *
     * @param interaction
     */
    public void process(Interaction interaction);

    /**
     *
     * @param iterator
     */
    public void process(Iterator<Interaction> iterator);

    /**
     *
     * @param collection
     */
    public void process(Collection<Interaction> collection);

    /**
     *
     */
    public void clear();

    /**
     *
     * @return
     */
    public Iterator<T> getResults();
}
