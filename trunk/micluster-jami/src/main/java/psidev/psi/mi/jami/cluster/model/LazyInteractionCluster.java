package psidev.psi.mi.jami.cluster.model;

import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;

/**
 * Created by maitesin on 02/09/2014.
 */
public class LazyInteractionCluster<T extends Interaction> extends AbstractInteractionCluster<T> {
    public LazyInteractionCluster(Long Id) {
        super(Id);
    }

    @Override
    public Long getId() {
        return this.Id;
    }

    @Override
    public Collection<T> getInteractions() {
        return loadAllInteractions();
    }

    @Override
    public boolean addInteraction(T interaction){
        //TODO
        return false;
    }

    @Override
    public void addInteractions(Collection<T> interactions){
        //TODO
    }

    private Collection<T> loadAllInteractions() {
        //TODO
        return null;
    }
}
