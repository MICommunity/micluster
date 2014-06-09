package uk.ac.ebi.enfin.mi.cluster;
import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.enfin.mi.cluster.exception.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.model.EncoreInteraction;

import java.io.IOException;
import java.util.*;

/**
 * Test size of cluster interactions. Include a test for original file size and number of interactors expected.
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestClusterIO extends ExampleFiles{
    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(brca2_intact) == 106);
        Assert.assertTrue(interactionMapping.size() == 33);
        Assert.assertTrue(interactorMapping.size() == 33);
        Assert.assertTrue(interactorSynonyms.size() == 64);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InMint() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_mint, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(brca2_mint) == 33);
        Assert.assertTrue(interactionMapping.size() == 10);
        Assert.assertTrue(interactorMapping.size() == 11);
        Assert.assertTrue(interactorSynonyms.size() == 0);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InInnatedb() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_innatedb, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(brca2_innatedb) == 2);
        Assert.assertTrue(interactionMapping.size() == 2);
        Assert.assertTrue(interactorMapping.size() == 3);
        Assert.assertTrue(interactorSynonyms.size() == 9);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InBiogrid() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(brca2_biogrid, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(brca2_biogrid) == 110);
        Assert.assertTrue(interactionMapping.size() == 38);
        Assert.assertTrue(interactorMapping.size() == 36);
        Assert.assertTrue(interactorSynonyms.size() == 0);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test //todo: something to fix in micluster. P37173 should have these synonymous: NM_001024847, NM_003242, NP_003233 instead it just takes the first one: NM_001024847
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb00() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("uniprotkb,refseq");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P37173_innatedb) == 29);
        Assert.assertTrue(interactionMapping.size() == 19);
        Assert.assertTrue(interactorMapping.size() == 19);
        Assert.assertTrue(interactorSynonyms.size() == 18);
        /* Testing the IDs are refseq */
        Assert.assertTrue(interactorSynonyms.keySet().contains("NP_001003652"));
        Assert.assertTrue(interactorSynonyms.keySet().contains("NM_138473"));
        /* Testing the IDs are uniprot */
        Assert.assertTrue(interactorMapping.keySet().contains("P01137"));
        Assert.assertTrue(interactorMapping.keySet().contains("P23511"));
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }


    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb01() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("uniprotkb,ensembl");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P37173_innatedb) == 29);
        Assert.assertTrue(interactionMapping.size() == 19);
        Assert.assertTrue(interactorMapping.size() == 19);
        Assert.assertTrue(interactorSynonyms.size() == 18);
        /* Testing the IDs are Ensembl */
        Assert.assertTrue(interactorSynonyms.keySet().contains("ENSG00000001167"));
        Assert.assertTrue(interactorSynonyms.keySet().contains("ENSG00000120837"));
        /* Testing the IDs are Uniport */
        Assert.assertTrue(interactorMapping.keySet().contains("P01137"));
        Assert.assertTrue(interactorMapping.keySet().contains("P23511"));
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }


    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb02() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.setMappingIdDbNames("ensembl,refseq");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P37173_innatedb) == 29);
        Assert.assertTrue(interactionMapping.size() == 19);
        Assert.assertTrue(interactorMapping.size() == 19);
        Assert.assertTrue(interactorSynonyms.size() == 19);
        /* Testing the IDs are refseq */
        Assert.assertTrue(interactorSynonyms.keySet().contains("NP_001003652"));
        Assert.assertTrue(interactorSynonyms.keySet().contains("NM_138473"));
        /* Testing the IDs are Ensembl */
        Assert.assertTrue(interactorMapping.keySet().contains("ENSG00000001167"));
        Assert.assertTrue(interactorMapping.keySet().contains("ENSG00000120837"));
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P37173_intact) == 13);
        Assert.assertTrue(interactionMapping.size() == 10);
        Assert.assertTrue(interactorMapping.size() == 11);
        Assert.assertTrue(interactorSynonyms.size() == 22);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InMint() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P37173_mint) == 11);
        Assert.assertTrue(interactionMapping.size() == 5);
        Assert.assertTrue(interactorMapping.size() == 6);
        Assert.assertTrue(interactorSynonyms.size() == 0);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP07200InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P07200_intact, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P07200_intact) == 5);
        Assert.assertTrue(interactionMapping.size() == 3);
        Assert.assertTrue(interactorMapping.size() == 4);
        Assert.assertTrue(interactorSynonyms.size() == 8);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP07200InIrefindex() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P07200_irefindex, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(countLines(P07200_irefindex) == 4);
        Assert.assertTrue(interactionMapping.size() == 4);
        Assert.assertTrue(interactorMapping.size() == 5);
        Assert.assertTrue(interactorSynonyms.size() == 6);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
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

        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(interactionMapping.size() == 27);
        Assert.assertTrue(interactorMapping.size() == 27);
        Assert.assertTrue(interactorSynonyms.size() == 0);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
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

        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(interactionMapping.size() == 27);
        Assert.assertTrue(interactorMapping.size() == 27);
        Assert.assertTrue(interactorSynonyms.size() == 78);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
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

        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(interactionMapping.size() == 29);
        Assert.assertTrue(interactorMapping.size() == 29);
        Assert.assertTrue(interactorSynonyms.size() == 78);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
    }

    @Test
    public void testClusteredContentForP37173InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.setMappingIdDbNames("uniprotkb,intact,irefindex");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String,String> firstInteractor = interactionMapping.values().iterator().next().getInteractorAccsA();
        Map<String,String> secondInteractor = interactionMapping.values().iterator().next().getInteractorAccsB();
        /* First interactor */
        Assert.assertTrue(firstInteractor.values().contains("hxcAAra96c/MOyY41mR/8MT0fcI9606"));
        Assert.assertTrue(firstInteractor.values().contains("P37173"));
        Assert.assertTrue(firstInteractor.values().contains("EBI-296151"));
        Assert.assertTrue(firstInteractor.keySet().contains("uniprotkb"));
        Assert.assertTrue(firstInteractor.keySet().contains("intact"));
        Assert.assertTrue(firstInteractor.keySet().contains("irefindex"));
        /* Second interactor */
        Assert.assertTrue(secondInteractor.values().contains("GB2j4Snn26HiyL4umac7sD150T41392"));
        Assert.assertTrue(secondInteractor.values().contains("Q81LN0"));
        Assert.assertTrue(secondInteractor.values().contains("EBI-2820887"));
        Assert.assertTrue(secondInteractor.keySet().contains("uniprotkb"));
        Assert.assertTrue(secondInteractor.keySet().contains("intact"));
        Assert.assertTrue(secondInteractor.keySet().contains("irefindex"));
    }

}
