package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionCluster implements InteractionCluster {

    public AbstractInteractionCluster(){
        this.id2InteractionsMap = new HashMap<String, Collection<Interaction>>();
        this.interaction2StringMap = new HashMap<Interaction, String>();
        this.idGenerator = 0L;
    }

    protected String getNextId(){
        return (++this.idGenerator).toString();
    }

    @Override
    public void clear(){
        this.id2InteractionsMap.clear();
        this.interaction2StringMap.clear();
        this.idGenerator = 0L;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    protected Map<String,Collection<Interaction>> id2InteractionsMap;
    protected Map<Interaction, String> interaction2StringMap;
    protected Long idGenerator;
}
