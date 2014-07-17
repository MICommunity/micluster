package psidev.psi.mi.jami.cluster.util;

import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.cluster.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.InteractionClusterStatus;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.Xref;

/**
 * Created by maitesin on 12/06/2014.
 */
public class InteractionClusterUtils {

    public static ModelledInteraction createModelledInteractionFromInteractionClusterStatus(InteractionClusterStatus status){
        // TODO: Nowadays is a Modelled BINARY Interaction. We have to think a way to represent N-ARY Interaction where N > 2
        ModelledBinaryInteraction modelled = null;
        // TODO: Convert from Interaction Cluster Status to Modelled Binary Interaction
        return modelled;
    }

    public static InteractionClusterSummary createSummaryFromInteractionClusterStatus(InteractionCluster status){
        InteractionClusterSummary summary = null;
        // TODO: Convert from Interaction Cluster Status to Interaction Cluster Summary
        return summary;
    }

    public static Xref extractUniqueIdentifier(/*TODO: Parameters???*/){
        Xref uniqueId = null;
        // TODO:???
        return uniqueId;
    }
}
