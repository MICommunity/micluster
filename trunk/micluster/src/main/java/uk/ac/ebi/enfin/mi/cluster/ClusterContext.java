package uk.ac.ebi.enfin.mi.cluster;

import uk.ac.ebi.enfin.mi.cluster.cache.CacheManager;
import uk.ac.ebi.enfin.mi.cluster.cache.CacheStrategy;
import uk.ac.ebi.enfin.mi.cluster.cache.EHCacheManager;
import uk.ac.ebi.enfin.mi.cluster.cache.InMemoryCacheManager;

/**
 * Cluster context gives access to configuration at the Thread level (<code>ThreadLocal</code>).
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.1
 */
public class ClusterContext {

    // TODO idea - set an optional contextId so that the same thread can have multiple context as long as they pass their id.
    // TODO, that sounds like we don't want to have this class as ThreadLocal but rather Singleton and handle a Map<String, ClusterContext>.


    private CacheManager<EncoreInteraction> cacheManager;
    private CacheStrategy cacheStrategy;

    private static ThreadLocal<ClusterContext> instance = new
            ThreadLocal<ClusterContext>() {
                @Override
                protected ClusterContext initialValue() {
                    return new ClusterContext();
                }
            };

    public static ClusterContext getInstance() {
        return instance.get();
    }

    //////////////////
    // Constructors

    private ClusterContext() {
        // initialize here default configuration
//        cacheStrategy = CacheStrategy.ON_DISK;
        cacheStrategy = CacheStrategy.IN_MEMORY;
    }

    ///////////////////////////
    // Getters and Setters


    public synchronized CacheStrategy getCacheStrategy() {
        return cacheStrategy;
    }

    public synchronized boolean isCacheStrategyAlreadySet() {
        return cacheStrategy != null;
    }

    public synchronized void setCacheStrategy( CacheStrategy cacheStrategy ) {
        if( cacheManager != null && ! this.cacheStrategy.equals( cacheStrategy ) ) {
            throw new IllegalArgumentException( "A cache manager is already instanciated with strategy '"+
                                                this.cacheStrategy +"', you cannot request '"+ cacheStrategy +"' " );

        }
        this.cacheStrategy = cacheStrategy;
    }

    public synchronized CacheManager getCacheManager() {
        if ( cacheManager == null ) {
            if ( cacheStrategy.equals( CacheStrategy.ON_DISK ) ) {
                cacheManager = new EHCacheManager();
            } else if ( cacheStrategy.equals( CacheStrategy.IN_MEMORY ) ) {
                cacheManager = new InMemoryCacheManager();
            } else {
                throw new IllegalArgumentException( "Strategy '" + cacheStrategy + "' is not supported." );
            }
        }
        return cacheManager;
    }
}
