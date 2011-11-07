package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Abstract class for binary interaction cluster
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/04/11</pre>
 */

public abstract class AbstractInteractionCluster<T extends EncoreBinaryInteraction> implements BinaryInteractionCluster<T>{

    private static Logger logger = Logger.getLogger(AbstractInteractionCluster.class);

    /* Mapping information */
    protected Map<Integer, T> interactionMapping; // interaction encore id, encoreInteraction object
    protected Map<String, List<Integer>> interactorMapping; // interactor, [interaction encore id]
    protected Map<String, String> synonymMapping; // synonym, interactor
    protected int interactionMappingId = 0;
    protected String mappingIdDbNames = "uniprotkb,chebi";
    protected Binary2Encore binary2Encore;

    /* Query parameters for PSICQUIC */
    protected int queryStart;
    protected int queryRange;

    /* Data to be clustered by other means than PSICQUIC. */
    protected Iterator<BinaryInteraction> binaryInteractionIterator;

    public AbstractInteractionCluster(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames){
        this.binary2Encore = new Binary2Encore();

        this.binaryInteractionIterator = binaryInteractionList.iterator();
        setMappingIdDbNames( mappingIdDbNames );
    }

    public AbstractInteractionCluster(List<BinaryInteraction> binaryInteractionList){
        String [] dbNames;
        if (mappingIdDbNames.contains(",")){
            dbNames = mappingIdDbNames.split(",");
        }
        else {
            dbNames = new String [] {mappingIdDbNames};
        }
        this.binary2Encore = new Binary2Encore(dbNames);

        this.binaryInteractionIterator = binaryInteractionList.iterator();
    }

    public AbstractInteractionCluster( InputStream is, boolean hasHeader ) throws ClusterServiceException {
        this( new InputStreamReader( is ), hasHeader );
    }

    public AbstractInteractionCluster( Reader r, boolean hasHeader ) throws ClusterServiceException {
        if ( r == null ) {
            throw new IllegalArgumentException( "You must give a non null MITAB Reader" );
        }

        String [] dbNames;
        if (mappingIdDbNames.contains(",")){
            dbNames = mappingIdDbNames.split(",");
        }
        else {
            dbNames = new String [] {mappingIdDbNames};
        }
        this.binary2Encore = new Binary2Encore(dbNames);

        final PsimiTabReader reader = new PsimiTabReader( hasHeader );
        try {
            this.binaryInteractionIterator = reader.iterate( r );
        } catch ( Exception e ) {
            throw new ClusterServiceException( "An error occured while read MITAB data", e );
        }
    }

    public AbstractInteractionCluster( Iterator<BinaryInteraction> iterator ) throws ClusterServiceException {
        if ( iterator == null ) {
            throw new IllegalArgumentException( "You must give a non null iterator" );
        }

        this.binaryInteractionIterator = iterator;

        String [] dbNames;
        if (mappingIdDbNames.contains(",")){
            dbNames = mappingIdDbNames.split(",");
        }
        else {
            dbNames = new String [] {mappingIdDbNames};
        }
        this.binary2Encore = new Binary2Encore(dbNames);
    }

    public AbstractInteractionCluster(int queryStart, int queryRange){
        this.queryStart = queryStart;
        if(queryRange > 200){
            logger.warn( "You have set a query range of " + queryRange + " while the maximum supported is 200. Setting it back to 200." );
            this.queryRange = 200;
        } else {
            this.queryRange = queryRange;
        }

        String [] dbNames;
        if (mappingIdDbNames.contains(",")){
            dbNames = mappingIdDbNames.split(",");
        }
        else {
            dbNames = new String [] {mappingIdDbNames};
        }
        this.binary2Encore = new Binary2Encore(dbNames);
    }

    public AbstractInteractionCluster(int queryStart, int queryRange, String mappingIdDbNames){
        this.queryStart = queryStart;
        if(queryRange > 200){
            logger.warn( "You have set a query range of " + queryRange + " while the maximum supported is 200. Setting it back to 200." );
            this.queryRange = 200;
        } else {
            this.queryRange = queryRange;
        }

        this.binary2Encore = new Binary2Encore();
        setMappingIdDbNames( mappingIdDbNames );
    }

    public AbstractInteractionCluster(String mappingIdDbNames){
        this.queryStart = 0;
        this.queryRange = 200;
        this.binary2Encore = new Binary2Encore();
        setMappingIdDbNames( mappingIdDbNames );
    }

    public void runService(){
        if(interactionMapping == null){
            if(ClusterContext.getInstance().getCacheManager().getInteractionCache().size() > 0){
                logger.error("ERROR: CACHE PROBLEM. InteractionCache HAS NOT BEEN RESET");
            }
            interactionMapping = ClusterContext.getInstance().getCacheManager().getInteractionCache();
        }
        if(interactorMapping == null){
            if(ClusterContext.getInstance().getCacheManager().getInteractorCache().size() > 0){
                logger.error("ERROR: CACHE PROBLEM. InteractorCache HAS NOT BEEN RESET");
            }
            interactorMapping = ClusterContext.getInstance().getCacheManager().getInteractorCache();
        }
        if(synonymMapping == null){
            if(ClusterContext.getInstance().getCacheManager().getSynonymCache().size() > 0){
                logger.error("ERROR: CACHE PROBLEM. SynonymCache HAS NOT BEEN RESET");
            }
            synonymMapping = ClusterContext.getInstance().getCacheManager().getSynonymCache();
        }
        setMappingForPsicquic();
        setMappingForBinaryInteractions();
    }

    protected void setMappingForBinaryInteractions(){
        if(binaryInteractionIterator != null){
            String[] idDbNameList = mappingIdDbNames.split(",");
            while ( binaryInteractionIterator.hasNext() ) {
                BinaryInteraction binaryInteraction = binaryInteractionIterator.next();
                setMappings(binaryInteraction, idDbNameList);
            }
        }
    }

    protected abstract void setMappingForPsicquic();

    protected void setMappingForPsicquic(String queryAcc, String querySource) throws IOException {
        if(queryAcc != null && querySource != null){
            /* List of databses names to take into account when retrieving and mapping molecule accessions */
            String[] idDbNameList = mappingIdDbNames.split(",");

            /* Changing values */
            int start = queryStart;
            int range = queryRange;
            int stop = queryStart + queryRange -1;

            PsicquicService pService = new PsicquicService(querySource);
            if(pService.getService() != null){
                int count = 0;
                final Integer serviceInteractionCount = pService.countInteractions( queryAcc );
                if( serviceInteractionCount != null){
                    /* This should always be a number. However something strange happens with the
                    PSICQUIC registry that returns MPIDB (non active) as active. So count could be
                    null. Error reported to PSICQUIC team */
                    count = serviceInteractionCount;
                    logger.debug( "Interaction count for " + querySource + ": " + count );
                }
                int numOfQueries = (int) Math.ceil((double)count/(double)queryRange);
                if( logger.isInfoEnabled() ) logger.info("Psicquic source: " + pService.getServiceName() + " | Number of psicquic queries: " + numOfQueries + " | Query range: " + queryRange + " | Total number of results: " + count);
                for(int i=0; i<numOfQueries; i++){
                    /* Calculate start and range. Used to sequentially query psicquic */
                    start = queryStart + (queryRange*i);
                    stop = start + range - 1;
                    if(stop >= count){
                        range = count - start;
                        stop = start + range -1;
                    }
                    if( logger.isInfoEnabled() ) logger.info("Psicquic source: " + pService.getServiceName() + " | Query num: " + i + " | start: " + start + ", stop: " + stop + ", range: " + range);

                    /* Query psiquic and populate mapping objects */
                    List<BinaryInteraction> searchResult =  pService.getInteractions(queryAcc, start, range);
                    if(searchResult == null){
                        logger.warn("Psicquic result is null. Source: Psicquic source: " + pService.getServiceName() + " | Query num: " + i + " | start: " + start + ", stop: " + stop + ", range: " + range);
                    } else {
                        if(searchResult.isEmpty()){
                            logger.warn("Data inside the psicquic result is Null. Psicquic source: " + pService.getServiceName() + " | Query num: " + i + " | start: " + start + ", stop: " + stop + ", range: " + range);
                        } else {
                            int interactionCount = 0;
                            for (BinaryInteraction interaction : searchResult) {
                                interactionCount++;
                                setMappings(interaction, idDbNameList);
                            }
                        }
                    }
                }
            }
        }
    }

    protected abstract T convertEncoreInteractionFrom(BinaryInteraction interaction, String[] idDbNameList);

    /**
     * This function checks if an interaction is already in the interaction mapping
     * object (interactionMapping). If present it will try to update the interaction with new information.
     * If the interaction is not present it will create a new interaction. It will update
     * as well two helper object:
     *   - interactorMapping: Helps to find interactor in the interactionMapping object
     *   - synonymMapping: Helps to find interactor synonyms not included in the interactorMapping
     * @param interaction
     * @param idDbNameList
     */
    protected void setMappings(BinaryInteraction interaction, String[] idDbNameList){
        T encoreInteraction = convertEncoreInteractionFrom(interaction, idDbNameList);

        if (encoreInteraction != null){
            Map<String,String> interactorAccsA = encoreInteraction.getInteractorAccsA();
            Map<String,String> interactorAccsB = encoreInteraction.getInteractorAccsB();

            /* Get the first acc following the idCbNameList */
            String interactorAccA = getInteractorFromAccs(interactorAccsA, idDbNameList);
            String interactorAccB = getInteractorFromAccs(interactorAccsB, idDbNameList);


            /* Find interactionMapping Ids for iA */
            List<Integer> interactionMappingIdsForInteractorAccA = new ArrayList<Integer>();
            if(interactorMapping.containsKey(interactorAccA)){
                /* Try in the interactor mapping */
                interactionMappingIdsForInteractorAccA = interactorMapping.get(interactorAccA);
            } else if(synonymMapping.containsKey(interactorAccA)){
                /* Otherwise look for synonyms */
                interactorAccA = synonymMapping.get(interactorAccA);
                interactionMappingIdsForInteractorAccA = interactorMapping.get(interactorAccA);
            }

            /* Find interactionMapping Ids for iB */
            List<Integer> interactionMappingIdsForInteractorAccB = new ArrayList<Integer>();
            if(interactorMapping.containsKey(interactorAccB)){
                interactionMappingIdsForInteractorAccB = interactorMapping.get(interactorAccB);
            } else if(synonymMapping.containsKey(interactorAccB)){
                /* Otherwise look for synonyms */
                interactorAccB = synonymMapping.get(interactorAccB);
                interactionMappingIdsForInteractorAccB = interactorMapping.get(interactorAccB);
            }

            /* Find this interaction in the interaction mapping object*/
            int interactionIdFound = 0;
            T mappingEcoreInteraction;
            if(interactorAccA.equalsIgnoreCase(interactorAccB)){
                /* A and B are the same */
                if(interactorMapping.containsKey(interactorAccA)){

                    loop1:
                    for(Integer iId:interactionMappingIdsForInteractorAccA){
                        mappingEcoreInteraction = interactionMapping.get(iId);
                        Map<String,String> mappingInteractorAccsA = mappingEcoreInteraction.getInteractorAccsA();
                        Map<String,String> mappingInteractorAccsB = mappingEcoreInteraction.getInteractorAccsB();
                        for(String acc:mappingInteractorAccsA.values()){
                            if(mappingInteractorAccsB.values().contains(acc)){
                                interactionIdFound = iId;
                                break loop1;
                            }
                        }
                    }
                }
            } else {
                /* A and B are different */
                loop2:
                for(Integer iId:interactionMappingIdsForInteractorAccA){
                    if(interactionMappingIdsForInteractorAccB.contains(iId)){
                        interactionIdFound = iId;
                        break loop2;
                    }
                }
            }

            /* Update synonym mapping with new accs from interactor A and B */
            for(String acc:encoreInteraction.getInteractorAccsA().values()){
                if(!synonymMapping.containsKey(acc) && !acc.equalsIgnoreCase(interactorAccA)){
                    synonymMapping.put(acc, interactorAccA);
                }
            }
            for(String acc:encoreInteraction.getInteractorAccsB().values()){
                if(!synonymMapping.containsKey(acc) && !acc.equalsIgnoreCase(interactorAccB)){
                    synonymMapping.put(acc, interactorAccB);
                }
            }

            /* If the interaction is in the interaction mapping object then update the interaction mapping object with additional information */
            if(interactionIdFound > 0){
                mappingEcoreInteraction = mergeWithExistingEncoreInteraction(encoreInteraction, interactionIdFound);


                interactionMapping.put(interactionIdFound, mappingEcoreInteraction);
            } else {
                //*.- New interaction
                //A.- include a new id in "interactionMapping"
                //B.- include info in interactorMapping (3 cases)
                //1.- Both interactors are new
                //2.- One interactor is new
                //2.1.- query interactor new
                //2.2.- partner interactor new
                //3.- Both interactors are already in (refering to different interactions)


                //A.- include a new id in "interactionMapping"
                interactionMappingId++;
                encoreInteraction.setId(interactionMappingId);
                interactionMapping.put(interactionMappingId, encoreInteraction);

                //B.- include info in interactorMapping (3 cases)
                //1.- Both interactors are new
                if(interactionMappingIdsForInteractorAccA.size() == 0 && interactionMappingIdsForInteractorAccB.size() == 0){
                    List<Integer> qM = new ArrayList<Integer>();
                    qM.add(interactionMappingId);
                    interactorMapping.put(interactorAccA, qM);
                    if(!interactorAccA.equalsIgnoreCase(interactorAccB)){
                        List<Integer> pM = new ArrayList<Integer>();
                        pM.add(interactionMappingId);
                        interactorMapping.put(interactorAccB, pM);
                    }
                }

                //B.- include info in interactorMapping (3 cases)
                //2.- One interactor is new
                //2.1.- query interactor new
                if(interactionMappingIdsForInteractorAccA.size() == 0 && interactionMappingIdsForInteractorAccB.size() > 0){
                    List<Integer> qM = new ArrayList<Integer>();
                    qM.add(interactionMappingId);
                    interactorMapping.put(interactorAccA, qM);
                    List<Integer> pA = interactionMappingIdsForInteractorAccB;
                    pA.add(interactionMappingId);
                    interactorMapping.put(interactorAccB, pA);
                }

                //B.- include info in interactorMapping (3 cases)
                //2.- One interactor is new
                //2.2.- partner interactor new
                if(interactionMappingIdsForInteractorAccA.size() > 0 && interactionMappingIdsForInteractorAccB.size() == 0){
                    List<Integer> pM = new ArrayList<Integer>();
                    pM.add(interactionMappingId);
                    interactorMapping.put(interactorAccB, pM);
                    List<Integer> qA = interactionMappingIdsForInteractorAccA;
                    qA.add(interactionMappingId);
                    interactorMapping.put(interactorAccA, qA);
                }

                //B.- include info in interactorMapping (3 cases)
                //3.- Both interactor are already in (refering to different interactions)
                if(interactionMappingIdsForInteractorAccA.size() > 0 && interactionMappingIdsForInteractorAccB.size() > 0){
                    List<Integer> pA = interactionMappingIdsForInteractorAccB;
                    pA.add(interactionMappingId);
                    interactorMapping.put(interactorAccB, pA);
                    List<Integer> qA = interactionMappingIdsForInteractorAccA;
                    qA.add(interactionMappingId);
                    interactorMapping.put(interactorAccA, qA);
                }
            }
        }
    }

    /**
     * Merge information with existing encore interaction and return the merged interaction
     * @param encoreInteraction
     * @param interactionIdFound
     * @return
     */
    protected T mergeWithExistingEncoreInteraction(T encoreInteraction, int interactionIdFound) {
        T mappingEcoreInteraction;// include additional information about exp, pubmed, ... for this interaction id
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
        return mappingEcoreInteraction;
    }

    protected abstract void processMethodAndType(T encoreInteraction, T mappingEncoreInteraction);

    protected String getInteractorFromAccs(Map<String,String> interactorAccs, String[] idDbNameList){
        String interactorAcc = null;
        for(String idDbName:idDbNameList){
            if(interactorAccs.containsKey(idDbName)){
                interactorAcc =  interactorAccs.get(idDbName);
                break;
            }
        }
        /* If interactor is not in the list take the first one */
        if(interactorAcc == null){
            interactorAcc = interactorAccs.values().iterator().next();
        }
        return interactorAcc;
    }

    public Map<Integer, T> getInteractionMapping() {
        return interactionMapping;
    }

    public void setInteractionMapping(Map<Integer, T> interactionMapping) {
        this.interactionMapping = interactionMapping;
    }

    public Map<String, List<Integer>> getInteractorMapping() {
        return interactorMapping;
    }

    public void setInteractorMapping(Map<String, List<Integer>> interactorMapping) {
        this.interactorMapping = interactorMapping;
    }

    public int getInteractionMappingId() {
        return interactionMappingId;
    }

    public void setInteractionMappingId(int interactionMappingId) {
        this.interactionMappingId = interactionMappingId;
    }

    public Map<String, String> getSynonymMapping() {
        return synonymMapping;
    }

    public void setSynonymMapping(Map<String, String> synonymMapping) {
        this.synonymMapping = synonymMapping;
    }

    public void setMappingIdDbNames( String mappingIdDbNames ) {
        if(mappingIdDbNames != null){
            this.mappingIdDbNames = mappingIdDbNames;

            String [] dbNames;
            if (mappingIdDbNames.contains(",")){
                dbNames = mappingIdDbNames.split(",");
            }
            else {
                dbNames = new String [] {mappingIdDbNames};
            }

            this.binary2Encore.setIdDbNameList(dbNames);

        } else {
            logger.warn("mappingIdDbNames can not be null. Setting the default mappingIdDbNames: " + this.mappingIdDbNames);
        }
    }

    public String getMappingIdDbNames() {
        return mappingIdDbNames;
    }

    public Iterator<BinaryInteraction> getBinaryInteractionIterator() {
        return binaryInteractionIterator;
    }

    public void setBinaryInteractionIterator(Iterator<BinaryInteraction> binaryInteractionIterator) {
        this.binaryInteractionIterator = binaryInteractionIterator;
    }

    public void setBinaryInteractionIterator(InputStream is, boolean hasHeader) throws ClusterServiceException {
        setBinaryInteractionIterator(new InputStreamReader( is ), hasHeader);
    }

    public void setBinaryInteractionIterator(Reader r, boolean hasHeader) throws ClusterServiceException {
        if ( r != null ) {
            final PsimiTabReader reader = new PsimiTabReader( hasHeader );
            try {
                this.binaryInteractionIterator = reader.iterate( r );
            } catch ( Exception e ) {
                throw new ClusterServiceException( "An error occured while read MITAB data", e );
            }
        }
    }

    public abstract void saveScoreInMitab(String fileName) throws IOException;
}
