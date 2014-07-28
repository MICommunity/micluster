package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.model.Interaction;

import java.util.*;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionCluster extends AbstractInteractionCluster {

    public DefaultInteractionCluster(){
        super();
    }

    @Override
    public Collection<Interaction> getInteractions(String id) {
        if (this.id2InteractionsMap.get(id) == null) this.id2InteractionsMap.put(id, new ArrayList<Interaction>());
        return this.id2InteractionsMap.get(id);
    }

    @Override
    public String getId(Interaction interaction){
        //TODO: find a way to know what id has is for an interaction
        Iterator<Interaction> iter = null;
        for (String key : this.id2InteractionsMap.keySet()) {
            iter = this.id2InteractionsMap.get(key).iterator();
            if (iter.hasNext())
                if (InteractionClusterUtils.compareById(iter.next(), interaction))
                    return key;
        }
        return this.getNextId();
    }

}
