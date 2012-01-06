package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import org.junit.Test;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.utils.CompositeInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;


/**
 * @author Rafael
 * @since 03-Jun-2010
 * Time: 15:21:12
 */
public class InteractionClusterAdvTest {
    private static String bout = "Bad output for";
    static Logger logger = Logger.getLogger( EncoreInteractionTest.class);

    @Test
    public void sourcesClusterUsingTheRegistry(){
        InteractionClusterAdv iC = new InteractionClusterAdv();
        /* Query one or more IDs */
//        iC.addQueryAcc("P37173");
        iC.addQueryAcc("P07200");
        /* sources to query */
        iC.setQuerySourcesFromPsicquicRegistry();
        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        int interactionMappingId = iC.getInteractionMappingId();
        assertTrue(interactionMappingId > 0);
        assertTrue(interactionMapping.size() > 0);
        assertTrue(interactorMapping.size() > 0);
    }

    @Test
    public void imexSources(){
        InteractionClusterAdv iC = new InteractionClusterAdv();
        /* Query one or more IDs */
//        iC.addQueryAcc("P37173");
        iC.addQueryAcc("P07200");
        /* sources to query */
        iC.setImexSources();
        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        int interactionMappingId = iC.getInteractionMappingId();
        assertTrue(interactionMappingId > 0);
        assertTrue(interactionMapping.size() > 0);
        assertTrue(interactorMapping.size() > 0);
    }



    @Test
    public void clusterFromFile(){
        File intact = new File( InteractionClusterTest.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
        File mint = new File( InteractionClusterTest.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );


        try {
            List<InputStream> streams = new ArrayList<InputStream>( );
            streams.add( new FileInputStream( intact ) );
//            streams.add( new FileInputStream( mint ) );
            final InputStream is = new CompositeInputStream( streams.iterator() );
            InteractionClusterAdv iC = new InteractionClusterAdv();
            iC.setBinaryInteractionIterator(is, true);
            iC.setMappingIdDbNames("uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex");
            iC.runService();
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            int interactionMappingId = iC.getInteractionMappingId();
            assertTrue(interactionMappingId > 0);
            assertTrue(interactionMapping.size() > 0);
            assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClusterServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Test
    public void sourcesClusterUsingSelectedSources(){
        InteractionClusterAdv iC = new InteractionClusterAdv();
        /* Query one or more IDs */
        iC.addQueryAcc("brca2");
//        iC.addQueryAcc("P07200");
//        iC.addQueryAcc("P0A3C9");
//        iC.addQueryAcc("identifier:Q545Q1 AND identifier:Q99490");
        /* sources to query */
//        iC.addQuerySource("irefindex");
        /* IMEX curated databases */
//        iC.addQuerySource("DIP");
//        iC.addQuerySource("APID");
        iC.addQuerySource("IntAct");
//        iC.addQuerySource("MINT");
//        iC.addQuerySource("MPact");
//        iC.addQuerySource("MatrixDB");
//        iC.addQuerySource("MPIDB");
//        iC.addQuerySource("String");
        iC.setMappingIdDbNames("uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
        int interactionMappingId = iC.getInteractionMappingId();
        assertTrue(interactionMappingId > 0);
        assertTrue(interactionMapping.size() > 0);
        assertTrue(interactorMapping.size() > 0);
    }

    @Test
    public void interactionClusterAdvWithBinaryInteractions(){
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
            URL irefindexQuery = new URL("http://irefindex.uio.no:8080/psicquic-ws/webservices/current/search/query/P07200");
            URL APIDQuery = new URL("http://cicblade.dep.usal.es/psicquic-ws/webservices/current/search/interactor/P07200");
            URL biogridQuery = new URL("http://tyerslab.bio.ed.ac.uk:8080/psicquic-ws/webservices/current/search/interactor/brca2");
            PsimiTabReader mitabReader = new PsimiTabReader(false);
            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            binaryInteractions.addAll(mitabReader.read(intactQuery));
            binaryInteractions.addAll(mitabReader.read(irefindexQuery));
            binaryInteractions.addAll(mitabReader.read(APIDQuery));
            binaryInteractions.addAll(mitabReader.read(biogridQuery));

            /* Run cluster using list of binary interactions as input */
            InteractionClusterAdv iC = new InteractionClusterAdv();
            iC.setBinaryInteractionList(binaryInteractions);
//            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            assertTrue(interactionMappingId > 0);
            assertTrue(interactionMapping.size() > 0);
            assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConverterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void interactionClusterAdvWithBinaryInteractionsFromFile(){
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P07200");
            PsimiTabReader mitabReader = new PsimiTabReader(false);
            Iterator<BinaryInteraction> binaryInteractionsIterator = mitabReader.iterate(intactQuery.openStream());

            /* Run cluster using list of binary interactions as input */
            InteractionClusterAdv iC = new InteractionClusterAdv();
            iC.setBinaryInteractionIterator(binaryInteractionsIterator);
//            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            assertTrue(interactionMappingId > 0);
            assertTrue(interactionMapping.size() > 0);
            assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConverterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void interactionClusterAdvWithBinaryInteractionsAndPsicquic(){
        PsimiTabReader mitabReader = new PsimiTabReader(false);
        try {
            /* Get binaryInteractions from PSI-MI files */
            URL intactQuery = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/P37173");
            URL irefindexQuery = new URL("http://irefindex.uio.no:8080/psicquic-ws/webservices/current/search/query/P37173");

            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            binaryInteractions.addAll(mitabReader.read(intactQuery));
            binaryInteractions.addAll(mitabReader.read(irefindexQuery));

            /* Run cluster using list of binary interactions as input */
            InteractionClusterAdv iC = new InteractionClusterAdv();
            /* Add binary interactions */
            iC.setBinaryInteractionList(binaryInteractions);
            /* Add interactions from Psicquic */
            iC.addQueryAcc("P37173");
            iC.addQuerySource("chembl");
            /* Set priority for molecules accession mapping (Find database names in MI Ontology, MI:0473) */
            iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
            /* Run service */
            iC.runService();

            /* Retrieve results */
            Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
            Map<String, List<Integer>> interactorMapping = iC.getInteractorMapping();
            Map<String, String> synonymMapping = iC.getSynonymMapping();
            int interactionMappingId = iC.getInteractionMappingId();

            /* Test */
            assertTrue(interactionMappingId > 0);
            assertTrue(interactionMapping.size() > 0);
            assertTrue(interactorMapping.size() > 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConverterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void sequentialCluster(){
        InteractionClusterAdv ics = new InteractionClusterAdv();

        /* Acc */
        List<String> acc = new ArrayList<String>();
        acc.add("Q06609");

        /* db 1 */
        List<String> db1 = new ArrayList<String>();
        db1.add("IntAct");

        /* db 2 */
        List<String> db2 = new ArrayList<String>();
        db2.add("mint");

        /* First query */
        ics.setQueryAccs(acc);
        ics.setQuerySources(db1);
        ics.runService();

        /* Second query */
        ics.setQueryAccs(acc);
        ics.setQuerySources(db2);
        ics.runService();

        Map<Integer, EncoreInteraction> interactionMapping = ics.getInteractionMapping();
        Map<String, List<Integer>> interactorMapping = ics.getInteractorMapping();
        Map<String, String> synonymMapping = ics.getSynonymMapping();
        int interactionMappingId = ics.getInteractionMappingId();

        assertTrue(interactionMappingId > 0);
        assertTrue(interactionMapping.size() > 0);
        assertTrue(interactorMapping.size() > 0);
    }


//    public void testMatrixDB(){
//        InteractionClusterAdv iC = new InteractionClusterAdv();
//        iC.setOptionalQueryParameters(0, 200);
//        iC.addQueryAcc("*");
//        iC.addQuerySource("MatrixDB");
//        iC.runService();
//        HashMap<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
//        HashMap<String, ArrayList<Integer>> interactorMapping = iC.getInteractorMapping();
//        int interactionMappingId = iC.getInteractionMappingId();
//        assertTrue(interactionMappingId > 0);
//        assertTrue(interactionMapping.size() > 0);
//        assertTrue(interactorMapping.size() > 0);
//    }


}
