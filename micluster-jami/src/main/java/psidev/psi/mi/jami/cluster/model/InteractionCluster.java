package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.impl.DefaultInteraction;

import java.util.Collection;
import java.util.Set;

/**
 * Created by maitesin on 25/07/2014.
 */
public interface InteractionCluster<T extends Interaction> {
    /**
     *
     * @return
     */
    public Long getId();

    /**
     *
     * @return
     */
    public Collection<T> getInteractions();

    /**
     *
     * @param interaction
     * @return
     */
    public boolean addInteraction(T interaction);

    /**
     *
     * @param interactions
     */
    public void addInteractions(Collection<T> interactions);
}
