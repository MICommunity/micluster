package uk.ac.ebi.enfin.mi.cluster;
import org.junit.Test;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;

import static junit.framework.Assert.assertTrue;

/**
 * Test size of cluster interactions. Include a test for original file size and number of interactors expected.
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestClusterExpectedIO extends ExampleFiles{
    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(brca2_intact) == 106);
        assertTrue(interactionMapping.size() == 33);
        assertTrue(interactorMapping.size() == 33);
        assertTrue(interactorSynonyms.size() == 64);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InMint() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_mint, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(brca2_mint) == 33);
        assertTrue(interactionMapping.size() == 10);
        assertTrue(interactorMapping.size() == 11);
        assertTrue(interactorSynonyms.size() == 0);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InInnatedb() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_innatedb, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(brca2_innatedb) == 2);
        assertTrue(interactionMapping.size() == 2);
        assertTrue(interactorMapping.size() == 3);
        assertTrue(interactorSynonyms.size() == 9);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InBiogrid() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_biogrid, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(brca2_biogrid) == 110);
        assertTrue(interactionMapping.size() == 38);
        assertTrue(interactorMapping.size() == 36);
        assertTrue(interactorSynonyms.size() == 0);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test //todo: something to fix in micluster. P37173 should have these synonymous: NM_001024847, NM_003242, NP_003233 instead it just takes the first one: NM_001024847
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb00() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("uniprotkb,refseq");
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P37173_innatedb) == 29);
        assertTrue(interactionMapping.size() == 19);
        assertTrue(interactorMapping.size() == 19);
        assertTrue(interactorSynonyms.size() == 18);
        /* Testing the IDs are refseq */
        assertTrue(interactorSynonyms.keySet().contains("NP_001003652"));
        assertTrue(interactorSynonyms.keySet().contains("NM_138473"));
        /* Testing the IDs are uniprot */
        assertTrue(interactorMapping.keySet().contains("P01137"));
        assertTrue(interactorMapping.keySet().contains("P23511"));
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }


    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb01() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("uniprotkb,ensembl");
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P37173_innatedb) == 29);
        assertTrue(interactionMapping.size() == 19);
        assertTrue(interactorMapping.size() == 19);
        assertTrue(interactorSynonyms.size() == 18);
        /* Testing the IDs are Ensembl */
        assertTrue(interactorSynonyms.keySet().contains("ENSG00000001167"));
        assertTrue(interactorSynonyms.keySet().contains("ENSG00000120837"));
        /* Testing the IDs are Uniport */
        assertTrue(interactorMapping.keySet().contains("P01137"));
        assertTrue(interactorMapping.keySet().contains("P23511"));
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }


    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb02() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("ensembl,refseq");
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P37173_innatedb) == 29);
        assertTrue(interactionMapping.size() == 19);
        assertTrue(interactorMapping.size() == 19);
        assertTrue(interactorSynonyms.size() == 19);
        /* Testing the IDs are refseq */
        assertTrue(interactorSynonyms.keySet().contains("NP_001003652"));
        assertTrue(interactorSynonyms.keySet().contains("NM_138473"));
        /* Testing the IDs are Ensembl */
        assertTrue(interactorMapping.keySet().contains("ENSG00000001167"));
        assertTrue(interactorMapping.keySet().contains("ENSG00000120837"));
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P37173_intact) == 13);
        assertTrue(interactionMapping.size() == 10);
        assertTrue(interactorMapping.size() == 11);
        assertTrue(interactorSynonyms.size() == 22);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InMint() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P37173_mint) == 11);
        assertTrue(interactionMapping.size() == 5);
        assertTrue(interactorMapping.size() == 6);
        assertTrue(interactorSynonyms.size() == 0);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP07200InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P07200_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P07200_intact) == 5);
        assertTrue(interactionMapping.size() == 3);
        assertTrue(interactorMapping.size() == 4);
        assertTrue(interactorSynonyms.size() == 8);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP07200InIrefindex() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P07200_irefindex, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(countLines(P07200_irefindex) == 4);
        assertTrue(interactionMapping.size() == 4);
        assertTrue(interactorMapping.size() == 5);
        assertTrue(interactorSynonyms.size() == 6);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testClusterForP37173InIntactAndInnatedb00() throws ClusterServiceException {
        InteractionCluster iC = new InteractionCluster();
        iC.setMappingIdDbNames("uniprotkb");
        /* first source */
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        /* second source */
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();

        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(interactionMapping.size() == 27);
        assertTrue(interactorMapping.size() == 27);
        assertTrue(interactorSynonyms.size() == 0);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testClusterForP37173InIntactAndInnatedb01() throws ClusterServiceException {
        InteractionCluster iC = new InteractionCluster();
        iC.setMappingIdDbNames(allMappingNames);
        /* first source */
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        /* second source */
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();

        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(interactionMapping.size() == 27);
        assertTrue(interactorMapping.size() == 27);
        assertTrue(interactorSynonyms.size() == 78);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testClusterForP37173InIntactAndInnatedbAndMint() throws ClusterServiceException {
        InteractionCluster iC = new InteractionCluster();
        iC.setMappingIdDbNames(allMappingNames);
        /* first source */
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        /* second source */
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();
        /* third source */
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.runService();

        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        assertTrue(interactionMapping.size() == 29);
        assertTrue(interactorMapping.size() == 29);
        assertTrue(interactorSynonyms.size() == 78);
        assertTrue(iC.interactionMappingId == interactionMapping.size());
    }

    @Test
    public void testClusteredContentForP37173InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.setMappingIdDbNames("uniprotkb,intact,irefindex");
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String,String> firstInteractor = interactionMapping.values().iterator().next().getInteractorAccsA();
        Map<String,String> secondInteractor = interactionMapping.values().iterator().next().getInteractorAccsB();
        /* First interactor */
        assertTrue(firstInteractor.values().contains("hxcAAra96c/MOyY41mR/8MT0fcI9606"));
        assertTrue(firstInteractor.values().contains("P37173"));
        assertTrue(firstInteractor.values().contains("EBI-296151"));
        assertTrue(firstInteractor.keySet().contains("uniprotkb"));
        assertTrue(firstInteractor.keySet().contains("intact"));
        assertTrue(firstInteractor.keySet().contains("irefindex"));
        /* Second interactor */
        assertTrue(secondInteractor.values().contains("GB2j4Snn26HiyL4umac7sD150T41392"));
        assertTrue(secondInteractor.values().contains("Q81LN0"));
        assertTrue(secondInteractor.values().contains("EBI-2820887"));
        assertTrue(secondInteractor.keySet().contains("uniprotkb"));
        assertTrue(secondInteractor.keySet().contains("intact"));
        assertTrue(secondInteractor.keySet().contains("irefindex"));
    }


    @Test
    public void testClusteredContentForP37173InIntactAndInnatedbAndMint() throws ClusterServiceException {
        InteractionCluster iC = new InteractionCluster();
        iC.setMappingIdDbNames(allMappingNames);
        /* first source */
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        /* second source */
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();
        /* third source */
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.runService();

        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        //todo: loop until you find an interesting interaction
        // interactionMapping.values().iterator().next()
        // a aevaluate things like ...
//
//                interactionMapping.values().iterator().next().getDistinctPublications() =1
//interactionMapping.values().iterator().next().getPublicationIds() =2
    }

}
