package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.score.DefaultMIScoreCalculator;
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
        this.miScoreCalculator = new DefaultMIScoreCalculator<DefaultInteractionCluster>();
    }

    @Override
    public Iterator<DefaultInteractionCluster> getResults(){
        Iterator<DefaultInteractionCluster> toScore = super.getResults();
        DefaultInteractionCluster auxCluster = null;
        while(toScore.hasNext()){
            auxCluster = toScore.next();
            auxCluster.setScore(this.miScoreCalculator.computeScore(auxCluster));
        }
        return super.getResults();
    }

}
