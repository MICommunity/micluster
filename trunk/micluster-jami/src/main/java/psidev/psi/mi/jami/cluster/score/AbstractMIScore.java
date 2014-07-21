package psidev.psi.mi.jami.cluster.score;

import java.util.Map;

/**
 * Created by maitesin on 18/07/2014.
 */
public abstract class AbstractMIScore implements MIScore {
    public AbstractMIScore(String filename){
        //TODO: Load values of the file into the Maps
    }
    protected Map<String, Double> methods;
    protected Map<String, Double> types;
    protected Double methodWeight;
    protected Double typeWeight;

}
