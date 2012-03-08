package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;

/**
 * Test PSICQUIC functionality in micluster works
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class TestPsicquicQueries {
   @Test
   public void testQueryInIntact(){
       InteractionCluster iC = new InteractionCluster();
       iC.addQueryAcc("P37173");
       iC.addQuerySource("IntAct");
       iC.runService();
       Assert.assertTrue(iC.getInteractionMapping().size() > 7);
    }

    @Test
    public void testPsicquicService() throws IOException {
        PsicquicService pS = new PsicquicService("IntAct");
        Assert.assertTrue(pS.countInteractions("P37173") > 10);
    }
}
