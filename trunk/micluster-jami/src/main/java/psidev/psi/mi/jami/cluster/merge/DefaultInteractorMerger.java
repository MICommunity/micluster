package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.Xref;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by maitesin on 31/07/2014.
 */
public class DefaultInteractorMerger extends AbstractInteractorMerger {
    @Override
    public boolean areSame(Interaction in1, Interaction in2) {
        //Check if the rigid are equal
        if( ! in1.getRigid().equals(in2.getRigid()) ) return false;
        //Check if the interaction types are equal
        if ( ! in1.getInteractionType().equals(in2.getInteractionType()) ) return false;
        //Check if the Identifiers are the same
        if ( ! checkIdentifiers(in1.getIdentifiers(), in2.getIdentifiers())) return false;
        //Check if the Participants are the same
        if ( ! checkParticipants(in1.getParticipants(), in2.getParticipants())) return false;

        //Congrats these interactions are the same
        return true;
    }

    private boolean checkParticipants(Collection<Participant> participants1, Collection<Participant> participants2) {
        if (participants1.size() != participants2.size()) return false;

        List part1 = (List) participants1;
        List part2 = (List) participants2;

        Collections.sort(part1);
        Collections.sort(part2);

        for(int i=0; i<part1.size(); ++i)
            if( ! part1.get(i).equals(part2.get(i)) )
                return false;

        return true;
    }

    private boolean checkIdentifiers(Collection<Xref> ids1, Collection<Xref> ids2) {
        if ( ids1.size() != ids2.size() ) return false;

        List ids1List = (List) ids1;
        List ids2List = (List) ids2;

        Collections.sort(ids1List);
        Collections.sort(ids2List);

        for(int i=0; i<ids1List.size(); ++i)
            if ( ! ids1List.get(i).equals(ids2List.get(i)) )
                return false;

        return true;
    }

    @Override
    public Interaction merge(Interaction in1, Interaction in2) {
        if ( ! areSame(in1, in2)) return null;
        Interaction newOne = null;

        //Rigid
        newOne.setRigid(in1.getRigid());
        //Interaction type
        newOne.setInteractionType(in1.getInteractionType());
        //Short name
        newOne.setShortName(in1.getShortName());
        //Created date
        newOne.setCreatedDate(in1.getCreatedDate());
        //Updated date
        newOne.setUpdatedDate(in1.getUpdatedDate());
        //Identifiers
        newOne.getIdentifiers().addAll(in1.getIdentifiers());
        //Participants
        newOne.getParticipants().addAll(in1.getParticipants());
        //Xrefs
        newOne.getXrefs().addAll(in1.getXrefs());
        newOne.getXrefs().addAll(in2.getXrefs());
        //Annotations
        newOne.getAnnotations().addAll(in1.getAnnotations());
        newOne.getAnnotations().addAll(in2.getAnnotations());
        //Checksums
        newOne.getChecksums().addAll(in1.getChecksums());
        newOne.getChecksums().addAll(in2.getChecksums());

        return newOne;
    }
}
