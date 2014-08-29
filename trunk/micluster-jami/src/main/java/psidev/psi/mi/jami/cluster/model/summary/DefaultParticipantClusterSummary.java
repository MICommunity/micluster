package psidev.psi.mi.jami.cluster.model.summary;

import psidev.psi.mi.jami.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maitesin on 17/07/2014.
 */
public class DefaultParticipantClusterSummary extends AbstractParticipantClusterSummary {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultParticipantClusterSummary() {
        this.biologicalRole2Experiment = new HashMap<CvTerm, List<Experiment>>();
        this.experimentalRole2Experiment = new HashMap<CvTerm, List<Experiment>>();
        this.feature2Experiment = new HashMap<Feature, Experiment>();
        this.identificationMethod2Experiment = new HashMap<CvTerm, List<Experiment>>();
        this.interactor2BioRole = new HashMap<Interactor, List<CvTerm>>();
        this.interactor2ExperimentalRole = new HashMap<Interactor, List<CvTerm>>();
        this.organism2Experiment = new HashMap<Organism, List<Experiment>>();
    }

    /*******************/
    /***   Getters   ***/
    /*******************/
    public Map<CvTerm, List<Experiment>> getBiologicalRole2Experiment() {
        return biologicalRole2Experiment;
    }
    public Map<CvTerm, List<Experiment>> getExperimentalRole2Experiment() {
        return experimentalRole2Experiment;
    }
    public Map<Feature, Experiment> getFeature2Experiment() {
        return feature2Experiment;
    }
    public Map<CvTerm, List<Experiment>> getIdentificationMethod2Experiment() {
        return identificationMethod2Experiment;
    }
    public Map<Interactor, List<CvTerm>> getInteractor2BioRole() {
        return interactor2BioRole;
    }
    public Map<Interactor, List<CvTerm>> getInteractor2ExperimentalRole() {
        return interactor2ExperimentalRole;
    }
    public Map<Organism, List<Experiment>> getOrganism2Experiment() {
        return organism2Experiment;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Map<CvTerm, List<Experiment>> biologicalRole2Experiment;
    private Map<CvTerm, List<Experiment>> experimentalRole2Experiment;
    private Map<Feature, Experiment> feature2Experiment;
    private Map<CvTerm, List<Experiment>> identificationMethod2Experiment;
    private Map<Interactor, List<CvTerm>> interactor2BioRole;
    private Map<Interactor, List<CvTerm>> interactor2ExperimentalRole;
    private Map<Organism,List<Experiment>> organism2Experiment;
}
