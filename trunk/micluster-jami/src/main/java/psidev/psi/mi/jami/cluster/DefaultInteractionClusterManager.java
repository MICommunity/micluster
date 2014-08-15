package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.DefaultInteractor2Interactions;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.Interactor2Interactions;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultInteractor;

import java.util.*;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionClusterManager extends AbstractInteractionClusterManager<Interaction,InteractionCluster<Interaction>> {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionClusterManager(){}

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
        Iterator<Participant> participantIterator = interaction.getParticipants().iterator();
        while(participantIterator.hasNext()){
            Interactor interactor = participantIterator.next().getInteractor();
            //Merge all interactors that match with current Interactor's ID
            Interactor2Interactions merged = getMergedInteractor(interactor);
            //Update the Interactor->Collection<Interaction> Map and the ID->Interactor Map
            updateId2InteractorMapAndInteractor2InteractionsMap(getAllIdsFromAnInteractor(interactor), merged);
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

    /**
     * Collect all the IDs for the interactor passed as parameter. Just take the IDs that the object has already.
     *
     * @param interactor is the interactor that you want to take all its IDs.
     * @return a list of Xref with the IDs that carries the interactor passed as parameter. If the interactor does not
     * have IDs at all it will return the Collections.EMPTY_LIST object.
     */
    private List<Xref> getAllIdsFromAnInteractor(Interactor interactor){
        if(interactor.getPreferredIdentifier() == null) return Collections.EMPTY_LIST;
        List<Xref> ids = new ArrayList<Xref>();
        ids.add(interactor.getPreferredIdentifier());
        ids.addAll(interactor.getIdentifiers());
        return ids;
    }

    private Interactor2Interactions getMergedInteractor(Interactor interactor) {
        Interactor2Interactions merged = new DefaultInteractor2Interactions(getInteractorMerger().merge(
                new DefaultInteractor(interactor.getFullName(),interactor.getPreferredIdentifier()),
                interactor)); //initialize the merged identifier with the interactor passed as parameter
        if(this.id2Interactor.containsKey(interactor.getPreferredIdentifier()))
            merged.getInteractions().addAll(this.id2Interactor.get(interactor.getPreferredIdentifier()).getInteractions());
        Interactor aux = null;
        List<Xref> idsList = getAllIdsFromAnInteractor(interactor);
        if ( idsList.size() > 0) {
            Iterator<Xref> ids = idsList.iterator();
            Xref id = null;
            while (ids.hasNext()) {
                id = ids.next();
                aux = getInteractorMerger().merge(merged.getInteractor(),this.id2Interactor.get(id).getInteractor());
                merged.getInteractions().addAll(this.id2Interactor.get(id).getInteractions());
                merged.setInteractor(aux);
            }
        }
        return merged;
    }

    private void updateId2InteractorMapAndInteractor2InteractionsMap(List<Xref> allIdsFromAnInteractor, Interactor2Interactions merged) {
        Iterator<Xref> idsIterator = allIdsFromAnInteractor.iterator();
        Xref id = null;
        while(idsIterator.hasNext()){
            id = idsIterator.next();
            this.id2Interactor.put(id, merged);
        }
    }

}
