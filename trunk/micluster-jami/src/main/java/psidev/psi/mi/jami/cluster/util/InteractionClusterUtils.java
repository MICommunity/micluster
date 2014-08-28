package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.summary.DefaultInteractionClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.DefaultParticipantClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.ParticipantClusterSummary;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by maitesin on 17/07/2014.
 */
public class InteractionClusterUtils {

    public static Collection<InteractionClusterSummary> buildSummaries(Iterator<InteractionCluster<Interaction>> interactionClusters) {
        Collection<InteractionClusterSummary> summaries = new ArrayList<InteractionClusterSummary>();
        while (interactionClusters.hasNext()){
            summaries.add(InteractionClusterUtils.buildSummary(interactionClusters.next()));
        }
        return summaries;
    }

    public static InteractionClusterSummary buildSummary(InteractionCluster<Interaction> interactionCluster){
        DefaultInteractionClusterSummary summary = new DefaultInteractionClusterSummary();
        Iterator<Interaction> interactions = interactionCluster.getInteractions().iterator();
        Iterator<Participant> participantIterator = null;
        Interaction inter = null;
        while (interactions.hasNext()){
            //Get Interaction
            inter = interactions.next();
            //Experiments
            //TODO
            //Interaction Detection Method 2 Publications(Experiments)
            //TODO
            //Interaction Type
            summary.getInteractionTypes().add(inter.getInteractionType());
            //Participants
            participantIterator = inter.getParticipants().iterator();
            while(participantIterator.hasNext()) {
                summary.getParticipants().add(InteractionClusterUtils.buildParticipantSummary(participantIterator.next()));
            }
            //Publication
            //TODO
        }
        return summary;
    }

    public static ParticipantClusterSummary buildParticipantSummary(Participant participant){
        DefaultParticipantClusterSummary summary = new DefaultParticipantClusterSummary();
        //Biological Role 2 Experiments
        //summary.getBiologicalRole2ExperimentMap().put(participant.getBiologicalRole(), );
        //Experimental Role 2 Experiments
        //TODO
        //Feature 2 Experiment
        for(Feature feature : (Collection<Feature>) participant.getFeatures()){
            //summary.getFeature2ExperimentMap().put(feature, );
        }
        //Identification Method 2 Experiments
        //TODO
        //Interactor 2 Bio Roles
        //TODO
        //Interactor 2 Experimental Roles
        //TODO
        //Organism 2 Experiments
        //TODO
        return summary;
    }

}
