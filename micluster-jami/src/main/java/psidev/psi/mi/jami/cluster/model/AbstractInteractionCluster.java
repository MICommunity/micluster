package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.*;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionCluster<T extends Interaction> implements InteractionCluster<T> {

    public AbstractInteractionCluster(Long Id){
        this.Id = Id;
        this.interactions = new ArrayList<T>();
    }

    @Override
    public boolean addInteraction(T interaction){
        if( this.interactions.contains(interaction) ){
            return false;
        }
        this.interactions.add(interaction);
        return true;
    }

    @Override
    public void addInteractions(Collection<T> interactions){
        Iterator<T> iterator = interactions.iterator();
        while(iterator.hasNext()){
            addInteraction(iterator.next());
        }
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected Long Id;
    protected Collection<T> interactions;

}
