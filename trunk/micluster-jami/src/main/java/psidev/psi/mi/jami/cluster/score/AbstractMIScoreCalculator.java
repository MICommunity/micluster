package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by maitesin on 04/08/2014.
 */
public abstract class AbstractMIScoreCalculator<T extends InteractionCluster> implements MIScoreCalculator<T> {

    /***********************/
    /***   Constructor   ***/
    /***********************/
    public AbstractMIScoreCalculator(String filename){
        this.miScore = new DefaultUnNormalizedMIScore(filename);
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected MIScore miScore;
}
