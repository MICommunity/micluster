package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Xref;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by maitesin on 18/07/2014.
 */
public class InteractionClusterIdentificationById implements InteractionClusterUniqueIdentification {
    @Override
    public boolean areEqualsTheseInteractions(Interaction i1, Interaction i2) {
        //TODO: create a way to know if two Interactions are different
        return false;
    }
}
