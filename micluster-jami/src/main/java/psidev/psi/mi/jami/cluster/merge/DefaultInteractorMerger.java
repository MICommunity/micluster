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
        Interactor interactor = new DefaultInteractor(in1.getFullName());
        //TODO
        return interactor;
    }
}
