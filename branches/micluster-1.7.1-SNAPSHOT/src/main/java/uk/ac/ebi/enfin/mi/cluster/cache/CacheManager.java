package uk.ac.ebi.enfin.mi.cluster.cache;

import uk.ac.ebi.enfin.mi.cluster.EncoreBinaryInteraction;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * TODO document this !
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public interface CacheManager<T extends EncoreBinaryInteraction> {
    
    File getCacheStorage();

    void setCacheStorage( File cacheStorage );

    boolean isResetCache();

    void setResetCache( boolean resetCache );

    boolean isCacheSynchronized();

    void setCacheSynchronized( boolean cacheSynchronized );

    Map<Integer, T> getInteractionCache();

    Map<String, List<Integer>> getInteractorCache();

    Map<String, String> getSynonymCache();

    void shutdown();
}
