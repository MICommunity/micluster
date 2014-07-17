package psidev.psi.mi.jami.cluster.cache;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Xref;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by maitesin on 14/07/2014.
 */
public class InteractionCache {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public InteractionCache() {
        this.interactionsMap = new HashMap<Xref, Collection<Interaction>>();
    }

    /**************************/
    /***   Public methods   ***/
    /**************************/
    public Collection<Interaction> getInteractions(Xref id) {
        return interactionsMap.get(id);
    }

    public void addInteraction(Interaction interaction) {
        Collection list = null;
        Xref id = null;
        if ( ! interaction.getXrefs().isEmpty() ) {
            id = (Xref) interaction.getXrefs().toArray()[0]; //get the first identifier in the list
            if ( ! this.interactionsMap.containsKey(id) )
                this.interactionsMap.put(id, new LinkedList<Interaction>());
            list = this.interactionsMap.get(id);
            list.add(interaction);
        }

    }
    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Map<Xref, Collection<Interaction>> interactionsMap;
}
