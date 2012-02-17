package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Test;
import sun.print.PSStreamPrinterFactory;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

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
       assertTrue(iC.getInteractionMapping().size() > 7);
    }

    @Test
    public void testPsicquicService() throws IOException {
        PsicquicService pS = new PsicquicService("IntAct");
        assertTrue(pS.countInteractions("P37173") > 10);
    }
}
