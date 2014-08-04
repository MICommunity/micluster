package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.model.Interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionClusterManager extends AbstractInteractionClusterManager {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionClusterManager(){
        this.merger = new DefaultInteractorMerger();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    @Override
    public void clear() {
        //Reset data structures to the initial state.
        super.clear();
    }

    @Override
    public void process(Interaction interaction) {
        Iterator<InteractionCluster> iter = null;
        String right_key = null;
        for(String key : this.id2InteractionsMap.keySet()){
            iter = this.id2InteractionsMap.get(key).iterator();
            if (iter.hasNext())
                if(this.merger.areSame(iter.next(),interaction))
                    right_key = key;

        }
        if(right_key == null){
            //New cluster
            this.id2InteractionsMap.put(this.getNextId(), new ArrayList<InteractionCluster>());
        }
        this.id2InteractionsMap.get(right_key).add(interaction);
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
    public Map<String, Collection<InteractionCluster>> getResults() {
        return this.id2InteractionsMap;
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/

}
