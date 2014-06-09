package uk.ac.ebi.enfin.mi.cluster.cache;

import uk.ac.ebi.enfin.mi.cluster.model.EncoreInteraction;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory strategy.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.1
 */
public class InMemoryCacheManager implements CacheManager {

    private Map<Integer, EncoreInteraction> interactionMapping;
    private Map<String, List<Integer>> interactorMapping;

    private boolean isCacheSynchronized = false;

    public File getCacheStorage() {
        return null;
    }

    public void setCacheStorage( File cacheStorage ) {
    }

    public boolean isResetCache() {
        return false;
    }

    public void setResetCache( boolean resetCache ) {
    }

    public boolean isCacheSynchronized() {
        return isCacheSynchronized;
    }

    public void setCacheSynchronized( boolean cacheSynchronized ) {
        this.isCacheSynchronized = cacheSynchronized;
    }

    public Map<Integer, EncoreInteraction> getInteractionCache() {
        Map<Integer, EncoreInteraction> map = null;
        if( isCacheSynchronized() ) {
            map = new ConcurrentHashMap<Integer, EncoreInteraction>();
        } else {
            map = new HashMap<Integer, EncoreInteraction>();
        }
        return map;
    }

    public Map<String, List<Integer>> getInteractorCache() {
        Map<String, List<Integer>> map = null;
        if( isCacheSynchronized() ) {
            map = new ConcurrentHashMap<String, List<Integer>>();
        } else {
            map = new HashMap<String, List<Integer>>();
        }
        return map;
    }

    public Map<String, String> getSynonymCache() {
        Map<String, String> map = null;
        if( isCacheSynchronized() ) {
            map = new ConcurrentHashMap<String, String>();
        } else {
            map = new HashMap<String, String>();
        }
        return map;
    }

    public void shutdown() {
    }
}
