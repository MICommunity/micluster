package uk.ac.ebi.enfin.mi.cluster.score;
import org.junit.Test;
import org.junit.Assert;
import psidev.psi.mi.tab.model.Confidence;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.ExampleFiles;
import uk.ac.ebi.enfin.mi.score.scores.MIScore;

import java.util.*;


/**
 * Test scores for clustered data
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestScoresForClusteredData extends ExampleFiles {
    @Test
    public void testClusterScoresForP37173InIntactAndInnatedbAndMint() throws ClusterServiceException {
        InteractionClusterScore iC = new InteractionClusterScore();
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
        int scoreCount = 0;
        Double[] expectedScores = {0.23642068,0.26973546,0.23642068,0.23642068,0.26973546,0.81777763,0.33150592,0.23642068,0.50613064,0.82401556,0.6230328,0.26973546,0.26973546,0.26973546,0.4996667,0.26973546,0.34777966,0.26973546,0.26973546,0.34777966,0.26973546,0.41277552,0.26973546,0.26973546,0.41277552,0.26973546,0.26973546,0.43887317,0.26973546};
        for(EncoreInteraction EncoreInteraction:interactionMapping.values()){
            List<Confidence> scores = EncoreInteraction.getConfidenceValues();
            for(Confidence score:scores){
                if(score.getType().equalsIgnoreCase("intactPsiscore")){
                    Assert.assertTrue(score.getValue().equalsIgnoreCase(expectedScores[scoreCount].toString()));
                    scoreCount++;
                }
            }
        }
    }
    
    
    @Test
    public void testCustomClusterScoresForP37173InIntactAndInnatedbAndMint() throws ClusterServiceException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setMappingIdDbNames(allMappingNames);
        
        /*CUSTOM SCORES*/
        iC.setTypeWeight(1.0f);
        iC.setMethodWeight(0.5f);
        iC.setPublicationWeight(0.6f);
        /* Custom scores for ontology type terms to calculate the type score*/
        Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
        customOntologyTypeScores.put("MI:0208", 0.05f); // cv1 // genetic interaction
        customOntologyTypeScores.put("MI:0403", 0.20f); // cv2 // colocalization
        customOntologyTypeScores.put("MI:0914", 0.20f); // cv3 // association
        customOntologyTypeScores.put("MI:0915", 0.40f); // cv4 // physical association
        customOntologyTypeScores.put("MI:0407", 1.00f); // cv5 // direct interaction
        customOntologyTypeScores.put("unknown", 0.02f); // cv6 // unknown
        iC.setCustomOntologyTypeScores(customOntologyTypeScores);
        /* Custom scores for ontology method terms to calculate the method score */
        Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
        customOntologyMethodScores.put("MI:0013", 1.00f); // cv1 // biophysical
        customOntologyMethodScores.put("MI:0090", 0.66f); // cv2 // protein complementation assay
        customOntologyMethodScores.put("MI:0254", 0.10f); // cv3 // genetic interference
        customOntologyMethodScores.put("MI:0255", 0.10f); // cv4 // post transcriptional interference
        customOntologyMethodScores.put("MI:0401", 1.00f); // cv5 // biochemical
        customOntologyMethodScores.put("MI:0428", 0.33f); // cv6 // imagining technique
        customOntologyMethodScores.put("unknown", 0.05f); // cv7 // unknown
        iC.setCustomOntologyMethodScores(customOntologyMethodScores);
        /* Set a number of publications to set the highest score for the publication score */
        iC.setCustomPublicationNumberWithHighestScore(10);
        
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
        int scoreCount = 0;
        Double[] expectedScores = {0.17064172,0.194438,0.17064172,0.17064172,0.194438,0.79372567,0.23748763,0.17064172,0.5101404,0.7810743,0.6359532,0.194438,0.194438,0.194438,0.35831782,0.194438,0.24911173,0.194438,0.194438,0.24911173,0.194438,0.29742372,0.194438,0.194438,0.29742372,0.194438,0.194438,0.46284923,0.194438};
        for(EncoreInteraction EncoreInteraction:interactionMapping.values()){
            List<Confidence> scores = EncoreInteraction.getConfidenceValues();
            for(Confidence score:scores){
                if(score.getType().equalsIgnoreCase("intactPsiscore")){
                    Assert.assertTrue(score.getValue().equalsIgnoreCase(expectedScores[scoreCount].toString()));
                    scoreCount++;
                }
            }
        }
    }


    @Test
    public void testGetScoreUsingLocalOntology(){
        Float score = null;

        ArrayList methodInput = new ArrayList();
        methodInput.add("MI:0051");
        methodInput.add("MI:0042");

        ArrayList typeInput = new ArrayList();
        typeInput.add("MI:0208");
        typeInput.add("MI:0403");
        typeInput.add("MI:0407");
        typeInput.add("MI:0407");

        MIScore tS = new MIScore(false);
        tS.setMethodScore(methodInput);
        tS.setTypeScore(typeInput);
        tS.setPublicationScore(4);
        score = tS.getScore();
        Assert.assertTrue(score >= 0 && score <= 1);
    }

    @Test
    public void testClusterScoresWithLocalOntology() throws ClusterServiceException {
        MIScore miscore = new MIScore(false);
        InteractionClusterScore iC = new InteractionClusterScore(miscore);
        iC.setMappingIdDbNames(allMappingNames);
        /* first source */
        iC.setBinaryInteractionIterator(P37173_intact, false);
        iC.runService();
        /* second source */
        iC.setBinaryInteractionIterator(P37173_innatedb, false);
        iC.runService();
        /* third source */
        iC.setBinaryInteractionIterator(P37173_mint, false);
        iC.runService(miscore);

        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        Map<String, String> interactorSynonyms = iC.getSynonymMapping();
        Assert.assertTrue(interactionMapping.size() == 29);
        Assert.assertTrue(interactorMapping.size() == 29);
        Assert.assertTrue(interactorSynonyms.size() == 78);
        Assert.assertTrue(iC.getInteractionMappingId() == interactionMapping.size());
        int scoreCount = 0;
        Double[] expectedScores = {0.23642068,0.26973546,0.23642068,0.23642068,0.26973546,0.81777763,0.33150592,0.23642068,0.50613064,0.82401556,0.6230328,0.26973546,0.26973546,0.26973546,0.4996667,0.26973546,0.34777966,0.26973546,0.26973546,0.34777966,0.26973546,0.41277552,0.26973546,0.26973546,0.41277552,0.26973546,0.26973546,0.43887317,0.26973546};
        for(EncoreInteraction EncoreInteraction:interactionMapping.values()){
            List<Confidence> scores = EncoreInteraction.getConfidenceValues();
            for(Confidence score:scores){
                if(score.getType().equalsIgnoreCase("intactPsiscore")){
                    Assert.assertTrue(score.getValue().equalsIgnoreCase(expectedScores[scoreCount].toString()));
                    scoreCount++;
                }
            }
        }
    }
}