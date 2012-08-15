import org.junit.Ignore;
import uk.ac.ebi.enfin.mi.score.distribution.MiscoreDistributionImp;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;
import uk.ac.ebi.enfin.mi.score.distribution.MiscoreDistribution;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 16:35:17
 */


public class TestMatrixDB {
    static Logger logger = Logger.getLogger(TestMatrixDB.class);

    @Test
    @Ignore
    public void testMatrixDB(){
        MiscoreDistribution mD = new MiscoreDistributionImp("*:*","MatrixDB");
        mD.createChart();
        mD.saveScores();
    }
}
