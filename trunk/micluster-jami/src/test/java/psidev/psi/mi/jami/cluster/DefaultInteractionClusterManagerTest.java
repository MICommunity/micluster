package psidev.psi.mi.jami.cluster;

import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.commons.MIDataSourceOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.datasource.InteractionStream;
import psidev.psi.mi.jami.factory.MIDataSourceFactory;
import psidev.psi.mi.jami.model.Interaction;

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
        Iterator<InteractionCluster<Interaction>> results = this.manager.getResults();
    }

    @Test
    public void testProcessInteraction() throws Exception {
        while(interactionIterator.hasNext()){
            this.manager.process(interactionIterator.next());
        }
    }

    @Test
    public void testProcessInteractionIterator() throws Exception {
        this.manager.process(interactionIterator);
    }

    @Test
    public void testGetResults() throws Exception {

    }

    private InteractionClusterManager<Interaction,InteractionCluster<Interaction>> manager;
    private String filename = "mitab_samples/ndc80_BIND_and_pcna_HPIDb.tsv";
    private Iterator<Interaction> interactionIterator;
}