package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.*;

/**
 * Abstract class to implement InteractionCluster Interface. Provides a long value
 * as Identifier (Id) for the "group" of Interactions. Stores the Interactions as
 * a Collection and it is using an ArrayList to do that.
 *
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionCluster<T extends Interaction> implements InteractionCluster<T> {

    public AbstractInteractionCluster(Long Id){
        this.Id = Id;
        this.interactions = new ArrayList<T>();
    }

    @Override
    public boolean addInteraction(T interaction){
        if(this.interactions.contains(interaction)){
            return false;
        }
        this.interactions.add(interaction);
        return true;
    }

    @Override
    public void addInteractions(Collection<T> interactions){
        for (T interaction : interactions) {
            addInteraction(interaction);
        }
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected Long Id;
    protected Collection<T> interactions;

}
