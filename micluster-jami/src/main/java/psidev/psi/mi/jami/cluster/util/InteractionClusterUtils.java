package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.cluster.io.input.ClusterInput;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by maitesin on 17/07/2014.
 */
public class InteractionClusterUtils {
    public static Collection<Interaction> readData(ClusterInput input){
        Collection<Interaction> interactions = new ArrayList<Interaction>();
        while(input.hasNext()){
            interactions.add(input.next());
        }
        return interactions;
    }

    public static InteractionCluster clusterInteractions(Collection<Interaction> interactions, InteractionClusterUniqueIdentification identification){
        //TODO: unique identification of the interactions in the cluster
        InteractionCluster cluster = new InteractionCluster();


        return cluster;
    }

    public static Iterator getIteratorFromInteractionCluster(InteractionCluster cluster) {
        return null;
    }
}
