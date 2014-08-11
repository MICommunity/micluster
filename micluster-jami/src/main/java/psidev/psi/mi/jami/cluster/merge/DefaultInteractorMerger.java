package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultInteractor;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

import java.util.Set;
import java.util.TreeSet;


/**
 * Created by maitesin on 31/07/2014.
 */
public class DefaultInteractorMerger extends AbstractInteractorMerger {
    @Override
    public boolean areSame(Interactor in1, Interactor in2) {
        if (in1 == null || in2 == null) return false;
        if (in1 == in2) return true;
        //Check preferred identifier
        UnambiguousXrefComparator xrefComparator = new UnambiguousXrefComparator();
        if( xrefComparator.compare(in1.getPreferredIdentifier(), in2.getPreferredIdentifier()) == 0 ) return true;
        else{
            //If not check the other identifiers
            Set<Xref> ids1 = (Set<Xref>) in1.getIdentifiers();
            Set<Xref> ids2 = (Set<Xref>) in2.getIdentifiers();
            Set<Xref> intersection = new TreeSet<Xref>(xrefComparator);
            intersection.addAll(ids1);
            intersection.retainAll(ids2); //calculate the intersection between both sets
            if(intersection.size() == 0) return false;
        }
        return true;
    }

    @Override
    public Interactor merge(Interactor in1, Interactor in2) {
        if (! areSame(in1,in2)) return null;
        //Copy some information from the first Interactor parameter
        Interactor interactor = new DefaultInteractor(in1.getFullName());
        interactor.setInteractorType(in1.getInteractorType());
        interactor.setShortName(in1.getShortName());
        interactor.setOrganism(in1.getOrganism());
        //Merge both interactor information
        //TODO: Could be better to put the information of both interactors in a set to avoid duplicates?
        //TODO: For example, put the identifiers of both interactors in a set, then we don't have twice the same Id
        //Aliases
        interactor.getAliases().addAll(in1.getAliases());
        interactor.getAliases().addAll(in2.getAliases());
        //Annotations
        interactor.getAnnotations().addAll(in1.getAnnotations());
        interactor.getAnnotations().addAll(in2.getAnnotations());
        //Checksums
        interactor.getChecksums().addAll(in1.getChecksums());
        interactor.getChecksums().addAll(in2.getChecksums());
        //Identifiers
        interactor.getIdentifiers().addAll(in1.getIdentifiers());
        interactor.getIdentifiers().addAll(in2.getIdentifiers());
        //Xrefs
        interactor.getXrefs().addAll(in1.getXrefs());
        interactor.getXrefs().addAll(in2.getXrefs());
        return interactor;
    }
}
