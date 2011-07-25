package uk.ac.ebi.enfin.mi.cluster.cache;

/**
 * Cache strategy supported by the CacheManager.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.1
 */
public enum CacheStrategy {

    /**
     * All clustered data is kept in memory.
     */
    IN_MEMORY,

    /**
     * All clustered data is kept in a cache on disk and can survive multiple JVM run.
     */
    ON_DISK;
}
