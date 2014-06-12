package psidev.psi.mi.jami.cluster.exception;

/**
 * Created by maitesin on 12/06/2014.
 */
public class ClusterException extends Exception {
    public ClusterException() {
    }

    public ClusterException(String message) {
        super(message);
    }

    public ClusterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterException(Throwable cause) {
        super(cause);
    }
}
