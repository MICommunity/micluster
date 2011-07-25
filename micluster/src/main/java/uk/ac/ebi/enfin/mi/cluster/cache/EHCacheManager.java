package uk.ac.ebi.enfin.mi.cluster.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shiftone.cache.adaptor.CacheMap;
import org.shiftone.cache.adaptor.EHCacheCache;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache manager allowing to build caches for the application.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.1
 */
public class EHCacheManager implements CacheManager {

    private static final Log log = LogFactory.getLog( EHCacheManager.class );

    private static final String INTERACTION_CACHE_NAME = "interactions.eternal-ondisk";
    private static final String INTERACTORS_CACHE_NAME = "interactors.eternal-ondisk";
    private static final String INTERACTOR_SYNONYMS_CACHE_NAME = "interactor-synonyms.eternal-ondisk";

    public static final String EHCACHE_CONFIG_FILE = "/ehcache-config.xml";

    private static final String DEFAULT_CACHE_LOCATION = System.getProperty( "java.io.tmpdir" ) + "/micluster";

    private net.sf.ehcache.CacheManager ehcacheManager;

    /**
     * Location on disk where your caches will be stored.
     */
    private File cacheStorage;

    /**
     * If set to true, caches are flushed empty when next requested from the CacheManager.
     * This is useful if you want to keep your cache across runs of the api.
     * By default this is true so don't forget to set it to false if you are intending to keep your cache for longer.
     */
    private boolean resetCache;

    /**
     * cache name -> status of the cache (true means the cache was reset already).
     */
    private Map<String, Boolean> cache2resetStatus;

    private boolean isCacheSynchronized;

    //////////////////
    // Constructors

    public EHCacheManager() {
        this.resetCache = true;
        isCacheSynchronized = false;
        this.cacheStorage = new File( DEFAULT_CACHE_LOCATION );
        cache2resetStatus = new HashMap<String, Boolean>();
    }

    ///////////////////////////
    // Getters and Setters

    public File getCacheStorage() {
        return cacheStorage;
    }

    public void setCacheStorage( File cacheStorage ) {
        if( ehcacheManager != null ) {
            throw new IllegalStateException( "You can only set the storage location of your caches before building them" );
        }
        this.cacheStorage = cacheStorage;
    }

    public synchronized boolean isResetCache() {
        return resetCache;
    }

    public synchronized void setResetCache( boolean resetCache ) {
        this.resetCache = resetCache;

        // if user request cache reset upon next access, we flush the list of cache reset status
        if( resetCache ) {
            cache2resetStatus.clear();
        }
    }

    public synchronized boolean isCacheSynchronized() {
        return isCacheSynchronized;
    }

    public synchronized void setCacheSynchronized( boolean cacheSynchronized ) {
        isCacheSynchronized = cacheSynchronized;
    }

    public synchronized Map<Integer, EncoreInteraction> getInteractionCache() {
        return (Map<Integer, EncoreInteraction>) buildCache( INTERACTION_CACHE_NAME );
    }

    public synchronized Map<String, List<Integer>> getInteractorCache() {
        return (Map<String, List<Integer>>) buildCache( INTERACTORS_CACHE_NAME );
    }

    public Map<String, String> getSynonymCache() {
        return (Map<String, String>) buildCache( INTERACTOR_SYNONYMS_CACHE_NAME );
    }

    private synchronized Map buildCache( final String cacheName ) {
        
        // TODO this is the place where we will handle the programatic creation of the caches instead of relying on a config file.

        if( ehcacheManager == null ) {

            // http://ehcache.org/documentation/shutdown.html
            log.info( "Setting System's property: net.sf.ehcache.enableShutdownHook=true" );
            System.setProperty( "net.sf.ehcache.enableShutdownHook", "true" );

            URL url = getClass().getResource( EHCACHE_CONFIG_FILE );
            if ( url == null ) {
                throw new IllegalArgumentException( "You must give a non null ehcache configuration url:" + EHCACHE_CONFIG_FILE );
            }
            final Configuration configuration = ConfigurationFactory.parseConfiguration( url );
            configuration.getDiskStoreConfiguration().setPath( cacheStorage.getAbsolutePath() );
            ehcacheManager = new net.sf.ehcache.CacheManager( configuration );



            // to make sure that the cache is released whenever the JVM stops
//            System.out.println( "--- REGISTERING SHUTDOWN HOOK ---" );
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                public void run() {
//                    log.info( "Executing JVM shutdown hook now..." );
//                    shutdown();
//                }
//            });
        }
        
        final net.sf.ehcache.Cache cache = ehcacheManager.getCache( cacheName );

        if( cache == null ) {
            throw new IllegalStateException( "Could not find cache by name: '"+ cacheName +"'" );
        }

        // Using the ShiftOne Cache adapters we convert ehcache into a shiftone cache into a Map
        Map map = new CacheMap( new EHCacheCache( cache ) );

        if( isCacheSynchronized ) {
            // This keeps the original backing Map (i.e. the cache) and synchronizes access to sensitive methods.
            // In this instance, the more performant ConcurrentHashMap cannot be used as it copies the content in memory :(
            map = Collections.synchronizedMap( map );
        }

        if( resetCache ) {


            if( ! cache2resetStatus.containsKey( cacheName )) {
                cache2resetStatus.put( cacheName, Boolean.FALSE );
            }

            boolean hasReset = cache2resetStatus.get( cacheName );

            if( ! hasReset ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Clearing up cache '"+ cacheName +"' that contained " + map.size() + " elements.");
                }
                map.clear();
                
                cache2resetStatus.put( cacheName, Boolean.TRUE );
            }
        }

        if(log.isDebugEnabled()) log.debug( "Map('"+ cacheName +"') contains " + map.size() + " entr" + (map.size() == 1 ? "y" : "ies") );

        return map;
    }

    /**
     * See also http://ehcache.org/documentation/shutdown.html for shutting it down cleanly. 
     */
    public void shutdown() {
        if(ehcacheManager != null && Status.STATUS_ALIVE.equals( ehcacheManager.getStatus() ) ) {

            // lookup caches and attempt to shut them down if active
            shutdownIfActive( ehcacheManager.getCache( INTERACTION_CACHE_NAME ) );
            shutdownIfActive( ehcacheManager.getCache( INTERACTORS_CACHE_NAME ) );
            shutdownIfActive( ehcacheManager.getCache( INTERACTOR_SYNONYMS_CACHE_NAME ) );

            log.info( "Shutting down EHCache EHCacheManager..." );
            ehcacheManager.shutdown();  
        }
    }

    private void shutdownIfActive( Cache cache ) {
         if( Status.STATUS_ALIVE.equals( cache.getStatus() ) ) {
             System.out.println( "Attempting to dispose of: " + cache.getName() + "..." );
             cache.dispose();
         } else {
             System.out.println( "Cache('"+ cache.getName() +"') was not alive. Skipping dispose()..." );
         }
    }
}
