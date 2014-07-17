package psidev.psi.mi.jami.cluster.model.summary;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interactor;

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
        this.interactor2BioRoleMap = new HashMap<Interactor, Collection<CvTerm>>();
        this.interactor2ExperimentalRoleMap = new HashMap<Interactor, Collection<CvTerm>>();
    }

    /*******************/
    /***   Getters   ***/
    /*******************/
    public Map<CvTerm, List<Experiment>> getBiologicalRole2Experiment() {
        return biologicalRole2ExperimentMap;
    }

    public Map<CvTerm, List<Experiment>> getExperimentalRole2Experiment() {
        return experimentalRole2ExperimentMap;
    }

    public Map<Feature, Experiment> getFeature2Experiment() {
        return feature2ExperimentMap;
    }

    public Map<Interactor, Collection<CvTerm>> getInteractor2BioRole() {
        return interactor2BioRoleMap;
    }

    public Map<Interactor, Collection<CvTerm>> getInteractor2ExperimentalRole() {
        return interactor2ExperimentalRoleMap;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Map<CvTerm, List<Experiment>> biologicalRole2ExperimentMap;
    private Map<CvTerm, List<Experiment>> experimentalRole2ExperimentMap;
    private Map<Feature, Experiment> feature2ExperimentMap;
    private Map<Interactor, Collection<CvTerm>> interactor2BioRoleMap;
    private Map<Interactor, Collection<CvTerm>> interactor2ExperimentalRoleMap;
}
