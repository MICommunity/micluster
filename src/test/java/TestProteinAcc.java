import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;
import org.apache.log4j.Logger;
import uk.ac.ebi.enfin.mi.score.distribution.MiscoreDistribution;
import uk.ac.ebi.enfin.mi.score.distribution.MiscoreDistributionImp;

import java.io.File;
import java.util.ArrayList;

/**
 * User: rafael
 * Date: 23-Jun-2010
 * Time: 16:41:47
 */
public class TestProteinAcc{
    static Logger logger = Logger.getLogger(TestProteinAcc.class);

    @Test
    public void testFile(){
        final File P37173_mint = new File( TestProteinAcc.class.getResource( "/P37173_mint.tsv" ).getFile() );
        final File P37173_intact = new File( TestProteinAcc.class.getResource( "/P37173_intact.tsv" ).getFile() );
        File[] files = new File[]{P37173_mint,P37173_intact};
        MiscoreDistribution mD = new MiscoreDistributionImp("Test dataset",files,false);
        mD.createChart();
    }

    @Test
    @Ignore
    public void testProteinAcc(){
        MiscoreDistribution mD = new MiscoreDistributionImp("P04637","IntAct");
        mD.createChart();
        mD.saveScores();
    }

    @Test
    @Ignore
    public void testIntactDb(){
        MiscoreDistribution mD = new MiscoreDistributionImp("*:*","IntAct");
        mD.createChart();
        mD.saveScores();
    }

    @Test
    @Ignore
    public void testAllDb(){
        MiscoreDistribution mD = new MiscoreDistributionImp("Q9UER7","all");
        mD.createChart();
        mD.saveScores();
    }

    @Test
    @Ignore
    public void testSomeDb(){
        ArrayList<String> databases = new ArrayList<String>();
        databases.add("intact");
        databases.add("mint");
        MiscoreDistribution mD = new MiscoreDistributionImp("Q9UER7",databases);
        mD.createChart();
        mD.saveScores();
    }
 }
