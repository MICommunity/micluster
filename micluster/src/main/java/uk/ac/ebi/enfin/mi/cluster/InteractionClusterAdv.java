package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class makes use of InteractionCluster to cluster interactions
 * from several sources making several queries.
 *
 * @author Rafael
 * @since 03-Jun-2010
 * Time: 15:17:54
 */
public class InteractionClusterAdv extends AbstractInteractionCluster<EncoreInteraction>{
    private static Logger logger = Logger.getLogger(InteractionClusterAdv.class);

    /* Query parameters for PSICQUIC */
    protected List<String> querySources = new ArrayList<String>();
    protected List<String> queryAccs = new ArrayList<String>();

    public InteractionClusterAdv(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames){
        super(binaryInteractionList, mappingIdDbNames);
    }

    public InteractionClusterAdv(List<BinaryInteraction> binaryInteractionList){
        super(binaryInteractionList);
    }

    public InteractionClusterAdv( InputStream is, boolean hasHeader ) throws ClusterServiceException {
        this( new InputStreamReader( is ), hasHeader );
    }

    public InteractionClusterAdv( Reader r, boolean hasHeader ) throws ClusterServiceException {
        super(r, hasHeader);
    }

    public InteractionClusterAdv( Iterator<BinaryInteraction> iterator ) throws ClusterServiceException {
        super(iterator);
    }

    public InteractionClusterAdv(List<String> queryAcc, List<String> querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);

        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
    }

    public InteractionClusterAdv(List<String> queryAcc, List<String> querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
    }

    public InteractionClusterAdv(){
        super(0, 200);
    }

    public InteractionClusterAdv(String mappingIdDbNames){
        super(mappingIdDbNames);
    }

    public InteractionClusterAdv(List<String> queryAcc, List<String> querySource, String mappingIdDbNames){
        super(mappingIdDbNames);
        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
    }

    public InteractionClusterAdv(List<String> queryAcc, List<String> querySource){
        super(0, 200);
        // this.queryAcc =  = "Q9UER7";
        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
    }

    protected void setMappingForPsicquic() {
        if(queryAccs.size() > 0 && querySources.size() > 0){
            for(String queryAcc:queryAccs){
                for(String querySource:querySources){
                    /* Run cluster service */
                    super.setMappingForPsicquic(queryAcc, querySource);
                }
            }
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

    protected void setMappingForBinaryInteractions() {
        super.setMappingForBinaryInteractions();
    }

    public void setQuerySourcesFromPsicquicRegistry() {
        PsicquicRegistryClient registryClient = new DefaultPsicquicRegistryClient();
        try {
            List<ServiceType> allServices =  registryClient.listServices();
            for (final ServiceType service : allServices) {
                if (service.isActive()) {
                    this.addQuerySource(service.getName());
                }
            }
        } catch (PsicquicRegistryClientException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public void setImexSources() {
        PsicquicRegistryClient registryClient = new DefaultPsicquicRegistryClient();
        try {
            List<ServiceType> allServices =  registryClient.listServices();
            for (final ServiceType service : allServices) {
                List<String> tags = service.getTags();
                if (service.isActive() && tags != null) {
                    if(tags.contains("MI:0959")){
                        this.addQuerySource(service.getName());
                    }
                }
            }
        } catch (PsicquicRegistryClientException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public List<String> getQuerySources() {
        return querySources;
    }

    public List<String> getQueryAccs() {
        return queryAccs;
    }

    public void setBinaryInteractionList(List<BinaryInteraction> binaryInteractionList) {
        this.binaryInteractionIterator = binaryInteractionList.iterator();
    }

    public void setQueryAccs(List<String> queryAccs) {
        this.queryAccs = queryAccs;
    }

    public void addQueryAcc(String queryAcc) {
        this.queryAccs.add(queryAcc);
    }

    public void setQuerySources(List<String> querySources) {
        this.querySources = querySources;
    }

    public void addQuerySource(String querySource) {
        this.querySources.add(querySource);
    }
}
