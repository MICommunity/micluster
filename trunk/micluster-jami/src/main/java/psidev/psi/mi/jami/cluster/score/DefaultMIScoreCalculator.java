package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.DefaultInteractionCluster;
import psidev.psi.mi.jami.model.Interaction;

/**
 * Created by maitesin on 04/08/2014.
 */
public class DefaultMIScoreCalculator<I extends Interaction,T extends DefaultInteractionCluster<I>> extends AbstractMIScoreCalculator<T> {
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public DefaultMIScoreCalculator(String filename) {
        super(filename);
    }

    @Override
    public void computeScore(T interactions) {
        this.miScore.clear();
        double score = 0.0d;

        for(I interaction : interactions.getInteractions()){
            //TODO: extract methods and type from interaction
            //this.miScore.addMethod();
            //this.miScore.addType();
        }

        score += this.miScore.getMethodsScore();
        score += this.miScore.getTypesScore();

        interactions.setScore(score);
    }

}
