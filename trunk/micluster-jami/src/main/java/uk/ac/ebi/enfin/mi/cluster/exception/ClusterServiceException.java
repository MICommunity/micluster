package uk.ac.ebi.enfin.mi.cluster.exception;

/**
 * Exception for the Clustersing service.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.3
 */
public class ClusterServiceException extends Exception {
    public ClusterServiceException() {
        super();
    }

    public ClusterServiceException( String message ) {
        super( message );
    }

    public ClusterServiceException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ClusterServiceException( Throwable cause ) {
        super( cause );
    }
}
