package uk.ac.ebi.enfin.mi.cluster;
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.util.*;

/**
 * Test the cluster provides the result we are expecting
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestClusterDataConsistency extends ExampleFiles{
    @Test
    /* This two interactions have the same interactors gene names,
    but different UniProt accessions since one interaction is for
    Human and the another for Mouse.
     */
    public void testSizeOfClusteredInteractionAndInteractorsForBrca2InIntact() throws ClusterServiceException, IOException {
        InteractionCluster iC = new InteractionCluster();
        iC.setBinaryInteractionIterator(EBI988710_AND_EBI1003831, false);
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();
        Map<Integer, EncoreInteractionForScoring> interactionMapping = iC.getInteractionMapping();
        Assert.assertEquals(interactionMapping.size(), 2);
    }
}
