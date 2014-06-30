package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Assert;
import org.junit.Test;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;
import uk.ac.ebi.enfin.mi.cluster.utils.CompositeInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

/**
 * Test conversion between encore and binaryInteraction are OK
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestEncoreBinaryInteractionsConversion extends ExampleFiles{
    @Test
    public void testEncore2Binary() throws ClusterServiceException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();

        /* Get PSI binary Interactions */
        Map<Integer, BinaryInteraction> binaryInteractionMapping = new HashMap<Integer,BinaryInteraction>();
        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
        for(int mappingId:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);
            binaryInteractionMapping.put(mappingId,bI);
        }

        /* Test binaryInteraction and encoreInteraction have the same size */
        Assert.assertTrue(binaryInteractionMapping.size() == interactionMapping.size());
    }




    @Test
    public void clusterIntramolecularInteractionsTest(){
        File intramolecular = new File( TestEncoreBinaryInteractionsConversion.class.getResource( "/mitab_samples/intramolecular_test.tsv" ).getFile() );


        try {
            List<InputStream> streams = new ArrayList<InputStream>( );
            streams.add( new FileInputStream( intramolecular ) );
//            streams.add( new FileInputStream( mint ) );
            final InputStream is = new CompositeInputStream( streams.iterator() );
            InteractionClusterScore iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(is, true);
            iC.setMappingIdDbNames("uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex");
            iC.runService();
            /* Convert EncoreInteractions into BinaryInteractions */
            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
            for(EncoreInteraction eI:iC.getInteractionMapping().values()){
                binaryInteractions.add(iConverter.getBinaryInteractionForScoring(eI));
            }
            assertTrue(binaryInteractions.size() == 3);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClusterServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }




    @Test
    public void clusterIntramolecularInteractionsIntact(){
        File intramolecular = new File( TestEncoreBinaryInteractionsConversion.class.getResource( "/mitab_samples/intramolecular_intact.tsv" ).getFile() );


        try {
            List<InputStream> streams = new ArrayList<InputStream>( );
            streams.add( new FileInputStream( intramolecular ) );
//            streams.add( new FileInputStream( mint ) );
            final InputStream is = new CompositeInputStream( streams.iterator() );
            InteractionClusterScore iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(is, true);
            iC.setMappingIdDbNames("uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex");
            iC.runService();
            /* Convert EncoreInteractions into BinaryInteractions */
            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
            for(EncoreInteraction eI:iC.getInteractionMapping().values()){
                binaryInteractions.add(iConverter.getBinaryInteractionForScoring(eI));
            }
            assertTrue(binaryInteractions.size() == 80);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClusterServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
