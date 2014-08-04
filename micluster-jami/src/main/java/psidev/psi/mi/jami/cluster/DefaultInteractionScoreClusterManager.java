package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.score.DefaultMIScoreUnNormalizedStrategy;

import java.util.Iterator;

/**
 * Created by maitesin on 04/08/2014.
 */
public class DefaultInteractionScoreClusterManager extends DefaultInteractionClusterManager {
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultInteractionScoreClusterManager(String filename){
        super();
        this.miScore = new DefaultMIScoreUnNormalizedStrategy(filename);
    }

    @Override
    public Iterator<InteractionCluster> getResults(){
        return this.miScore.calculateScore(super.getResults());
    }

}
