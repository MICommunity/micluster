package uk.ac.ebi.enfin.mi.cluster.model;


import java.util.Comparator;

/**
 * Created by maitesin on 08/06/14.
 */
public class DefaultClusterComparator implements ClusterComparator {

    public int compare(EncoreInteraction o1, EncoreInteraction o2) {

        //Identifier
        int id1 = o1.getId(),
            id2 = o2.getId();

        if (id1 < id2) return -1;
        if (id1 > id2) return  1;

        //Interactors A
        String interactor1 = o1.getInteractorA(),
               interactor2 = o2.getInteractorA();

        int value = interactor1.compareTo(interactor2);
        if (value != 0) return value;

        //Interactors B
        interactor1 = o1.getInteractorB();
        interactor2 = o2.getInteractorB();

        value = interactor1.compareTo(interactor2);
        if (value != 0) return value;

        //Mapping ID DB Names
        String mapping1 = o1.getMappingIdDbNames(),
               mapping2 = o2.getMappingIdDbNames();

        value = mapping1.compareTo(mapping2);
        if(value != 0) return value;


        //These ones are equal
        return 0;
    }
}
