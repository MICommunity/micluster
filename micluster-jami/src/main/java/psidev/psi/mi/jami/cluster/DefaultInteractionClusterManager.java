package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.model.Interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionClusterManager extends AbstractInteractionClusterManager<Interaction,InteractionCluster<Interaction>> {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionClusterManager(){
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
        if (interaction != null) {
            Iterator<InteractionCluster<Interaction>> iteratorCluster = this.interactionClusters.iterator();
            InteractionCluster interactionClusterAux = null;
            Iterator<Interaction> iteratorInteraction = null;
            Long right_key = null;
            while (iteratorCluster.hasNext()) {
                interactionClusterAux = iteratorCluster.next();
                iteratorInteraction = interactionClusterAux.getInteractions().iterator();
                //TODO: check the interactors no the interactions
                if (iteratorInteraction.hasNext() && InteractionClusterUtils.areSame(iteratorInteraction.next(), interaction, getInteractorMerger())) {
                    interactionClusterAux.getInteractions().add(interaction);
                    right_key = interactionClusterAux.getId();
                    break;
                }
            }
            if (right_key == null) {
                //New cluster
                DefaultInteractionCluster defaultInteractionCluster = new DefaultInteractionCluster(this.getNextId());
                defaultInteractionCluster.getInteractions().add(interaction);
                this.interactionClusters.add(defaultInteractionCluster);
            }
        }
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

    /**
     *
     * @return
     */
    public InteractorMerger getInteractorMerger(){
        if(this.merger == null) {
            this.merger = new DefaultInteractorMerger();
        }
        return this.merger;
    }
    
    @Override
    public Iterator<InteractionCluster<Interaction>> getResults() {
        return this.interactionClusters.iterator();
    }

    /**
     *
     * @param merger
     */
    public void setInteractorMeger(InteractorMerger merger){
        if(merger != null) this.merger = merger;
    }
    
    /***************************/
    /***   Private Methods   ***/
    /***************************/

}
