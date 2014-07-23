package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.io.input.ClusterInput;
import psidev.psi.mi.jami.cluster.io.input.DefaultMitabClusterInput;
import psidev.psi.mi.jami.cluster.io.output.ClusterOutput;
//import psidev.psi.mi.jami.cluster.io.output.DefaultMitabClusterOutput;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUniqueIdentification;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractorPool;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by maitesin on 12/06/2014.
 */
public class InteractionClusterManager {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public InteractionClusterManager(InputStream in){
        this.identification = null; //TODO: Create a way to identify unique
        this.input = new DefaultMitabClusterInput(in);
        //this.output = new DefaultMitabClusterOutput();
        PsiJami.initialiseAllFactories();
    }

    public void process(){
        //Step 1) Read input and crete the basics for the cluster
        Collection<Interaction> interactions = InteractionClusterUtils.readData(this.input);

        //Step 2) Unify identifiers to be able to merge interactions

        //Step 3) Merge interactions and store that information in an Interaction Cluster object
        this.cluster = InteractionClusterUtils.clusterInteractions(interactions, this.identification);

        //Step 4) Score merged interactions

        //Step 5) Write output
        this.output.write(InteractionClusterUtils.getIteratorFromInteractionCluster(this.cluster));
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private ClusterInput input = null;
    private ClusterOutput output = null;
    private InteractionCluster cluster = null;
    private InteractionClusterUniqueIdentification identification = null;
}
