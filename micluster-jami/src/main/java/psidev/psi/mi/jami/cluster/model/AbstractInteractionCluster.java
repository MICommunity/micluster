package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionCluster<T extends Interaction> implements InteractionCluster<T> {

    public AbstractInteractionCluster(String Id){
        this.Id = Id;
        this.interactions = new ArrayList<T>();
        this.score = 0.0;
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected String Id;
    protected Collection<T> interactions;
    protected double score;
}
