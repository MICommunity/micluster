package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.*;

/**
 * DefaultInteractionCluster is the default way to represent a ClusterInteraction.
 * It just uses the attributes provided by the abstract class AbstractInteractionCluster.
 *
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionCluster<T extends Interaction> extends AbstractInteractionCluster<T> {

    public DefaultInteractionCluster(Long Id) {
        super(Id);
    }

    @Override
    public Long getId() {
        return this.Id;
    }

    @Override
    public Collection<T> getInteractions() {
        return this.interactions;
    }
}
