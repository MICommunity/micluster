import junit.framework.TestCase;
import org.apache.log4j.Logger;
import uk.ac.ebi.enfin.mi.score.distribution.EncoreMiscoreDistribution;

import java.util.ArrayList;

/**
 * User: rafael
 * Date: 23-Jun-2010
 * Time: 16:41:47
 */
public class testProteinAcc extends TestCase{
    static Logger logger = Logger.getLogger(testProteinAcc.class);

    public void testProteinAcc(){
//        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution("IntAct","Q9UER7");
        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution("IntAct","P04637");
        mD.createChart();
        mD.saveScores();
    }

    public void testAllDb(){
        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution("all","Q9UER7");
        mD.createChart();
        mD.saveScores();
    }

    public void testSomeDb(){
        ArrayList<String> databases = new ArrayList<String>();
        databases.add("intact");
        databases.add("mint");
        EncoreMiscoreDistribution mD = new EncoreMiscoreDistribution(databases,"Q9UER7");
        mD.createChart();
        mD.saveScores();
    }
 }
