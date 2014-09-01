package psidev.psi.mi.jami.cluster;

import org.apache.log4j.Logger;
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
    /**
     * Cleans the content of the data structures
     */
    @Override
    public void clear() {
        //Reset data structures to the initial state.
        if(log.isInfoEnabled()) log.info("Cleaning the structure");
        super.clear();
    }

    /**
     * Receives an interaction to process and cluster. For that this method follows this steps:
     * 1) Check if there are already interactors of this interaction. If there are merge them, else add the interactor.
     * 2) For every interactor of this interaction find all interactions that contains it. And find the intersection of
     *    that list of groups.
     * 3) With the intersection of the Interactions we have all Interactions that match with the current one. Depending
     *    on how many Interactions we have in the intersection we have to do:
     *    i)   For None Interactions: This Interaction does not have a cluster, then create one.
     *    ii)  For One Interaction: We just have to add the current Interaction to this cluster.
     *    iii) For Two or More Interactions: Merge their clusters in a new one and add the current Interaction to it.
     *
     * @param interaction interaction to process and cluster
     */
    @Override
    public void process(Interaction interaction) {
        if(log.isInfoEnabled()) log.info("Processing interaction: " + interaction.toString());
        Iterator<Participant> participantIterator = interaction.getParticipants().iterator();
        //This loop is for updating and merging the current information with the new one carried by this interaction
        while(participantIterator.hasNext()){
            Interactor interactor = participantIterator.next().getInteractor();
            if(log.isInfoEnabled()) log.info("Processing interactor: " + interactor.toString());
            //Merge all interactors that match with current Interactor's ID
            Interactor2Interactions merged = getMergedInteractor(interactor);
            if(log.isInfoEnabled()) log.info("Merged interactor: " + merged.toString());
            //Update the Interactor->Collection<Interaction> Map and the ID->Interactor Map
            updateMergedInteraction(merged, interaction);
            if(log.isInfoEnabled()) log.info("Updating all references from interaction to the old interactors to merged one");
            updateId2Interactor(merged);
        }
        //This loop is to retrieve all the interaction where these interactor are part of it.
        participantIterator = interaction.getParticipants().iterator();
        List<Collection<Interaction>> listCollectionInteractions =
                new ArrayList<Collection<Interaction>>(interaction.getParticipants().size());
        while (participantIterator.hasNext()){
            Interactor interactor = participantIterator.next().getInteractor();
            listCollectionInteractions.add(this.id2Interactor.get(interactor.getPreferredIdentifier()).getInteractions());
        }
        if(log.isInfoEnabled()) log.info("Calculating the intersection of the interactions");
        List<Interaction> intersection = getIntersection(listCollectionInteractions);
        intersection.remove(interaction); //We just removed the current Interaction
        InteractionCluster cluster = null;
        if(intersection.size() > 0){
            if(intersection.size() == 1) {
                //We already have one interaction with that information. If there several all of them should be
                //pointing to the same InteractionCluster
                if(log.isInfoEnabled()) log.info("Exist one cluster that match this interaction");
                cluster = this.interaction2InteractionCluster.get(intersection.get(0));
                cluster.addInteraction(interaction);
            }
            else{
                //Intersection size larger than 1. That means we have to merge the InteractionClusters
                if(log.isInfoEnabled()) log.info("Merge several existing clusters in the same one");
                cluster = new DefaultInteractionCluster(getNextId());
                InteractionCluster auxCluster = null;
                Interaction auxInteraction = null;
                Iterator<Interaction> interactionIterator = intersection.iterator();
                while (interactionIterator.hasNext()){
                    auxInteraction = interactionIterator.next();
                    auxCluster = this.interaction2InteractionCluster.get(auxInteraction);
                    cluster.addInteractions(auxCluster.getInteractions());
                    this.interactionClusters.remove(this.interaction2InteractionCluster.get(auxInteraction));
                    //this.interaction2InteractionCluster.put(auxInteraction, cluster);
                }
                updateInteractionsInInteractionCluster(cluster);
                this.interactionClusters.add(cluster);
                cluster.addInteraction(interaction);
            }
        }
        else{
            //We should create a new InteractionCluster to add that new Interaction
            if(log.isInfoEnabled()) log.info("Create a new cluster for this interaction");
            cluster = new DefaultInteractionCluster(getNextId());
            this.interactionClusters.add(cluster);
            cluster.addInteraction(interaction);
        }
        this.interaction2InteractionCluster.put(interaction, cluster);
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
     * Return back the InteractorMerger if has one, if not create a DefaultInteractorMerger and return it.
     *
     * @return the current InteractorMerger
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
     * Set the InteractorMerger to be able to merge different Interactors
     *
     * @param merger InteractorMerger provided to use it in the cluster algorithm
     */
    public void setInteractorMerger(InteractorMerger merger){
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


    private void updateInteractionsInInteractionCluster(InteractionCluster cluster) {
        Iterator<Interaction> interactionIterator = cluster.getInteractions().iterator();
        while(interactionIterator.hasNext())
            this.interaction2InteractionCluster.put(interactionIterator.next(), cluster);
    }

    private void updateMergedInteractions(Interactor2Interactions merged, Collection<Interaction> interactions) {
        Iterator<Interaction> iterator = interactions.iterator();
        Interaction aux = null;
        while (iterator.hasNext()) {
            updateMergedInteraction(merged, iterator.next());

        }
    }

    /**
     *
     *
     * @param merged Interactor2Interaction to store the Interaction object (if is not already there) passed as parameter
     * @param interaction Interaction to store in the Interactor2Interaction object passed as parameter
     */
    private void updateMergedInteraction(Interactor2Interactions merged, Interaction interaction) {
        if ( ! merged.getInteractions().contains(interaction))
            merged.getInteractions().add(interaction);
    }

    /**
     * Given an Interactor2Interaction object extract all IDs from it and update their references into the id2Interactor
     * Map.
     *
     * @param merged Interactor2Interaction instance to update all the IDs references to the Interactors to this one.
     */
    private void updateId2Interactor(Interactor2Interactions merged) {
        List<Xref> allIdsFromAnInteractor = getAllIdsFromAnInteractor(merged.getInteractor());
        Iterator<Xref> idsIterator = allIdsFromAnInteractor.iterator();
        Xref id = null;
        while(idsIterator.hasNext()){
            id = idsIterator.next();
            this.id2Interactor.put(id, merged);
        }
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Logger log = Logger.getLogger(DefaultInteractionClusterManager.class);

}
