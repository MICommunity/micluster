package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.cluster.model.summary.DefaultInteractionClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionClusterManager extends AbstractInteractionClusterManager {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionClusterManager(){
        this.cluster = new DefaultInteractionCluster();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    @Override
    public void clear() {
        //Reset data structures to the initial state.
        this.cluster.clear();
    }

    @Override
    public void process(Interaction interaction) {
        String id = this.cluster.getId(interaction);
        this.cluster.getInteractions(id).add(interaction);
    }

    @Override
    public void process(Iterator<Interaction> iterator) {
        while(iterator.hasNext()){
            this.process(iterator.next());
        }
    }

    @Override
    public void process(Collection<Interaction> collection) {
        process(collection.iterator());
    }

    @Override
    public InteractionClusterSummary getResults() {
        //TODO: convert from InteractionCluster object to InteractionClusterSummary
        return null;
    }

    @Override
    public void score() {
        //TODO: after process the interactions we can score them
    }
}
