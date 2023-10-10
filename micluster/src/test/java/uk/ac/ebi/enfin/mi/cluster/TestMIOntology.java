package uk.ac.ebi.enfin.mi.cluster;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScoreCache;
import uk.ac.ebi.enfin.mi.score.ols.MIOntology;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;


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
        Assert.assertTrue(children.isEmpty());
    }

    @Test
    public void testGetJsonChildren(){
        MIOntology MIO = new MIOntology();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        Map<String,String> children = MIO.getJsonChildren("MI:0660");
        stopwatch.stop();
        long timeTaken = stopwatch.getTime();
        System.out.println("timeTaken: "+timeTaken);
        Assert.assertTrue(children.size() > 6);
    }

    @Test
    public void menthaPerformanceTestForReactome() {
        InteractionClusterScore interactionClusterScore;
        final int NTHREDS = 20;

        // new code
        ConcurrentHashMap<String,List<BinaryInteraction>> proteinInteractionsMap=new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        //new code
        InteractionClusterScoreCache interactionClusterScoreCache = new InteractionClusterScoreCache();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        String string="P01133,Q9NWQ8,P27986,Q9UBC2,P19174,P42336,P22681,Q13480,Q06124,P29353,Q9Y6I3,Q96B97,O43597,Q07889,P41240,P00533,Q96JA1,Q15262,Q05209,P26045";
        List<String> stringArray = new ArrayList<>(Arrays.asList(string.split(",")));
        for(String id:stringArray) {
            try {

                URL url = new URL("http://mentha.uniroma2.it:9090/psicquic/webservices/current/search/query/"+id);

                //new <code>
                Runnable worker = new PsimiTabReaderRunnable(id,url,proteinInteractionsMap);
                executor.submit(worker);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // do nothing
        }
        for(String protein:proteinInteractionsMap.keySet()){
            interactionClusterScore = new InteractionClusterScore(interactionClusterScoreCache);
            interactionClusterScore.setBinaryInteractionIterator(proteinInteractionsMap.get(protein).iterator());

            // This is the dbSource added in the alias
            interactionClusterScore.setMappingIdDbNames(null);

            interactionClusterScore.runService();
        }
        stopwatch.stop();
        long timeTaken = stopwatch.getTime();
        System.out.println("timeTaken: " + timeTaken);

        Assert.assertTrue(timeTaken <= 76000);

    }

}

