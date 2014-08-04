package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by maitesin on 25/07/2014.
 */
public interface InteractionClusterManager<T extends Interaction> {
    public void process(Interaction interaction);
    public void process(Iterator<Interaction> iterator);
    public void process(Collection<Interaction> collection);
    public void clear();
    public Map<String,Collection<T>> getResults();
}
