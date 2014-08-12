package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;

import java.util.*;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionClusterManager<I extends Interaction,T extends InteractionCluster<I>> implements InteractionClusterManager<I,T> {

    /********************************/
    /***   Abstract Constructor   ***/
    /********************************/
    public AbstractInteractionClusterManager(){
        this.interactionClusters = new ArrayList<T>();
        this.interaction2String = new HashMap<Interaction, String>();
        this.idGenerator = 0L;
        this.string2Interactor = new TreeMap<String, Interactor>();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public void clear(){
        this.interactionClusters.clear();
        this.interaction2String.clear();
        this.idGenerator = 0L;
    }

    /*****************************/
    /***   Protected Methods   ***/
    /*****************************/
    protected Long getNextId(){
        return ++this.idGenerator;
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected InteractorMerger merger = null;
    protected Collection<T> interactionClusters;
    protected Map<Interaction, String> interaction2String;
    protected Map<String, Interactor> string2Interactor;
    protected Long idGenerator;
}
