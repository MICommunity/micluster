package psidev.psi.mi.jami.cluster;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.model.summary.InteractionClusterSummary;
import psidev.psi.mi.jami.cluster.score.DefaultMIScoreCalculator;
import psidev.psi.mi.jami.cluster.score.MIScoreCalculator;
import psidev.psi.mi.jami.cluster.util.InteractionClusterUtils;
import psidev.psi.mi.jami.commons.MIDataSourceOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.datasource.InteractionStream;
import psidev.psi.mi.jami.factory.MIDataSourceFactory;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.impl.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class DefaultInteractionClusterManagerTest {

    @Before
    public void setUp() throws Exception {
        //Initialise JAMI and read file
        PsiJami.initialiseAllFactories();
        MIDataSourceOptionFactory optionfactory = MIDataSourceOptionFactory.getInstance();
        MIDataSourceFactory dataSourceFactory = MIDataSourceFactory.getInstance();
        File file = new File(ClassLoader.getSystemResource(filename).toURI());
        Map<String, Object> options = optionfactory.getDefaultOptions(file);
        InteractionStream interactionSource = dataSourceFactory.
                getInteractionSourceWith(options);
        interactionIterator = interactionSource.getInteractionsIterator();
        //Initialize Manager and fill it with the information from the file
        this.manager = new DefaultInteractionClusterManager();
    }

    @Test
    public void testClear() throws Exception {
        this.manager.process(interactionIterator);
        Assert.assertTrue(countNumberOfClusters(this.manager.getResults()) == 6);
        Assert.assertTrue(countNumberOfClusters(this.manager.getResults()) == 6);
        this.manager.clear();
        Assert.assertTrue(countNumberOfClusters(this.manager.getResults()) == 0);
    }

    @Test
    public void testProcessInteraction() throws Exception {
        Interactor test1 = new DefaultInteractor("Test1", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_1"));
        test1.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_2"));
        test1.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_5"));
        Interactor test2 = new DefaultInteractor("Test2", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_2"));
        test2.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_1"));
        Interactor test3 = new DefaultInteractor("Test3", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_3"));
        Interactor test4 = new DefaultInteractor("Test4", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_4"));
        Interaction interactionTest = new DefaultInteraction("InteractionTest1");
        interactionTest.addParticipant(new DefaultParticipant(test1));
        interactionTest.addParticipant(new DefaultParticipant(test4));
        this.manager.process(interactionTest);
        interactionTest = new DefaultInteraction("InteractionTest2");
        interactionTest.addParticipant(new DefaultParticipant(test2));
        interactionTest.addParticipant(new DefaultParticipant(test3));
        this.manager.process(interactionTest);
        Interactor test5 = new DefaultInteractor("Test5", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_5"));
        Interactor test6 = new DefaultInteractor("Test6", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_6"));
        test6.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_3"));
        interactionTest = new DefaultInteraction("InteractionTest3");
        interactionTest.addParticipant(new DefaultParticipant(test5));
        interactionTest.addParticipant(new DefaultParticipant(test6));
        this.manager.process(interactionTest);
        Assert.assertTrue(countNumberOfClusters(this.manager.getResults()) == 2);
    }

    @Test
    public void testProcessInteractionIterator() throws Exception {
        this.manager.process(interactionIterator);
        Assert.assertTrue(countNumberOfClusters(this.manager.getResults()) == 6);
    }

    @Test
    public void testBuildSummary() throws Exception {
        this.manager.process(interactionIterator);
        Collection<InteractionClusterSummary> summaries = InteractionClusterUtils.buildSummaries(this.manager.getResults());
        Assert.assertTrue(summaries.size() == 6);
    }

    @Test
    public void testMIScoreCalculator() throws Exception {
        this.manager.process(interactionIterator);
        MIScoreCalculator<InteractionCluster<Interaction>> miScoreCalculator = new DefaultMIScoreCalculator<Interaction, InteractionCluster<Interaction>>("score/scoreCategories.properties");
        Iterator<InteractionCluster<Interaction>> it = this.manager.getResults();
        while (it.hasNext()) {
            InteractionCluster<Interaction> interactionCluster = it.next();
            System.out.println("Interaction: " + interactionCluster.getId() + "; Score: " + miScoreCalculator.computeScore(interactionCluster));
        }
    }

    private int countNumberOfClusters(Iterator<InteractionCluster<Interaction>> iterator){
        int numberOfClusters = 0;
        while(iterator.hasNext()) {
            iterator.next();
            ++numberOfClusters;
        }
        return numberOfClusters;
    }

    private InteractionClusterManager<Interaction,InteractionCluster<Interaction>> manager;
    private String filename = "mitab_samples/ndc80_BIND_and_pcna_HPIDb.tsv";
    private Iterator<Interaction> interactionIterator;
}