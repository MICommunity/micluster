package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 18/07/2014.
 */
public abstract class AbstractMIScoreStrategy implements MIScoreStrategy {
    public AbstractMIScoreStrategy(MIScore miScore) {
        this.miScore = miScore;
    }

    protected MIScore miScore;
}
