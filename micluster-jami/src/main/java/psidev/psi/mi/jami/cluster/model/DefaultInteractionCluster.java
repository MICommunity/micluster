package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.model.Interaction;

import java.util.*;

/**
 * Created by maitesin on 12/06/2014.
 */
public class DefaultInteractionCluster<T extends Interaction> extends AbstractInteractionCluster<T> {

    public DefaultInteractionCluster(String Id) {
        super(Id);
    }

    @Override
    public String getId() {
        return this.Id;
    }

    @Override
    public Collection<T> getInteractions() {
        return this.interactions;
    }
}
