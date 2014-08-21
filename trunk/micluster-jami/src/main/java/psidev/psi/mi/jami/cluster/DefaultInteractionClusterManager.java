package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
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
        //This loop is for updating and merging the current information with the new one carried by this interaction
        while(participantIterator.hasNext()){
            Interactor interactor = participantIterator.next().getInteractor();
            //Merge all interactors that match with current Interactor's ID
            Interactor2Interactions merged = getMergedInteractor(interactor);
            //Update the Interactor->Collection<Interaction> Map and the ID->Interactor Map
            updateMergedInteraction(merged, interaction);
            updateId2InteractorMapAndInteractor2InteractionsMap(merged);
        }
        //This loop is to retrieve all the interaction where these interactor are part of it.
        participantIterator = interaction.getParticipants().iterator();
        List<Collection<Interaction>> listCollectionInteractions =
                new ArrayList<Collection<Interaction>>(interaction.getParticipants().size());
        while (participantIterator.hasNext()){
            Interactor interactor = participantIterator.next().getInteractor();
            listCollectionInteractions.add(this.id2Interactor.get(interactor.getPreferredIdentifier()).getInteractions());
        }
        List<Interaction> intersection = getIntersection(listCollectionInteractions);
        intersection.remove(interaction); //We just removed the current Interaction
        InteractionCluster cluster = null;
        if(intersection.size() > 0){
            //We already have one interaction with that information. If there several all of them should be
            //pointing to the same InteractionCluster
            cluster = this.interaction2InteractionCluster.get(intersection.get(0));
            this.interaction2InteractionCluster.put(interaction, cluster);
        }
        else{
            //We should create a new InteractionCluster to add that new Interaction
            cluster = new DefaultInteractionCluster(getNextId());
            this.interactionClusters.add(cluster);
            this.interaction2InteractionCluster.put(interaction, cluster);
        }
        cluster.getInteractions().add(interaction);
    }

    private List<Interaction> getIntersection(List<Collection<Interaction>> listCollectionInteractions) {
        Collection<Interaction> minCollection = getMinSizeCollection(listCollectionInteractions);
        Iterator<Collection<Interaction>> iterator = listCollectionInteractions.iterator();
        while (iterator.hasNext()){
            Iterator<Interaction> innerIterator = minCollection.iterator();
            Collection<Interaction> interactions = iterator.next();
            Collection<Interaction> auxCollection = new ArrayList<Interaction>();
            while(innerIterator.hasNext()){
                Interaction inter = innerIterator.next();
                if (interactions.contains(inter))
                    auxCollection.add(inter);
            }
            if (auxCollection.size() > 0)
                minCollection = auxCollection;
            else
                return Collections.EMPTY_LIST;
        }
        return (List<Interaction>) minCollection;
    }

    private Collection<Interaction> getMinSizeCollection(List<Collection<Interaction>> listCollectionInteractions) {
        Iterator<Collection<Interaction>> iterator = listCollectionInteractions.iterator();
        Collection<Interaction> auxCollection = null;
        Collection<Interaction> minCollection = null;
        if (iterator.hasNext()){
            minCollection = iterator.next();
            int minSize = minCollection.size();
            while(iterator.hasNext()){
                auxCollection = iterator.next();
                if(auxCollection.size() < minSize)
                    minCollection = auxCollection;
            }
        }
        return minCollection;
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
        if(interactor.getIdentifiers().contains(interactor.getPreferredIdentifier()))
            return (List<Xref>) interactor.getIdentifiers();
        else {
            List<Xref> ids = new ArrayList<Xref>();
            ids.add(interactor.getPreferredIdentifier());
            ids.addAll(interactor.getIdentifiers());
            return ids;
        }
    }

    /**
     *
     * @param interactor
     * @return
     */
    private Interactor2Interactions getMergedInteractor(Interactor interactor) {
        Interactor imerged = new DefaultInteractor(interactor.getShortName(),interactor.getPreferredIdentifier());
        Interactor2Interactions merged = new DefaultInteractor2Interactions(getInteractorMerger().merge(
                imerged,
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
                if(this.id2Interactor.containsKey(id)) {
                    aux = getInteractorMerger().merge(merged.getInteractor(), this.id2Interactor.get(id).getInteractor());
                    updateMergedInteractions(merged, this.id2Interactor.get(id).getInteractions());
                    merged.setInteractor(aux);
                }
            }
        }
        return merged;
    }

    private void updateMergedInteractions(Interactor2Interactions merged, Collection<Interaction> interactions) {
        Iterator<Interaction> iterator = interactions.iterator();
        Interaction aux = null;
        while (iterator.hasNext()) {
            updateMergedInteraction(merged, iterator.next());

        }
    }

    private void updateMergedInteraction(Interactor2Interactions merged, Interaction interaction) {
        if ( ! merged.getInteractions().contains(interaction))
            merged.getInteractions().add(interaction);
    }

    /**
     *
     * @param merged
     */
    private void updateId2InteractorMapAndInteractor2InteractionsMap(Interactor2Interactions merged) {
        List<Xref> allIdsFromAnInteractor = getAllIdsFromAnInteractor(merged.getInteractor());
        Iterator<Xref> idsIterator = allIdsFromAnInteractor.iterator();
        Xref id = null;
        while(idsIterator.hasNext()){
            id = idsIterator.next();
            this.id2Interactor.put(id, merged);
        }
    }

}
