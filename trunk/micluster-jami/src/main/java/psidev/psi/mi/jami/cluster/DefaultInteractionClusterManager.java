package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.model.summary.ParticipantClusterSummary;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.Xref;

import java.util.*;

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
//        if (interaction != null) {
//            Iterator<InteractionCluster<Interaction>> iteratorCluster = this.interactionClusters.iterator();
//            InteractionCluster interactionClusterAux = null;
//            Iterator<Interaction> iteratorInteraction = null;
//            Long right_key = null;
//            while (iteratorCluster.hasNext()) {
//                interactionClusterAux = iteratorCluster.next();
//                iteratorInteraction = interactionClusterAux.getInteractions().iterator();
//                //TODO: check the interactors no the interactions
//                if (iteratorInteraction.hasNext() && areSame(iteratorInteraction.next(), interaction)) {
//                    interactionClusterAux.getInteractions().add(interaction);
//                    right_key = interactionClusterAux.getId();
//                    break;
//                }
//            }
//            if (right_key == null) {
//                //New cluster
//                DefaultInteractionCluster defaultInteractionCluster = new DefaultInteractionCluster(this.getNextId());
//                defaultInteractionCluster.getInteractions().add(interaction);
//                this.interactionClusters.add(defaultInteractionCluster);
//            }
//        }
        if(interaction != null){
            Iterator<Participant> interactorIterator = interaction.getParticipants().iterator();
            Participant participant = null;
            Interactor interactor = null, merged = null;
            List<Xref> existingId = null, allIds = null;
            while (interactorIterator.hasNext()){
                participant = interactorIterator.next();
                interactor = participant.getInteractor();
                allIds = new ArrayList<Xref>(interactor.getIdentifiers().size());
                allIds.add(interactor.getPreferredIdentifier());
                allIds.addAll(interactor.getIdentifiers());
                existingId = checkExistingIdsInTheMapOfId2Interactor(interactor);
                merged = getMergedInteractorOfAllExistingIds(existingId);
                updateId2InteractorMap(merged, allIds);
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
    private List<Xref> checkExistingIdsInTheMapOfId2Interactor(Interactor interactor) {
        List<Xref> existingId = new ArrayList<Xref>(interactor.getIdentifiers().size());
        //First process the preferred Id
        if(interactor.getPreferredIdentifier() != null && this.id2Interactor.containsKey(interactor.getPreferredIdentifier())){
            existingId.add(interactor.getPreferredIdentifier());
        }
        //Process other Ids
        Iterator<Xref> ids = interactor.getIdentifiers().iterator();
        Xref id = null;
        while(ids.hasNext()){
            id = ids.next();
            if(this.id2Interactor.containsKey(id)) existingId.add(id);
        }
        return existingId;
    }

    private Interactor getMergedInteractorOfAllExistingIds(List<Xref> existingId) {
        Interactor merged = null;
        if(existingId.size() > 0){
            if(existingId.size() >= 2){
                Iterator<Xref> iter = existingId.iterator();
                while(iter.hasNext()){
                    //TODO
                }
            }
            else {
                //There is just one element
                return this.id2Interactor.get(existingId.get(0));
            }
        }
        return merged;
    }

    private void updateId2InteractorMap(Interactor interactor, List<Xref> allIds) {
        Iterator<Xref> iter = allIds.iterator();
        while(iter.hasNext()){
            this.id2Interactor.put(iter.next(), interactor);
        }
    }
}
