package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;

/**
 * Abstract class that implements the MIScoreManager interface.
 * It has hardcoded a file with a default properties in case
 * we do not want or we can not connect to the OLS to get
 * the current ones.
 *
 * Created by maitesin on 05/03/2015.
 */
public abstract class AbstractMIScoreManager<I extends Interaction,T extends InteractionCluster<I>> implements MIScoreManager<I,T> {

    /*********************************/
    /***   Abstract Constructors   ***/
    /*********************************/
    public AbstractMIScoreManager(String filename) {
        this.miScoreCalculator = new DefaultMIScoreCalculator<I, T>(filename);
    }
    public AbstractMIScoreManager() {
        this("scoreCategories.properties");
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected MIScoreCalculator<T> miScoreCalculator;
}
