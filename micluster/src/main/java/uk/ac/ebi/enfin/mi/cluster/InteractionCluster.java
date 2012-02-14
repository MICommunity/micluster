package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class clusters interactions for one specific source.
 * Look at InteractionClusterAdv to cluster interaction from
 * several sources making several queries.
 *
 * @author: Rafael
 * @since 03-Jun-2010
 * @deprecated  This functionality is already in InteractionClusterAdv
 * Time: 14:22:17
 */
@Deprecated
public class InteractionCluster extends AbstractInteractionCluster<EncoreInteraction> {

    private static Logger logger = Logger.getLogger(InteractionCluster.class);

    /* Query parameters for PSICQUIC */
    protected String queryAcc;
    protected String querySource;

    public InteractionCluster(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames){
        super(binaryInteractionList, mappingIdDbNames);
    }

    public InteractionCluster(List<BinaryInteraction> binaryInteractionList){
        super(binaryInteractionList);
    }

    public InteractionCluster( InputStream is, boolean hasHeader ) throws ClusterServiceException {
        this( new InputStreamReader( is ), hasHeader );
    }

    public InteractionCluster( Reader r, boolean hasHeader ) throws ClusterServiceException {
        super(r, hasHeader);
    }

    public InteractionCluster( Iterator<BinaryInteraction> iterator ) throws ClusterServiceException {
        super(iterator);
    }

    public InteractionCluster(String queryAcc, String querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);

        this.queryAcc = queryAcc;
        this.querySource = querySource;
    }

    public InteractionCluster(String queryAcc, String querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        this.queryAcc = queryAcc;
        this.querySource = querySource;
    }

    public InteractionCluster(String queryAcc, String querySource, String mappingIdDbNames){
        super(mappingIdDbNames);
        this.queryAcc = queryAcc;
        this.querySource = querySource;
    }

    public InteractionCluster(String queryAcc, String querySource){
        super(0, 200);
        // this.queryAcc =  = "Q9UER7";
        this.queryAcc = queryAcc;
        this.querySource = querySource;
    }

    protected void setMappingForPsicquic(){
        try {
            super.setMappingForPsicquic(this.queryAcc, this.querySource);
        } catch (IOException e) {
            logger.error("Problem setting mapping for PSICQUIC for query source: "+querySource, e);
        }
    }

    @Override
    protected EncoreInteraction convertEncoreInteractionFrom(BinaryInteraction interaction, String[] idDbNameList) {
        this.binary2Encore.setIdDbNameList(idDbNameList);
        return this.binary2Encore.getEncoreInteraction(interaction);
    }

    @Override
    protected void processMethodAndType(EncoreInteraction encoreInteraction, EncoreInteraction mappingEncoreInteraction) {
        mappingEncoreInteraction.addMethodToPubmed(encoreInteraction.getMethodToPubmed());
        mappingEncoreInteraction.addTypeToPubmed(encoreInteraction.getTypeToPubmed());

    }

    public void saveScoreInMitab(String fileName) throws IOException {
        PsimiTabWriter writer = new PsimiTabWriter();
        File file = new File(fileName);

        Map<Integer, EncoreInteraction> interactionMapping = getInteractionMapping();
        Encore2Binary iConverter = new Encore2Binary(getMappingIdDbNames());

        for(Integer mappingId:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteraction(eI);

            writer.writeOrAppend(bI, file, false);
        }
    }
}
