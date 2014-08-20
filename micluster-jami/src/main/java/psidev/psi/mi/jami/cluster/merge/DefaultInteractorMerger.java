package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultInteractor;
import psidev.psi.mi.jami.utils.comparator.xref.DefaultXrefComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

import java.util.*;


/**
 * Created by maitesin on 31/07/2014.
 */
public class DefaultInteractorMerger extends AbstractInteractorMerger {
    @Override
    public boolean areSame(Interactor in1, Interactor in2) {
        if (in1 == null || in2 == null) return false;
        if (in1 == in2) return true;
        //Check preferred identifier
        if( DefaultXrefComparator.areEquals(in1.getPreferredIdentifier(), in2.getPreferredIdentifier() ) ) return true;
        else{
            //TODO: wrong way. Change to use two list and check "manually" if they are equal
            //If not check the other identifiers
//            Set<Xref> ids1 = (Set<Xref>) in1.getIdentifiers();
//            Set<Xref> ids2 = (Set<Xref>) in2.getIdentifiers();
//            Set<Xref> intersection = new TreeSet<Xref>(xrefComparator);
//            intersection.addAll(ids1);
//            intersection.retainAll(ids2); //calculate the intersection between both sets
//            if(intersection.size() == 0) return false;
            List<Xref> ids1 = (List<Xref>) in1.getIdentifiers();
            List<Xref> ids2 = (List<Xref>) in2.getIdentifiers();
            List<Xref> auxList = null;
            Iterator<Xref> iter = null;
            if (ids1.size() > ids2.size()){
                auxList = ids1;
                iter = ids2.iterator();
            }
            else {
                auxList = ids2;
                iter = ids1.iterator();
            }
            while (iter.hasNext()){
                if(auxList.contains(iter.next()))
                    return true;
            }
            return false;
        }
    }

    @Override
    public Interactor merge(Interactor in1, Interactor in2) {
        if ( in1 == in2 ) return in1; //There is nothing to merge. Both are the same object
        if (! areSame(in1,in2)) return null;
        //Copy some information from the first Interactor parameter
        Interactor interactor = new DefaultInteractor(in1.getShortName());
        try {
            getEnricher().enrich(interactor, in1);
            getEnricher().enrich(interactor, in2);
        } catch (EnricherException e) {
            e.printStackTrace();
        }
        return interactor;
    }
}
