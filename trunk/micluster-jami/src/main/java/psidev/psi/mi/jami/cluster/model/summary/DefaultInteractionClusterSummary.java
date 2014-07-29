package psidev.psi.mi.jami.cluster.model.summary;

import psidev.psi.mi.jami.cluster.model.AbstractInteractionCluster;
import psidev.psi.mi.jami.model.*;

import java.util.*;

/**
 * Created by maitesin on 11/07/2014.
 */
public class DefaultInteractionClusterSummary extends AbstractInteractionClusterSummary{

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionClusterSummary() {
        this.experiments = new ArrayList<Experiment>();
        this.interactionDetectionMethod2Publication = new HashMap<CvTerm, List<Experiment>>();
        this.interactionTypes = new ArrayList<CvTerm>();
        this.participants = new ArrayList<DefaultParticipantClusterSummary>();
        this.publications = new ArrayList<Publication>();
    }

    /*******************/
    /***   Getters   ***/
    /*******************/
    public Collection<Experiment> getExperiments() {
        return experiments;
    }

    public Map<CvTerm, List<Experiment>> getInteractionDetectionMethod2Publication() {
        return interactionDetectionMethod2Publication;
    }

    public Collection<CvTerm> getInteractionTypes() {
        return interactionTypes;
    }

    public Collection<DefaultParticipantClusterSummary> getParticipants() {
        return participants;
    }

    public Collection<Publication> getPublications() {
        return publications;
    }
    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Collection<Experiment> experiments;
    private Map<CvTerm, List<Experiment>> interactionDetectionMethod2Publication;
    private Collection<CvTerm> interactionTypes;
    private Collection<DefaultParticipantClusterSummary> participants;
    private Collection<Publication> publications;

}
