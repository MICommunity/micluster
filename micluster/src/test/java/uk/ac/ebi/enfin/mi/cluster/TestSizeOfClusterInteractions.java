package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.*;

import static junit.framework.Assert.assertTrue;

/**
 * Test size of cluster interactions. Include a test for original file size and number of interactors expected.
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public class TestSizeOfClusterInteractions {
    private File brca2_mint;
    private File brca2_intact;
    private File brca2_innateDB;
    private File brca2_biogrid;
    private File P37173_mint;
    private File P37173_intact;
    private File P37173_innatedb;
    //todo:check with psicquic view clustering

    @Before
    public void setDataSourceExamples(){
        brca2_mint = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );
        brca2_intact = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
        brca2_innateDB = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );
        brca2_biogrid = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/brca2_biogrid.tsv" ).getFile() );
        P37173_innatedb = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/P37173_innatedb.tsv" ).getFile() );
        P37173_intact = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/P37173_intact.tsv" ).getFile() );
        P37173_mint = new File( TestSizeOfClusterInteractions.class.getResource( "/mitab_samples/P37173_mint.tsv" ).getFile() );
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InIntact() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        System.out.println(countLines(brca2_intact));
        assertTrue(interactionMapping.size() == 33);
        assertTrue(interactorMapping.size() == 33);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InMint() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_mint, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(brca2_mint) == 33);
        assertTrue(interactionMapping.size() == 10);
        assertTrue(interactorMapping.size() == 11);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InInnatedb() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_innateDB, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(brca2_innateDB) == 2);
        assertTrue(interactionMapping.size() == 2);
        assertTrue(interactorMapping.size() == 3);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InBiogrid() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_biogrid, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(brca2_biogrid) == 110);
        assertTrue(interactionMapping.size() == 38);
        assertTrue(interactorMapping.size() == 36);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InInnatedb() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(P37173_innatedb) == 29);
        assertTrue(interactionMapping.size() == 19);
        assertTrue(interactorMapping.size() == 19);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InIntact() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(P37173_intact) == 13);
        assertTrue(interactionMapping.size() == 10);
        assertTrue(interactorMapping.size() == 11);
    }

    @Test
    public void testSizeOfClusteredInteractionAndInteractorsForP37173InMint() throws ClusterServiceException, IOException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        assertTrue(countLines(P37173_mint) == 11);
        assertTrue(interactionMapping.size() == 5);
        assertTrue(interactorMapping.size() == 6);
    }


    public int countLines(File file) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        lnr.skip(Long.MAX_VALUE);
        return lnr.getLineNumber();
    }



}
