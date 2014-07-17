package psidev.psi.mi.jami.cluster.io.input;

import psidev.psi.mi.jami.commons.MIDataSourceOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.factory.MIDataSourceFactory;

/**
 * Created by maitesin on 12/06/2014.
 */
abstract class AbstractClusterInput implements ClusterInput {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    protected AbstractClusterInput() {
        PsiJami.initialiseAllFactories();
        optionfactory =  MIDataSourceOptionFactory.getInstance();
        dataSourceFactory = MIDataSourceFactory.getInstance();
    }

    /*************************/
    /***   Public Method   ***/
    /*************************/
    @Override
    public void remove() {
        throw new IllegalStateException("Never should call that method");
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected MIDataSourceOptionFactory optionfactory;
    protected MIDataSourceFactory dataSourceFactory;
}
