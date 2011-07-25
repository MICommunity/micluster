import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.enfin.mi.score.distribution.FileMiscoreDistribution;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 16:35:17
 */
public class testFile {
    static Logger logger = Logger.getLogger(testFile.class);

    @Test
    @Ignore
    public void testScore(){
        FileMiscoreDistribution mD = new FileMiscoreDistribution("MatrixDB_Scores.txt");
        mD.createChart();
    }

}
