import junit.framework.TestCase;
import org.apache.log4j.Logger;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

/**
 * User: rafael
 * Date: 08-Jun-2010
 * Time: 11:42:47
 */
public class TestSaveScore extends TestCase {
    private static String bout = "Bad output for";
    static Logger logger = Logger.getLogger(TestSaveScore.class);
    public void testSaveScores(){
        InteractionClusterScore ics = new InteractionClusterScore();
        ics.addQueryAcc("P04637");
        ics.addQuerySource("IntAct");
        ics.saveScores();
        assertTrue(ics.getInteractionMapping().size() > 0);
    }
}
