package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUniqueIdentification;

import java.io.InputStream;

/**
 * Created by maitesin on 12/06/2014.
 */
public class InteractionClusterManager {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public InteractionClusterManager(InputStream in){
        this.identification = null; //TODO: Create a way to identify unique
    }

    public void process(){
        //Step 2) Unify identifiers to be able to merge interactions

        //Step 3) Merge interactions and store that information in an Interaction Cluster object
        //this.cluster = InteractionClusterUtils.clusterInteractions(interactions, this.identification);

        //Step 4) Score merged interactions

    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private InteractionCluster cluster = null;
    private InteractionClusterUniqueIdentification identification = null;
}
