package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.score.MIScoreStrategy;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionClusterManager implements InteractionClusterManager<InteractionCluster> {

    /********************************/
    /***   Abstract Constructor   ***/
    /********************************/
    public AbstractInteractionClusterManager(){
        this.id2InteractionsMap = new HashMap<String, Collection<InteractionCluster>>();
        this.interaction2StringMap = new HashMap<Interaction, String>();
        this.idGenerator = 0L;
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public void clear(){
        this.id2InteractionsMap.clear();
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
    protected MIScoreStrategy miScore = null;

    protected Map<String,Collection<InteractionCluster>> id2InteractionsMap;
    protected Map<Interaction, String> interaction2StringMap;
    protected Long idGenerator;
}
