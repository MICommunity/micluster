package uk.ac.ebi.enfin.mi.cluster.score;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.ExampleFiles;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 11:42:47
 */
public class TestSaveScore extends ExampleFiles {
    @Test
    public void testSaveScores() throws ClusterServiceException {
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.setBinaryInteractionIterator(brca2_intact, false);
        iC.saveScores("scores.txt");
        Assert.assertTrue(iC.getInteractionMapping().size() > 0);
    }
}
