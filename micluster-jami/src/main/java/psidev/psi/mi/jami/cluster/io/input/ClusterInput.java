package psidev.psi.mi.jami.cluster.io.input;

import psidev.psi.mi.jami.cluster.exception.ClusterException;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Iterator;

/**
 * Created by maitesin on 12/06/2014.
 */
public interface ClusterInput extends Iterator<Interaction> {
    @Override
    public Interaction next();

    @Override
    public boolean hasNext();

    @Override
    public void remove();
}
