package uk.ac.ebi.enfin.mi.cluster.score;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.enfin.mi.cluster.exception.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.ExampleFiles;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 11:42:47
 */
public class TestSaveScore extends ExampleFiles {
    @Test
    @Ignore
    public void testSaveScores() throws ClusterServiceException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.saveScores("scores.txt");
        Assert.assertTrue(iC.getInteractionMapping().size() > 0);
    }
}
