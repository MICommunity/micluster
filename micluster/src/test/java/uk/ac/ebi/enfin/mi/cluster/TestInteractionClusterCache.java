package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.enfin.mi.cluster.cache.CacheStrategy;
import uk.ac.ebi.enfin.mi.cluster.utils.CompositeInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * InteractionCluster Tester.
 *
 * @author Rafael
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 *
 * @since 1.0
 */

public class TestInteractionClusterCache {

    static Logger logger = Logger.getLogger( TestInteractionClusterCache.class);

    @Test
    public void cluster_psicquic_inMemory(){

        ClusterContext.getInstance().setCacheStrategy( CacheStrategy.IN_MEMORY );

//        InteractionCluster iC = new InteractionCluster("P37173", "intact", 0, 100);
        InteractionCluster iC = new InteractionCluster("brca2", "intact", 0, 100);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        int interactionMappingId = iC.getInteractionMappingId();
        Assert.assertTrue(interactionMappingId > 0);
        Assert.assertTrue(interactionMapping.size() > 0);
        Assert.assertTrue(interactorMapping.size() > 0);
   }

    @Test
    public void cluster_psicquic_inMemory2(){

        ClusterContext.getInstance().setCacheStrategy( CacheStrategy.IN_MEMORY );

//        InteractionCluster iC = new InteractionCluster("P37173", "intact", 0, 100);
        InteractionCluster iC = new InteractionCluster("P06241", "mint", 0, 100);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        int interactionMappingId = iC.getInteractionMappingId();
        Assert.assertTrue(interactionMappingId > 0);
        Assert.assertTrue(interactionMapping.size() > 0);
        Assert.assertTrue(interactorMapping.size() > 0);
    }

    @Test
    public void cluster_inputstream_cached() throws Exception {

        // setup data
        File intact = new File( TestInteractionClusterCache.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
        File mint = new File( TestInteractionClusterCache.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );

        //TODO : when a trhead local clusterContext has been initialized, it is impossible to change it. we have to start a new process not like in this test
        // configure clustering strategy
        // ClusterContext.getInstance().setCacheStrategy( CacheStrategy.ON_DISK );

        for ( int i = 0; i < 25; i++ ) {

            List<InputStream> streams = new ArrayList<InputStream>( );
            streams.add( new FileInputStream( intact ) );
            streams.add( new FileInputStream( mint ) );
            final InputStream is = new CompositeInputStream( streams.iterator() );

            InteractionCluster iC = new InteractionCluster( is, true );

            iC.runService();
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            Assert.assertEquals( 39, interactionMappingId );
            Assert.assertEquals( 39, interactionMapping.size() );
            Assert.assertEquals( 37, interactorMapping.size() );

            ClusterContext.getInstance().getCacheManager().setResetCache( true );
        }
    }

    @Test
    public void cluster_inputstream_inMemory() throws Exception {

        // setup data
        File intact = new File( TestInteractionClusterCache.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
        File mint = new File( TestInteractionClusterCache.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );

        // configure clustering strategy
        ClusterContext.getInstance().setCacheStrategy( CacheStrategy.IN_MEMORY );
        ClusterContext.getInstance().getCacheManager().setResetCache( false );

        for ( int i = 0; i < 25; i++ ) {

            List<InputStream> streams = new ArrayList<InputStream>( );
            streams.add( new FileInputStream( intact ) );
            streams.add( new FileInputStream( mint ) );
            final InputStream is = new CompositeInputStream( streams.iterator() );

            InteractionCluster iC = new InteractionCluster( is, true );

            iC.runService();
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            Assert.assertEquals( 39, interactionMappingId );
            Assert.assertEquals( 39, interactionMapping.size() );
            Assert.assertEquals( 37, interactorMapping.size() );
        }
    }
}
