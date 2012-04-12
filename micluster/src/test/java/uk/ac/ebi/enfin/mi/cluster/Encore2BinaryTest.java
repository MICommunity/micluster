package uk.ac.ebi.enfin.mi.cluster;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;
import psidev.psi.mi.tab.model.BinaryInteraction;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafael
 * @since 03-Jun-2010
 * Time: 15:21:12
 */
public class Encore2BinaryTest {
    private static String bout = "Bad output for";
    static Logger logger = Logger.getLogger( Encore2BinaryTest.class);

    @Test
    public void sourcesClusterUsingSelectedSources(){
        InteractionCluster iC = new InteractionCluster();
        /* Query one or more IDs */
        iC.addQueryAcc("brca2");
//        iC.addQueryAcc("P07200");
        /* sources to query */
//        iC.addQuerySource("MINT");
        iC.addQuerySource("intact");
//        iC.addQuerySource("irefindex");
        /* Set priority for molecules accession mapping (Find database names in MI Ontology) */
        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<Integer, BinaryInteraction> binaryInteractionMapping = new HashMap<Integer,BinaryInteraction>();
        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
        for(int mappingId:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);
            binaryInteractionMapping.put(mappingId,bI);
        }
        Assert.assertTrue(iC != null);
        Assert.assertTrue(binaryInteractionMapping.size() > 0);
    }

    @Test
    public void sourcesCluster(){
        InteractionClusterAdv iC = new InteractionClusterAdv();
        /* Query one or more IDs */
        iC.addQueryAcc("brca2");
//        iC.addQueryAcc("P07200");
        /* sources to query */
//        iC.addQuerySource("MINT");
        iC.addQuerySource("intact");
//        iC.addQuerySource("irefindex");
        /* Set priority for molecules accession mapping (Find database names in MI Ontology) */
        iC.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        iC.runService();
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();
        Map<Integer, BinaryInteraction> binaryInteractionMapping = new HashMap<Integer,BinaryInteraction>();
        Encore2Binary iConverter = new Encore2Binary();
        for(int mappingId:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteraction(eI);
            binaryInteractionMapping.put(mappingId,bI);
        }
        Assert.assertTrue(iC != null);
        Assert.assertTrue(binaryInteractionMapping.size() > 0);
    }

}
