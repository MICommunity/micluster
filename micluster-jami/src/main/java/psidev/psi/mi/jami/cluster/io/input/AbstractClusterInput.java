package psidev.psi.mi.jami.cluster.io.input;

/**
 * Created by maitesin on 12/06/2014.
 */
abstract class AbstractClusterInput implements ClusterInput {
    @Override
    public void remove() {
        throw new IllegalStateException("Never should call that method");
    }
}
