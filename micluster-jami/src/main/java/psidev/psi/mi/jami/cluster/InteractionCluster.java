package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.io.input.ClusterInput;
import psidev.psi.mi.jami.cluster.io.input.MitabClusterInput;
import psidev.psi.mi.jami.cluster.io.output.ClusterOutput;
import psidev.psi.mi.jami.cluster.model.InteractionClusterStatus;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.commons.MIWriterOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.InteractorPool;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.impl.DefaultInteractorPool;
import psidev.psi.mi.jami.tab.extension.MitabInteractorPool;
import psidev.psi.mi.jami.tab.extension.factory.MitabWriterFactory;
import psidev.psi.mi.jami.utils.InteractionUtils;

import java.io.InputStream;

/**
 * Created by maitesin on 12/06/2014.
 */
public class InteractionCluster {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public InteractionCluster(InputStream in){
        this.input = new MitabClusterInput(in);
        //this.pool = new MitabInteractorPool()
        PsiJami.initialiseAllFactories();
    }

    public void process(){
        InteractionClusterStatus status = new InteractionClusterStatus();
        //Step 1) Read input and crete the basics for the cluster
        while(this.input.hasNext()){
            // add the interaction to an interaction pool
        }

        //Step 2) Unify identifiers to be able to merge interactions

        //Step 3) Merge interactions and store that information in an Interaction Cluster Status object

        //Step 4) Write output

    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    InteractorPool pool = null;
    ClusterInput input = null;
    ClusterOutput output = null;
}
