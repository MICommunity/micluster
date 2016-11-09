package uk.ac.ebi.enfin.mi.cluster;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;
import uk.ac.ebi.enfin.mi.score.ols.MIOntology;

import java.net.URL;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: rafael
 * Date: 17-May-2010
 * Time: 15:21:06
 * To change this template use File | Settings | File Templates.
 */
public class TestMIOntology {

    @Test
    public void testFalseChildren(){
        MIOntology MIO = new MIOntology();
        Map<String,String> children = MIO.getJsonChildren("MI:99999999");
        Assert.assertTrue(children.size() == 0);
    }

    @Test
    public void testGetJsonChildren(){

        MIOntology MIO = new MIOntology();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        Map<String,String> children = MIO.getJsonChildren("MI:0660");
        stopwatch.stop();
        long timeTaken = stopwatch.getTime();
        System.out.println("timeTaken"+timeTaken);
        Assert.assertTrue(children.size() > 6);
    }

    @Test
    public void mintTestForReactome() {
        InteractionClusterScore interactionClusterScore=null;
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        StopWatch stopwatch2=null;
        long timeTaken2=0;
        String string="P01133,18348,Q9NWQ8,P27986,Q9UBC2,17552,P19174,P42336,P62993-1,P22681,Q13480,Q06124,P29353,Q9Y6I3,15996,Q96B97,O43597,Q07889,P12931-1,P41240,16618,15422,16761,15377,18367,P00533,Q96JA1,Q15262,Q05209,P26045";
List<String> stringArray = new ArrayList<String>(Arrays.asList(string.split(",")));
        for(String id:stringArray) {
            try {

                URL url = new URL("http://mentha.uniroma2.it:9090/psicquic/webservices/current/search/query/"+id);

                List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();

                PsimiTabReader mitabReader = new PsimiTabReader();
                binaryInteractions.addAll(mitabReader.read(url));
              // Collections.addAll((Collection)binaryInteractions,mitabReader.read(url));


// Run cluster using list of binary interactions as input
                interactionClusterScore = new InteractionClusterScore();
                interactionClusterScore.setBinaryInteractionIterator(binaryInteractions.iterator());

// This is the dbSource added in the alias
                interactionClusterScore.setMappingIdDbNames(null);
                stopwatch2 = new StopWatch();
                stopwatch2.start();
                interactionClusterScore.runService();

            } catch (Exception e) {
               e.printStackTrace();
            }
            stopwatch2.stop();
            timeTaken2 += stopwatch2.getTime();
            System.out.println("timeTaken Sub Time"+stopwatch2.getTime());
        }
        stopwatch.stop();
        long timeTaken = stopwatch.getTime();
        System.out.println("timeTaken Sub Step"+timeTaken2 );
        System.out.println("timeTaken"+timeTaken );

    }


    @Test
    public void testGetJsonChildrenFromFile(){
       /* MIOntology MIO = new MIOntology(false);
        Map<String,String> children = MIO.getJsonChildren("MI:0000");*/

    }

}

