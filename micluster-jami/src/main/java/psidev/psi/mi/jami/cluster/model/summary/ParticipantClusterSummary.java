package psidev.psi.mi.jami.cluster.model.summary;

import psidev.psi.mi.jami.model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maitesin on 17/07/2014.
 */
public class ParticipantClusterSummary {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public ParticipantClusterSummary() {
        this.biologicalRole2ExperimentMap = new HashMap<CvTerm, List<Experiment>>();
        this.experimentalRole2ExperimentMap = new HashMap<CvTerm, List<Experiment>>();
        this.feature2ExperimentMap = new HashMap<Feature, Experiment>();
        this.identificationMethod2ExperimentMap = new HashMap<CvTerm, List<Experiment>>();
        this.interactor2BioRoleMap = new HashMap<Interactor, List<CvTerm>>();
        this.interactor2ExperimentalRoleMap = new HashMap<Interactor, List<CvTerm>>();
        this.organism2ExperimentMap = new HashMap<Organism, List<Experiment>>();
    }

    /*******************/
    /***   Getters   ***/
    /*******************/
    public Map<CvTerm, List<Experiment>> getBiologicalRole2ExperimentMap() {
        return biologicalRole2ExperimentMap;
    }

    public Map<CvTerm, List<Experiment>> getExperimentalRole2ExperimentMap() {
        return experimentalRole2ExperimentMap;
    }

    public Map<Feature, Experiment> getFeature2ExperimentMap() {
        return feature2ExperimentMap;
    }

    public Map<CvTerm, List<Experiment>> getIdentificationMethod2ExperimentMap() {
        return identificationMethod2ExperimentMap;
    }

    public Map<Interactor, List<CvTerm>> getInteractor2BioRoleMap() {
        return interactor2BioRoleMap;
    }

    public Map<Interactor, List<CvTerm>> getInteractor2ExperimentalRoleMap() {
        return interactor2ExperimentalRoleMap;
    }

    public Map<Organism, List<Experiment>> getOrganism2ExperimentMap() {
        return organism2ExperimentMap;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Map<CvTerm, List<Experiment>> biologicalRole2ExperimentMap;
    private Map<CvTerm, List<Experiment>> experimentalRole2ExperimentMap;
    private Map<Feature, Experiment> feature2ExperimentMap;
    private Map<CvTerm,List<Experiment>> identificationMethod2ExperimentMap;
    private Map<Interactor, List<CvTerm>> interactor2BioRoleMap;
    private Map<Interactor, List<CvTerm>> interactor2ExperimentalRoleMap;
    private Map<Organism,List<Experiment>> organism2ExperimentMap;
}
