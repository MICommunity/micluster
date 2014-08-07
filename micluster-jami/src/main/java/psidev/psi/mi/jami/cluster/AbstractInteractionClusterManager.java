package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.score.MIScoreCalculator;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.model.Interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionClusterManager<I extends Interaction,T extends InteractionCluster<I>> implements InteractionClusterManager<I,T> {

    /********************************/
    /***   Abstract Constructor   ***/
    /********************************/
    public AbstractInteractionClusterManager(){
        this.interactionClusters = new ArrayList<T>();
        this.interaction2StringMap = new HashMap<Interaction, String>();
        this.idGenerator = 0L;
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public void clear(){
        this.interactionClusters.clear();
        this.interaction2StringMap.clear();
        this.idGenerator = 0L;
    }

    /*****************************/
    /***   Protected Methods   ***/
    /*****************************/
    protected String getNextId(){
        return (++this.idGenerator).toString();
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected InteractorMerger merger = null;

    protected Collection<T> interactionClusters;
    protected Map<Interaction, String> interaction2StringMap;
    protected Long idGenerator;
}
