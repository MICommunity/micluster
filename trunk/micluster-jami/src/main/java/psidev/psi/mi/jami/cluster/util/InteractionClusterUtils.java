package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.summary.DefaultInteractionClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.DefaultParticipantClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.cluster.model.summary.ParticipantClusterSummary;
import psidev.psi.mi.jami.model.*;

import java.util.*;

/**
 * Created by maitesin on 17/07/2014.
 */
public class InteractionClusterUtils {

    /**************************/
    /***   Public Methods   ***/
    /**************************/
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
            if(inter instanceof InteractionEvidence) {
                InteractionEvidence interactionEvidence = (InteractionEvidence) inter;
                //Experiments
                summary.getExperiments().add(interactionEvidence.getExperiment());
                //Interaction Detection Method 2 Publications(Experiments)
                addExperimen2Map(summary.getInteractionDetectionMethod2Publication(),interactionEvidence.getExperiment().getInteractionDetectionMethod(), interactionEvidence.getExperiment());
                //Publication
                summary.getPublications().add(interactionEvidence.getExperiment().getPublication());
            }
            //Interaction Type
            summary.getInteractionTypes().add(inter.getInteractionType());
            //Participants
            participantIterator = inter.getParticipants().iterator();
            while(participantIterator.hasNext()) {
                summary.getParticipants().add(InteractionClusterUtils.buildParticipantSummary(participantIterator.next()));
            }
        }
        return summary;
    }

    public static ParticipantClusterSummary buildParticipantSummary(Participant participant){
        DefaultParticipantClusterSummary summary = new DefaultParticipantClusterSummary();
        Interaction inter = participant.getInteraction();
        if(inter instanceof InteractionEvidence) {
            InteractionEvidence interactionEvidence = (InteractionEvidence) inter;
            //Biological Role 2 Experiments
            addExperimen2Map(summary.getBiologicalRole2Experiment(), participant.getBiologicalRole(),interactionEvidence.getExperiment());
            if(participant instanceof ParticipantEvidence) {
                ParticipantEvidence participantEvidence = (ParticipantEvidence) participant;
                //Experimental Role 2 Experiments
                addExperimen2Map(summary.getExperimentalRole2Experiment(), participantEvidence.getExperimentalRole(), interactionEvidence.getExperiment());
                //Identification Method 2 Experiments
                for (CvTerm identificationMethod : participantEvidence.getIdentificationMethods()){
                    addExperimen2Map(summary.getIdentificationMethod2Experiment(), identificationMethod, interactionEvidence.getExperiment());
                }
                //Interactor 2 Experimental Roles
                addRole2Map(summary.getInteractor2ExperimentalRole(), participant.getInteractor(), participantEvidence.getExperimentalRole());
            }
            //Feature 2 Experiment
            for (Feature feature : (Collection<Feature>) participant.getFeatures()) {
                summary.getFeature2Experiment().put(feature, interactionEvidence.getExperiment());
            }
            //Interactor 2 Bio Roles
            addRole2Map(summary.getInteractor2BioRole(),participant.getInteractor(), participant.getBiologicalRole());
            //Organism 2 Experiments
            if(summary.getOrganism2Experiment().containsKey(participant.getInteractor().getOrganism())){
                //It is already in the map
                summary.getOrganism2Experiment().get(participant.getInteractor().getOrganism()).add(interactionEvidence.getExperiment());
            }
            else{
                //New entry in the map
                List<Experiment> experiments = new ArrayList<Experiment>();
                experiments.add(interactionEvidence.getExperiment());
                summary.getOrganism2Experiment().put(participant.getInteractor().getOrganism(),experiments);
            }

        }
        return summary;
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/
    private static void addExperimen2Map(Map<CvTerm,List<Experiment>> map, CvTerm cvTerm, Experiment experiment){
        if(map.containsKey(cvTerm)){
            //It is already in the map
            map.get(cvTerm).add(experiment);
        }
        else{
            //New entry in the map
            List<Experiment> experiments = new ArrayList<Experiment>();
            experiments.add(experiment);
            map.put(cvTerm, experiments);
        }
    }

    private static void addRole2Map(Map<Interactor,List<CvTerm>> map, Interactor interactor, CvTerm role){
        if(map.containsKey(interactor)){
            //It is already in the map
            map.get(interactor).add(role);
        }
        else {
            //New entry in the map
            List<CvTerm> roles = new ArrayList<CvTerm>();
            roles.add(role);
            map.put(interactor,roles);
        }
    }


}
