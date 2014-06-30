package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.io.*;
import java.util.*;


/**
 * This class clusters interactions
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public class InteractionCluster extends AbstractInteractionCluster<EncoreInteraction> {
    private static final Logger logger = Logger.getLogger(InteractionCluster.class);

    /* Query parameters for PSICQUIC */
    protected List<String> querySources = new ArrayList<String>();
    protected List<String> queryAccs = new ArrayList<String>();

    public InteractionCluster(){
        super(0, 200);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames) {
        super(binaryInteractionList, mappingIdDbNames);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(List<BinaryInteraction> binaryInteractionList) {
        super(binaryInteractionList);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(InputStream is, boolean hasHeader) throws ClusterServiceException {
        super(is, hasHeader);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(Reader r, boolean hasHeader) throws ClusterServiceException {
        super(r, hasHeader);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(Iterator<BinaryInteraction> iterator) throws ClusterServiceException {
        super(iterator);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(int queryStart, int queryRange) {
        super(queryStart, queryRange);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(int queryStart, int queryRange, String mappingIdDbNames) {
        super(queryStart, queryRange, mappingIdDbNames);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(String mappingIdDbNames) {
        super(mappingIdDbNames);
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(List<String> miqlQueries, List<String> querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);

        if (miqlQueries != null){
            this.queryAccs = miqlQueries;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(List<String> miqlQueries, List<String> querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        if (miqlQueries != null){
            this.queryAccs = miqlQueries;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionCluster(String miqlQuery, String querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);
        this.addMIQLQuery(miqlQuery);
        this.addQuerySource(querySource);
    }

    public InteractionCluster(String miqlQuery, String querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        this.addMIQLQuery(miqlQuery);
        this.addQuerySource(querySource);
    }

    public InteractionCluster(String miqlQuery, String querySource, String mappingIdDbNames){
        super(mappingIdDbNames);
        this.addMIQLQuery(miqlQuery);
        this.addQuerySource(querySource);
    }

    public InteractionCluster(String miqlQuery, String querySource){
        super(0, 200);
        this.addMIQLQuery(miqlQuery);
        this.addQuerySource(querySource);
    }

    @Override
    protected void setMappingForPsicquic() {
        if(queryAccs.size() > 0 && querySources.size() > 0){
            for(String queryAcc:queryAccs){
                for(String querySource:querySources){
                    /* Run cluster service */
                    try {
                        super.setMappingForPsicquic(queryAcc, querySource);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected EncoreInteraction convertEncoreInteractionFrom(BinaryInteraction binaryInteraction, String[] strings) {
        this.binary2Encore.setIdDbNameList(strings);
        return binary2Encore.getEncoreInteractionForScoring(binaryInteraction);
    }

    @Override
    protected void processMethodAndType(EncoreInteraction encoreInteraction, EncoreInteraction mappingEncoreInteraction) {
        Map<MethodTypePair, List<String>> existingMethodTypeToPubmed = mappingEncoreInteraction.getMethodTypePairListMap();

        for (Map.Entry<MethodTypePair, List<String>> entry : encoreInteraction.getMethodTypePairListMap().entrySet()){
            if (existingMethodTypeToPubmed.containsKey(entry.getKey())){
                List<String> existingPubmeds = existingMethodTypeToPubmed.get(entry.getKey());
                List<String> newPubmeds = encoreInteraction.getMethodTypePairListMap().get(entry.getKey());

                for (String pub : newPubmeds){
                    if (!existingPubmeds.contains(pub)){
                        existingPubmeds.add(pub);
                    }
                }
            }
            else{
                existingMethodTypeToPubmed.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void saveResultsInMitab(String fileName) throws IOException {
        PsimiTabWriter writer = new PsimiTabWriter();
        File file = new File(fileName);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        try{
            Map<Integer, EncoreInteraction> interactionMapping = getInteractionMapping();
            Encore2Binary iConverter = new Encore2Binary(getMappingIdDbNames());

            for(Integer mappingId:interactionMapping.keySet()){
                EncoreInteraction eI = interactionMapping.get(mappingId);
                BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);

                writer.write(bI, bufferedWriter);
            }
        }
        finally {
            bufferedWriter.close();
        }
    }

    @Override
    protected EncoreInteraction mergeWithExistingEncoreInteraction(EncoreInteraction encoreInteraction, int interactionIdFound) {
        EncoreInteraction mappingEcoreInteraction;// include additional information about exp, pubmed, ... for this interaction id
        mappingEcoreInteraction = interactionMapping.get(interactionIdFound);
        if( mappingEcoreInteraction == null ) {
            throw new IllegalStateException( "Could not find an EncoreInteraction with id: " + interactionIdFound );
        }
        /* Check that interactors are in the same order A=A and B=B */
        boolean swapInteractors = true;
        swap_loop:
        for(String acc:mappingEcoreInteraction.getInteractorAccsA().values()){
            if(encoreInteraction.getInteractorAccsA().containsValue(acc)){
                swapInteractors = false;
                break swap_loop;
            }
        }
        if(swapInteractors){
            mappingEcoreInteraction.addInteractorAccsA(encoreInteraction.getInteractorAccsB());
            mappingEcoreInteraction.addInteractorAccsB(encoreInteraction.getInteractorAccsA());
            mappingEcoreInteraction.addOtherInteractorAccsA(encoreInteraction.getOtherInteractorAccsB());
            mappingEcoreInteraction.addOtherInteractorAccsB(encoreInteraction.getOtherInteractorAccsA());
            mappingEcoreInteraction.addOrganismsA(encoreInteraction.getOrganismsB());
            mappingEcoreInteraction.addOrganismsB(encoreInteraction.getOrganismsA());
        } else{
            mappingEcoreInteraction.addInteractorAccsA(encoreInteraction.getInteractorAccsA());
            mappingEcoreInteraction.addInteractorAccsB(encoreInteraction.getInteractorAccsB());
            mappingEcoreInteraction.addOtherInteractorAccsA(encoreInteraction.getOtherInteractorAccsA());
            mappingEcoreInteraction.addOtherInteractorAccsB(encoreInteraction.getOtherInteractorAccsB());
            mappingEcoreInteraction.addOrganismsA(encoreInteraction.getOrganismsA());
            mappingEcoreInteraction.addOrganismsB(encoreInteraction.getOrganismsB());
        }
        mappingEcoreInteraction.addPublicationId(encoreInteraction.getPublicationIds());
        mappingEcoreInteraction.addExperimentToPubmed(encoreInteraction.getExperimentToPubmed());
        mappingEcoreInteraction.addExperimentToDatabase(encoreInteraction.getExperimentToDatabase());
        processMethodAndType(encoreInteraction, mappingEcoreInteraction);
        mappingEcoreInteraction.addAuthors(encoreInteraction.getAuthors());
        mappingEcoreInteraction.addConfidenceValues(encoreInteraction.getConfidenceValues());
        mappingEcoreInteraction.addSourceDatabases(encoreInteraction.getSourceDatabases());
        mappingEcoreInteraction.getDistinctPublications().addAll(encoreInteraction.getDistinctPublications());
        return mappingEcoreInteraction;
    }



    public void setQueryAccs(List<String> queryAccs) {
        this.queryAccs = queryAccs;
    }

    public void setMIQLQueries(List<String> miqlQueries) {
        setQueryAccs(miqlQueries);
    }

    public void addQueryAcc(String queryAcc) {
        this.queryAccs.add(queryAcc);
    }

    public void addMIQLQuery(String miqlQuery){
        addQueryAcc(miqlQuery);
    }

    public List<String> getQueryAccs() {
        return queryAccs;
    }

    public List<String> getMIQLQueries() {
        return getQueryAccs();
    }

    public void setQuerySources(List<String> querySources) {
        this.querySources = querySources;
    }

    public void addQuerySource(String querySource) {
        this.querySources.add(querySource);
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

}
