package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Test;
import org.junit.Assert;
import psidev.psi.mi.tab.model.BinaryInteraction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
