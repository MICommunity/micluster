package psidev.psi.mi.jami.cluster;

import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.cluster.merge.DefaultInteractorMerger;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.commons.MIDataSourceOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.datasource.InteractionStream;
import psidev.psi.mi.jami.factory.MIDataSourceFactory;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.impl.*;

import java.io.File;
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
        System.out.println("Just to be able to put here a breakpoint");
        Interactor test7 = new DefaultInteractor("Test7", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_7"));
        test7.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"), "TEST_1"));
        Interactor test8 = new DefaultInteractor("Test8", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_8"));
        test8.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"), "TEST_4"));
        test8.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"), "TEST_6"));
        interactionTest = new DefaultInteraction("InteractionTest4");
        interactionTest.addParticipant(new DefaultParticipant(test7));
        interactionTest.addParticipant(new DefaultParticipant(test8));
        this.manager.process(interactionTest);
        System.out.println("Another one just to put here a breakpoint");
    }

    @Test
    public void testProcessInteractionIterator() throws Exception {
        this.manager.process(interactionIterator);
    }

    @Test
    public void testGetResults() throws Exception {
        Interactor test1 = new DefaultInteractor("Test1", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_1"));
        test1.getIdentifiers().add(new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_2"));
        test1.setOrganism(new DefaultOrganism(9606));
        test1.setInteractorType(new DefaultCvTerm("Human"));
        Interactor test2 = new DefaultInteractor("Test2", new DefaultXref(new DefaultCvTerm("IntAct"),"TEST_2"));
        test2.setOrganism(new DefaultOrganism(1010));
        test2.setInteractorType(new DefaultCvTerm("Something Invented"));
        InteractorMerger merger = new DefaultInteractorMerger();
        merger.merge(test1,test2);
        System.out.println("Just to be able to put here a breakpoint");
    }

    @Test
    public void testRealIntActData() throws Exception{
        int i = 0;
        while(interactionIterator.hasNext()) {
            this.manager.process(interactionIterator.next());
            if(++i%1000==0) System.out.println(i + " Time: " + System.currentTimeMillis());
        }
        System.out.println("Just to put a breakpoint");
    }

    private InteractionClusterManager<Interaction,InteractionCluster<Interaction>> manager;
    private String filename = "mitab_samples/my_recudec_intact.txt";//"mitab_samples/ndc80_BIND_and_pcna_HPIDb.tsv";
    private Iterator<Interaction> interactionIterator;
}