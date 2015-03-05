package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by maitesin on 05/03/2015.
 */
public interface MIScoreManager<I extends Interaction, T extends InteractionCluster<I>> {
    public void calculateScoreAndSetIt(T interaction);
    public void calculateScoreAndSetIt(Iterator<T> iterator);
    public void calculateScoreAndSetIt(Collection<T> collection);
}
