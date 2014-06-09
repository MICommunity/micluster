package uk.ac.ebi.enfin.mi.cluster.model;

import psidev.psi.mi.tab.model.CrossReference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by maitesin on 03/06/2014.
 */
public class InteractorCluster implements Serializable {
    /************************/
    /***   Constructors   ***/
    /************************/
    public InteractorCluster() {
    }

    public InteractorCluster(Map<String, String> acs, Map<String, List<String>> otherAcs, List<CrossReference> organisms) {
        this.acs = acs;
        this.otherAcs = otherAcs;
        this.organisms = organisms;
    }

    /*******************************/
    /***   Getters and Setters   ***/
    /*******************************/
    public Map<String, String> getAcs() {
        return acs;
    }

    public void setAcs(Map<String, String> acs) {
        this.acs = acs;
    }

    public Map<String, List<String>> getOtherAcs() {
        return otherAcs;
    }

    public void setOtherAcs(Map<String, List<String>> otherAcs) {
        this.otherAcs = otherAcs;
    }

    public List<CrossReference> getOrganisms() {
        return organisms;
    }

    public void setOrganisms(List<CrossReference> organisms) {
        this.organisms = organisms;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Map<String, String> acs;
    private Map<String, List<String>> otherAcs;
    private List<CrossReference> organisms;

}