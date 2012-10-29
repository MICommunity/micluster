import junit.framework.TestCase;
import org.apache.log4j.Logger;
import psidev.psi.mi.tab.PsimiTabException;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 11:06:07
 */
public class TestInteractionClusterScore extends TestCase {
    private static String bout = "Bad output for";
    static Logger logger = Logger.getLogger(TestInteractionClusterScore.class);
    public void testInteractionClusterScore(){
        InteractionClusterScore ics = new InteractionClusterScore();
        ics.addQueryAcc("P04637");
        ics.addQueryAcc("Q06609");
        ics.addQuerySource("IntAct");
        ics.addQuerySource("mint");
        ics.runService();
        Map<Integer, EncoreInteraction> interactionMapping = ics.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = ics.getInteractorMapping();
        int interactionMappingId = ics.getInteractionMappingId();
        //assertTrue(interactionMappingId > 0);
        //assertTrue(interactionMapping.size() > 0);
        //assertTrue(interactorMapping.size() > 0);
    }

    public void testRefeedingInteractionClusterScore(){
        /* First cluster */
        InteractionClusterScore ics = new InteractionClusterScore();
        ics.addQueryAcc("Q06609");
        ics.addQuerySource("IntAct");
        ics.runService();

        /* New cluster merging the information from the first cluster and the results from a new query */
        InteractionClusterScore ics2 = new InteractionClusterScore();
        ics2.setInteractionMapping(ics.getInteractionMapping());
        ics2.setInteractorMapping(ics.getInteractorMapping());
        ics2.setSynonymMapping(ics.getSynonymMapping());
        ics2.setInteractionMappingId(ics.getInteractionMappingId());
        ics2.addQueryAcc("Q06609");
        ics2.addQuerySource("mint");
        ics2.runService();

        Map<Integer, EncoreInteraction> interactionMapping = ics2.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = ics2.getInteractorMapping();
        Map<String, String> synonymMapping = ics2.getSynonymMapping();
        int interactionMappingId = ics2.getInteractionMappingId();

        //assertTrue(interactionMappingId > 0);
        //assertTrue(interactionMapping.size() > 0);
        //assertTrue(interactorMapping.size() > 0);
    }

    public void testRefeedingInteractionClusterScore2(){
        InteractionClusterScore iC = new InteractionClusterScore();

        /* Acc */
        List<String> acc = new ArrayList<String>();
        acc.add("Q06609");

        /* db 1 */
        List<String> db1 = new ArrayList<String>();
        db1.add("IntAct");

        /* db 2 */
        List<String> db2 = new ArrayList<String>();
        db2.add("mint");

        /* First query */
        iC.setQueryAccs(acc);
        iC.setQuerySources(db1);
        iC.runService();

        /* Second query */
        iC.setQueryAccs(acc);
        iC.setQuerySources(db2);
        iC.runService();

        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> synonymMapping = iC.getSynonymMapping();
        int interactionMappingId = iC.getInteractionMappingId();

        //assertTrue(interactionMappingId > 0);
        //assertTrue(interactionMapping.size() > 0);
       // assertTrue(interactorMapping.size() > 0);
    }


    public void testInteractionClusterScoreWithCustomScores(){
        InteractionClusterScore ics = new InteractionClusterScore();
        ics.addQueryAcc("P37173");
        ics.addQuerySource("IntAct");
        /* Custom weights to calculate the miScore weight */
        ics.setTypeWeight(1.0f);
        ics.setMethodWeight(0.5f);
        ics.setPublicationWeight(0.6f);

        /* Custom scores for ontology type terms to calculate the type score*/
        Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
        customOntologyTypeScores.put("MI:0208", 0.05f); // cv1 // genetic interaction
        customOntologyTypeScores.put("MI:0403", 0.20f); // cv2 // colocalization
        customOntologyTypeScores.put("MI:0914", 0.20f); // cv3 // association
        customOntologyTypeScores.put("MI:0915", 0.40f); // cv4 // physical association
        customOntologyTypeScores.put("MI:0407", 1.00f); // cv5 // direct interaction
        customOntologyTypeScores.put("unknown", 0.02f); // cv6 // unknown
        ics.setCustomOntologyTypeScores(customOntologyTypeScores);

        /* Custom scores for ontology method terms to calculate the method score */
        Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
        customOntologyMethodScores.put("MI:0013", 1.00f); // cv1 // biophysical
        customOntologyMethodScores.put("MI:0090", 0.66f); // cv2 // protein complementation assay
        customOntologyMethodScores.put("MI:0254", 0.10f); // cv3 // genetic interference
        customOntologyMethodScores.put("MI:0255", 0.10f); // cv4 // post transcriptional interference
        customOntologyMethodScores.put("MI:0401", 1.00f); // cv5 // biochemical
        customOntologyMethodScores.put("MI:0428", 0.33f); // cv6 // imagining technique
        customOntologyMethodScores.put("unknown", 0.05f); // cv7 // unknown
        ics.setCustomOntologyMethodScores(customOntologyMethodScores);

        /* Set a number of publications to set the highest score for the publication score */
        ics.setCustomPublicationNumberWithHighestScore(10);
        
        /* Run service */
        ics.runService();
        /* Retrive results */
        Map<Integer, EncoreInteraction> interactionMapping = ics.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = ics.getInteractorMapping();
        Map<String, String> synonymMapping = ics.getSynonymMapping();        
        int interactionMappingId = ics.getInteractionMappingId();
        //assertTrue(interactionMappingId > 0);
        //assertTrue(interactionMapping.size() > 0);
        //assertTrue(interactorMapping.size() > 0);
    }

    public void testInteractionClusterScoreWithBinaryInteractions(){
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
            URL irefindexQuery = new URL("http://biotin.uio.no:8080/psicquic-ws/webservices/current/search/query/P07200");
            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            PsimiTabReader mitabReader = new PsimiTabReader();
            binaryInteractions.addAll(mitabReader.read(intactQuery));
            binaryInteractions.addAll(mitabReader.read(irefindexQuery));

            /* Run cluster using list of binary interactions as input */
            InteractionClusterScore iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(binaryInteractions.iterator());
            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            //assertTrue(interactionMappingId > 0);
            //assertTrue(interactionMapping.size() > 0);
            //assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PsimiTabException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void testInteractionClusterScoreWithBinaryInteractions_fromInputStream(){
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
            PsimiTabReader mitabReader = new PsimiTabReader();

            Iterator<BinaryInteraction> binaryInteractionsIterator = mitabReader.iterate(intactQuery.openStream());

            /* Run cluster using list of binary interactions as input */
            InteractionClusterScore iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(binaryInteractionsIterator);
            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            //assertTrue(interactionMappingId > 0);
            //assertTrue(interactionMapping.size() > 0);
            //assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void testInteractionClusterScoreWithBinaryInteractionsAndPsicquic(){
        PsimiTabReader mitabReader = new PsimiTabReader();
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P37173");
            URL irefindexQuery = new URL("http://biotin.uio.no:8080/psicquic-ws/webservices/current/search/query/P37173");

            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            binaryInteractions.addAll(mitabReader.read(intactQuery));
            binaryInteractions.addAll(mitabReader.read(irefindexQuery));

            /* Run cluster using list of binary interactions as input */
            InteractionClusterScore iC = new InteractionClusterScore();
            /* Add binary interactions */
            iC.setBinaryInteractionIterator(binaryInteractions.iterator());
            /* Add interactions from Psicquic */
            iC.addQueryAcc("P37173");
            iC.addQuerySource("chembl");
            /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            /* Run clustering service */
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            //assertTrue(interactionMappingId > 0);
            //assertTrue(interactionMapping.size() > 0);
            //assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PsimiTabException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void testEncore2Binary(){
        PsimiTabReader mitabReader = new PsimiTabReader();
        /* Run cluster using list of binary interactions as input */
        InteractionClusterScore iC = new InteractionClusterScore();
        /* Add interactions from Psicquic */
        iC.addQueryAcc("P37173");
//        iC.addQuerySource("chembl");
        iC.addQuerySource("intact");
        /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        /* Run clustering service */
        iC.runService();

        /* Retrieve results */
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();

        /* Get PSI binary Interactions */
        Map<Integer, BinaryInteraction> binaryInteractionMapping = new HashMap<Integer,BinaryInteraction>();        
        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
        for(int mappingId:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);
            binaryInteractionMapping.put(mappingId,bI);
        }        

        /* Test */
        //assertTrue(binaryInteractionMapping.size() > 0);
    }

//    public void testMatrixDB(){
//        InteractionClusterScore ics = new InteractionClusterScore();
//        ics.setOptionalQueryParameters(0, 400);
//        ics.addQueryAcc("*");
//        ics.addQuerySource("MatrixDB");
//        ics.runService();
//        HashMap<Integer, EncoreInteraction> interactionMapping = ics.getInteractionMapping();
//        HashMap<String, ArrayList<Integer>> interactorMapping = ics.getInteractorMapping();
//        int interactionMappingId = ics.getInteractionMappingId();
//        assertTrue(interactionMappingId > 0);
//        assertTrue(interactionMapping.size() > 0);
//        assertTrue(interactorMapping.size() > 0);
//    }



}
