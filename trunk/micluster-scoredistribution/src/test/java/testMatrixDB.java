import uk.ac.ebi.enfin.mi.score.distribution.EncoreMiscoreDistribution;
import org.apache.log4j.Logger;
import junit.framework.TestCase;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 16:35:17
 */
public class testMatrixDB extends TestCase {
    static Logger logger = Logger.getLogger(testMatrixDB.class);

    public void testMatrixDB(){
        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution("MatrixDB");
        mD.createChart();
        mD.saveScores();
    }
}
