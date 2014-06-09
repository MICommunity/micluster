package uk.ac.ebi.enfin.mi.cluster.model;

import psidev.psi.mi.jami.binary.impl.DefaultBinaryInteraction;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.Xref;

import java.util.List;

/**
 * Created by maitesin on 03/06/14.
 */
public class BinaryInteractionJamiCluster extends DefaultBinaryInteraction {
    /************************/
    /***   Constructors   ***/
    /************************/
    public BinaryInteractionJamiCluster() {
        super();
    }

    public BinaryInteractionJamiCluster(String shortName) {
        super(shortName);
    }

    public BinaryInteractionJamiCluster(String shortName, CvTerm type) {
        super(shortName, type);
    }

    public BinaryInteractionJamiCluster(Participant participantA, Participant participantB) {
        super(participantA, participantB);
    }

    public BinaryInteractionJamiCluster(String shortName, Participant participantA, Participant participantB) {
        super(shortName, participantA, participantB);
    }

    public BinaryInteractionJamiCluster(String shortName, CvTerm type, Participant participantA, Participant participantB) {
        super(shortName, type, participantA, participantB);
    }

    public BinaryInteractionJamiCluster(CvTerm complexExpansion) {
        super(complexExpansion);
    }

    public BinaryInteractionJamiCluster(String shortName, CvTerm type, CvTerm complexExpansion) {
        super(shortName, type, complexExpansion);
    }

    public BinaryInteractionJamiCluster(Participant participantA, Participant participantB, CvTerm complexExpansion) {
        super(participantA, participantB, complexExpansion);
    }

    public BinaryInteractionJamiCluster(String shortName, Participant participantA, Participant participantB, CvTerm complexExpansion) {
        super(shortName, participantA, participantB, complexExpansion);
    }

    public BinaryInteractionJamiCluster(String shortName, CvTerm type, Participant participantA, Participant participantB, CvTerm complexExpansion) {
        super(shortName, type, participantA, participantB, complexExpansion);
    }

    /*******************************/
    /***   Getters and Setters   ***/
    /*******************************/
    public List<Xref> getPublications() {
        return publications;
    }

    public void setPublications(List<Xref> publications) {
        this.publications = publications;
    }
    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private List<Xref> publications;
}
