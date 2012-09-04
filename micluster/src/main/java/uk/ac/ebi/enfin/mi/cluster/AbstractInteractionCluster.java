package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.utils.CompositeInputStream;

import java.io.*;
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
    protected int maxNumerOfQueryRetries = 3;
    protected int maxNumerOfErrorRequests = 50;

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

        final PsimiTabReader reader = new PsimiTabReader( );
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

    protected void setMappingForPsicquic(String miqlQuery, String querySource) throws IOException {
        if(miqlQuery != null && querySource != null){
            /* List of databses names to take into account when retrieving and mapping molecule accessions */
            String[] idDbNameList = mappingIdDbNames.split(",");

            /* Changing values */
            int start = queryStart;
            int range = queryRange;
            int stop = queryStart + queryRange -1;

            PsicquicService pService = new PsicquicService(querySource);
            if(pService.getService() != null){
                int count = 0;
                final Integer serviceInteractionCount = pService.countInteractions( miqlQuery );
                if( serviceInteractionCount != null){
                    /* This should always be a number. However something strange happens with the
                    PSICQUIC registry that returns MPIDB (non active) as active.  */
                    count = serviceInteractionCount;
                    logger.debug( "Interaction count for " + querySource + ": " + count );
                }
                int numOfQueries = (int) Math.ceil((double)count/(double)queryRange);
                if( logger.isInfoEnabled() ) logger.debug("Psicquic source: " + pService.getServiceName() + " | Number of psicquic queries: " + numOfQueries + " | Query range: " + queryRange + " | Total number of results: " + count);
                int queryTry = 0;
                int queryCount = 0;
                int countErrorRequests = 0;
                int countMissingInteractions = 0;
                psicquicLoop:
                while(queryCount < numOfQueries){
                    /* Calculate start and range. Used to sequentially query psicquic */
                    start = queryStart + (queryRange*queryCount);
                    stop = start + range - 1;
                    if(stop >= count){
                        range = count - start;
                        stop = start + range -1;
                    }

                    if( logger.isInfoEnabled() ) logger.debug("Psicquic source: " + pService.getServiceName() + " | Query num: " + queryCount + " | start: " + start + ", stop: " + stop + ", range: " + range);

                    /* Query psiquic and populate mapping objects */
                    List<BinaryInteraction> searchResult = null;
                    try{
                        searchResult =  pService.getInteractions(miqlQuery, start, range);
                    } catch (Exception e) {
                        logger.warn("Problem retrieving data from " + pService.getServiceName() + " | Query num: " + queryCount + " | start: " + start + ", stop: " + stop + ", range: " + range);
                        queryTry++;
                        countErrorRequests++;
                        if (queryTry == maxNumerOfQueryRetries){
                            queryTry = 0;
                            queryCount++;
                            logger.error("Problem retrieving data after " + maxNumerOfQueryRetries + "attempts from " + pService.getServiceName() + " | Query num: " + queryCount + " | start: " + start + ", stop: " + stop + ", range: " + range + ". The program will skip this query and try with the next query.");
                        } else {
                            /* wait for 2 seconds and try again */
                            Thread thisThread = Thread.currentThread();
                            try {
                                thisThread.sleep(2000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if(countErrorRequests > maxNumerOfErrorRequests){
                            logger.error("Stop quering " + pService.getServiceName() + "More than " + maxNumerOfErrorRequests + "error request");
                            break psicquicLoop;
                        }
                    }
                    if(searchResult != null){
                        if(searchResult.isEmpty()){
                            logger.warn("Data inside the psicquic result is empty. Psicquic source: " + pService.getServiceName() + " | Query num: " + queryCount + " | start: " + start + ", stop: " + stop + ", range: " + range);
                        } else {
                            queryTry = 0;
                            queryCount++;
                            for (BinaryInteraction interaction : searchResult) {
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

        if (encoreInteraction != null ){
            Map<String,String> interactorAccsA = encoreInteraction.getInteractorAccsA();
            Map<String,String> interactorAccsB = encoreInteraction.getInteractorAccsB();

            // if at leats one interactor column is given, we can process the encore interaction
            if (!interactorAccsA.isEmpty() || !interactorAccsB.isEmpty()){
                /* Get the first acc following the idCbNameList */
                String interactorAccA = null;
                String interactorAccB = null;
                // two columns are given
                if (!interactorAccsA.isEmpty() && !interactorAccsB.isEmpty()){
                    interactorAccA = getInteractorFromAccs(interactorAccsA, idDbNameList);
                    interactorAccB = getInteractorFromAccs(interactorAccsB, idDbNameList);
                }
                // first column given, not second. We do assume that we have an intramolecular interaction
                else if (!interactorAccsA.isEmpty() && interactorAccsB.isEmpty()){
                    interactorAccA = getInteractorFromAccs(interactorAccsA, idDbNameList);
                    interactorAccB = interactorAccA;

                    encoreInteraction.setInteractorAccsB(interactorAccsA);
                    interactorAccsB = interactorAccsA;
                }
                // second column given, not first. We do assume that we have an intramolecular interaction
                else {
                    interactorAccB = getInteractorFromAccs(interactorAccsB, idDbNameList);
                    interactorAccA = interactorAccB;

                    encoreInteraction.setInteractorAccsA(interactorAccsB);
                    interactorAccsA = interactorAccsB;
                }

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

    public void setBinaryInteractionIterator(InputStream inputStream, boolean hasHeader) throws ClusterServiceException {
        if ( inputStream != null ) {
            final PsimiTabReader reader = new PsimiTabReader( );
            try {
                this.binaryInteractionIterator = reader.iterate( inputStream );
            } catch ( Exception e ) {
                throw new ClusterServiceException( "An error occured while read MITAB data", e );
            }
        }
    }

    public void setBinaryInteractionIterator(File file, boolean hasHeader) throws ClusterServiceException {
        final InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
             throw new ClusterServiceException( "No file found", e );
        }
        setBinaryInteractionIterator(inputStream, hasHeader);
    }

    public void setBinaryInteractionIterator(File[] files, boolean hasHeader) throws ClusterServiceException {
        List<InputStream> streams = new ArrayList<InputStream>( );
        for(File file:files){
            try {
                streams.add( new FileInputStream( file ) );
            } catch (FileNotFoundException e) {
                throw new ClusterServiceException( "No file found", e );
            }
        }
        final InputStream inputStream;
        try {
            inputStream = new CompositeInputStream( streams.iterator() );
        } catch (IOException e) {
            throw new ClusterServiceException( "Input error", e );
        }
        setBinaryInteractionIterator(inputStream, hasHeader);
    }

    public void setBinaryInteractionIterator(Reader r, boolean hasHeader) throws ClusterServiceException {
        if ( r != null ) {
            final PsimiTabReader reader = new PsimiTabReader( );
            try {
                this.binaryInteractionIterator = reader.iterate( r );
            } catch ( Exception e ) {
                throw new ClusterServiceException( "An error occured while read MITAB data", e );
            }
        }
    }

    public abstract void saveResultsInMitab(String fileName) throws IOException;

}
