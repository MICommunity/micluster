//package uk.ac.ebi.enfin.mi.cluster.score;
//
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.Test;
//import psidev.psi.mi.tab.PsimiTabReader;
//import psidev.psi.mi.tab.model.BinaryInteraction;
//import psidev.psi.mi.xml.converter.ConverterException;
//import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
//import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
//import uk.ac.ebi.enfin.mi.cluster.EncoreInteractionForScoring;
//import static junit.framework.Assert.*;
//import static junit.framework.Assert.assertTrue;
//
//import java.io.*;
//import java.net.URL;
//import java.util.*;
//
///**
// * User: rafael
// * Date: 07-Jun-2010
// * Time: 11:06:07
// */
//public class TestInteractionClusterScore {
//    private File brca2_mint;
//    private File brca2_intact;
//    private File brca2_innateDB;
//    private File brca2_biogrid;
//    private File P37173_mint;
//    private File P37173_intact;
//    private File P37173_innatedb;
//
//    @Before
//    public void setDataSourceExamples(){
//        brca2_mint = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );
//        brca2_intact = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
//        brca2_innateDB = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );
//        brca2_biogrid = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/brca2_biogrid.tsv" ).getFile() );
//        P37173_innatedb = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/P37173_innatedb.tsv" ).getFile() );
//        P37173_intact = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/P37173_intact.tsv" ).getFile() );
//        P37173_mint = new File( TestInteractionClusterScore.class.getResource( "/mitab_samples/P37173_mint.tsv" ).getFile() );
//    }
//    //todo: test scores
//    //todo: for documentation check all the available acc db names. So far: uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex,hgnc,ensembl  (entrez gene/locuslink)?
//    //todo: otherInteractorAcc (maybe it should eb call UniProt. It seems just to accept uniprot) and synonymous might have some redundant or excluding information. check this!
//
//
//
//
//
//    @Test  //todo: review this test. it seems synonyms is not working
//    public void testSynonymous() throws ClusterServiceException {
//        InteractionClusterScore iC = new InteractionClusterScore();
//        iC.setBinaryInteractionIterator(brca2_intact, false);
//        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//        iC.runService();
//        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
////        assertTrue(interactorSynonyms.size() > 0);
//    }
//
//
//    @Test
//    public void testPrimaryId() throws ClusterServiceException {
//        InteractionClusterScore iC = new InteractionClusterScore();
//        iC.setBinaryInteractionIterator(brca2_intact, false);
//        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//        iC.runService();
//        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//        assertTrue(interactionMapping.values().iterator().next().getInteractorAccsA().keySet().iterator().next().equalsIgnoreCase("uniprotkb") );
//        assertTrue(interactionMapping.values().iterator().next().getInteractorAccsB().keySet().iterator().next().equalsIgnoreCase("uniprotkb") );
//    }
//
//    @Test //todo: check. No other Ids
//    public void testOtherIds() throws ClusterServiceException {
//        InteractionClusterScore iC = new InteractionClusterScore();
//        iC.setBinaryInteractionIterator(brca2_intact, false);
//        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//        iC.runService();
//        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
////        assertTrue(interactionMapping.values().iterator().next().getOtherInteractorAccsA().keySet().contains("irefindex") );
////        assertTrue(interactionMapping.values().iterator().next().getOtherInteractorAccsA().keySet().contains("irefindex") );
//    }
//
//
//
//    @Test
//    public void testTwoInputs() throws ClusterServiceException {
//        InteractionClusterScore iC = new InteractionClusterScore();
//        iC.setMappingIdDbNames("uniprotkb,chebi");
//        /* one source */
//        iC.setBinaryInteractionIterator(brca2_intact, false);
//        iC.runService();
//        /* second source */
//        iC.setBinaryInteractionIterator(brca2_mint, false);
//        iC.runService();
//
//        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
//        assertTrue(interactionMapping.size() == 37);
//        assertTrue(interactorMapping.size() == 36);
//    }
//
//
//    public void testInteractionClusterScoreWithCustomScores(){
//        InteractionClusterScore ics = new InteractionClusterScore();
//        ics.addQueryAcc("P37173");
//        ics.addQuerySource("IntAct");
//        /* Custom weights to calculate the miScore weight */
//        ics.setTypeWeight(1.0f);
//        ics.setMethodWeight(0.5f);
//        ics.setPublicationWeight(0.6f);
//
//        /* Custom scores for ontology type terms to calculate the type score*/
//        Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
//        customOntologyTypeScores.put("MI:0208", 0.05f); // cv1 // genetic interaction
//        customOntologyTypeScores.put("MI:0403", 0.20f); // cv2 // colocalization
//        customOntologyTypeScores.put("MI:0914", 0.20f); // cv3 // association
//        customOntologyTypeScores.put("MI:0915", 0.40f); // cv4 // physical association
//        customOntologyTypeScores.put("MI:0407", 1.00f); // cv5 // direct interaction
//        customOntologyTypeScores.put("unknown", 0.02f); // cv6 // unknown
//        ics.setCustomOntologyTypeScores(customOntologyTypeScores);
//
//        /* Custom scores for ontology method terms to calculate the method score */
//        Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
//        customOntologyMethodScores.put("MI:0013", 1.00f); // cv1 // biophysical
//        customOntologyMethodScores.put("MI:0090", 0.66f); // cv2 // protein complementation assay
//        customOntologyMethodScores.put("MI:0254", 0.10f); // cv3 // genetic interference
//        customOntologyMethodScores.put("MI:0255", 0.10f); // cv4 // post transcriptional interference
//        customOntologyMethodScores.put("MI:0401", 1.00f); // cv5 // biochemical
//        customOntologyMethodScores.put("MI:0428", 0.33f); // cv6 // imagining technique
//        customOntologyMethodScores.put("unknown", 0.05f); // cv7 // unknown
//        ics.setCustomOntologyMethodScores(customOntologyMethodScores);
//
//        /* Set a number of publications to set the highest score for the publication score */
//        ics.setCustomPublicationNumberWithHighestScore(10);
//
//        /* Run service */
//        ics.runService();
//        /* Retrive results */
//        Map<Integer, EncoreInteractionForScoring> interactionMapping = ics.getInteractionMapping();
//        Map<String, List<Integer>> interactorMapping = ics.getInteractorMapping();
//        Map<String, String> synonymMapping = ics.getSynonymMapping();
//        int interactionMappingId = ics.getInteractionMappingId();
//        assertTrue(interactionMappingId > 0);
//        assertTrue(interactionMapping.size() > 0);
//        assertTrue(interactorMapping.size() > 0);
//    }
//
//    public void testInteractionClusterScoreWithBinaryInteractions(){
//        try {
//            /* Get binaryInteractions from PSI-MI files */
//            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
//            URL irefindexQuery = new URL("http://biotin.uio.no:8080/psicquic-ws/webservices/current/search/query/P07200");
//            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
//            PsimiTabReader mitabReader = new PsimiTabReader(false);
//            binaryInteractions.addAll(mitabReader.read(intactQuery));
//            binaryInteractions.addAll(mitabReader.read(irefindexQuery));
//
//            /* Run cluster using list of binary interactions as input */
//            InteractionClusterScore iC = new InteractionClusterScore();
//            iC.setBinaryInteractionIterator(binaryInteractions.iterator());
//            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//            iC.runService();
//
//            /* Retrieve results */
//            Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
//            Map<String, String> synonymMapping = iC.getSynonymMapping();
//            int interactionMappingId = iC.getInteractionMappingId();
//
//            /* Test */
//            assertTrue(interactionMappingId > 0);
//            assertTrue(interactionMapping.size() > 0);
//            assertTrue(interactorMapping.size() > 0);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (ConverterException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//    public void testInteractionClusterScoreWithBinaryInteractions_fromInputStream(){
//        try {
//            /* Get binaryInteractions from PSI-MI files */
//            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
//            PsimiTabReader mitabReader = new PsimiTabReader(false);
//
//            Iterator<BinaryInteraction> binaryInteractionsIterator = mitabReader.iterate(intactQuery.openStream());
//
//            /* Run cluster using list of binary interactions as input */
//            InteractionClusterScore iC = new InteractionClusterScore();
//            iC.setBinaryInteractionIterator(binaryInteractionsIterator);
//            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//            iC.runService();
//
//            /* Retrieve results */
//            Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
//            Map<String, String> synonymMapping = iC.getSynonymMapping();
//            int interactionMappingId = iC.getInteractionMappingId();
//
//            /* Test */
//            assertTrue(interactionMappingId > 0);
//            assertTrue(interactionMapping.size() > 0);
//            assertTrue(interactorMapping.size() > 0);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (ConverterException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//    public void testInteractionClusterScoreWithBinaryInteractionsAndPsicquic(){
//        PsimiTabReader mitabReader = new PsimiTabReader(false);
//        try {
//            /* Get binaryInteractions from PSI-MI files */
//            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P37173");
//            URL irefindexQuery = new URL("http://biotin.uio.no:8080/psicquic-ws/webservices/current/search/query/P37173");
//
//            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
//            binaryInteractions.addAll(mitabReader.read(intactQuery));
//            binaryInteractions.addAll(mitabReader.read(irefindexQuery));
//
//            /* Run cluster using list of binary interactions as input */
//            InteractionClusterScore iC = new InteractionClusterScore();
//            /* Add binary interactions */
//            iC.setBinaryInteractionIterator(binaryInteractions.iterator());
//            /* Add interactions from Psicquic */
//            iC.addQueryAcc("P37173");
//            iC.addQuerySource("chembl");
//            /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
//            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//            /* Run clustering service */
//            iC.runService();
//
//            /* Retrieve results */
//            Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
//            Map<String, String> synonymMapping = iC.getSynonymMapping();
//            int interactionMappingId = iC.getInteractionMappingId();
//
//            /* Test */
//            assertTrue(interactionMappingId > 0);
//            assertTrue(interactionMapping.size() > 0);
//            assertTrue(interactorMapping.size() > 0);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (ConverterException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//
//    public void testEncore2Binary(){
//        PsimiTabReader mitabReader = new PsimiTabReader(false);
//        /* Run cluster using list of binary interactions as input */
//        InteractionClusterScore iC = new InteractionClusterScore();
//        /* Add interactions from Psicquic */
//        iC.addQueryAcc("P37173");
////        iC.addQuerySource("chembl");
//        iC.addQuerySource("intact");
//        /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
//        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
//        /* Run clustering service */
//        iC.runService();
//
//        /* Retrieve results */
//        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
//
//        /* Get PSI binary Interactions */
//        Map<Integer, BinaryInteraction> binaryInteractionMapping = new HashMap<Integer,BinaryInteraction>();
//        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
//        for(int mappingId:interactionMapping.keySet()){
//            EncoreInteractionForScoring eI = interactionMapping.get(mappingId);
//            BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);
//            binaryInteractionMapping.put(mappingId,bI);
//        }
//
//        /* Test */
//        assertTrue(binaryInteractionMapping.size() > 0);
//    }
//
////    public void testMatrixDB(){
////        InteractionClusterScore ics = new InteractionClusterScore();
////        ics.setOptionalQueryParameters(0, 400);
////        ics.addQueryAcc("*");
////        ics.addQuerySource("MatrixDB");
////        ics.runService();
////        HashMap<Integer, EncoreInteraction> interactionMapping = ics.getInteractionMapping();
////        HashMap<String, ArrayList<Integer>> interactorMapping = ics.getInteractorMapping();
////        int interactionMappingId = ics.getInteractionMappingId();
////        assertTrue(interactionMappingId > 0);
////        assertTrue(interactionMapping.size() > 0);
////        assertTrue(interactorMapping.size() > 0);
////    }
//
//
//public int countLines(File file) throws IOException {
//    LineNumberReader  lnr = new LineNumberReader(new FileReader(file));
//    lnr.skip(Long.MAX_VALUE);
//    return lnr.getLineNumber();
//}
//
//
//
//}
