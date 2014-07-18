package psidev.psi.mi.jami.cluster.build;

import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.cluster.InteractionClusterManager;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.Xref;

/**
 * Created by maitesin on 12/06/2014.
 */
public class InteractionClusterBuilder {

    public static ModelledInteraction createModelledInteractionFromInteractionClusterStatus(InteractionCluster status){
        // TODO: Nowadays is a Modelled BINARY Interaction. We have to think a way to represent N-ARY Interaction where N > 2
        ModelledBinaryInteraction modelled = null;
        // TODO: Convert from Interaction Cluster Status to Modelled Binary Interaction
        return modelled;
    }

    public static InteractionClusterSummary createSummaryFromInteractionClusterStatus(InteractionClusterManager status){
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
