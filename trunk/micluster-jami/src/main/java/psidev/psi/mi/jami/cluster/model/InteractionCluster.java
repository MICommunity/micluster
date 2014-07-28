package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Set;

/**
 * Created by maitesin on 25/07/2014.
 */
public interface InteractionCluster {

    public void clear();
    public String getId(Interaction interaction);
    public Collection<Interaction> getInteractions(String id);
}
