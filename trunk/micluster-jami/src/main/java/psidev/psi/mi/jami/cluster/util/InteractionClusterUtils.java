package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;

import java.util.Iterator;
import java.util.List;

/**
 * Created by maitesin on 17/07/2014.
 */
public class InteractionClusterUtils {

    public static boolean areSame(Interaction i1, Interaction i2, InteractorMerger merger){
        Iterator<Participant> participants1 = i1.getParticipants().iterator();
        Iterator<Participant> participants2 = i2.getParticipants().iterator();
        //TODO: we have to think a way to be able to sort interactors
        return true;
    }
}
