package uk.ac.ebi.enfin.mi.cluster.score;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.ConfidenceImpl;
import uk.ac.ebi.enfin.mi.cluster.*;
import uk.ac.ebi.enfin.mi.score.ols.MIOntology;
import uk.ac.ebi.enfin.mi.score.scores.MIScore;

import java.io.*;
import java.util.*;

/**
 * This class creates an interaction encore object with molecular
 * interaction scores.
 * User: rafael
 * Date: 04-Jun-2010
 * Time: 16:54:10
 */
public class InteractionClusterScore extends AbstractInteractionCluster<EncoreInteractionForScoring> {
    private static final Logger logger = Logger.getLogger(InteractionClusterScore.class);
    protected String fileName;
    protected MIOntology MIO = new MIOntology();
    protected Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
    protected Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
    protected Integer customPublicationNumberWithHighestScore = null;
    protected Float typeWeight = 1.0f;
    protected Float methodWeight = 1.0f;
    protected Float publicationWeight = 1.0f;
    
    protected String scoreName = "intactPsiscore";

    /* Query parameters for PSICQUIC */
    protected List<String> querySources = new ArrayList<String>();
    protected List<String> queryAccs = new ArrayList<String>();

    protected MIScore miscore;

    public InteractionClusterScore(){
        super(0, 200);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(MIScore miScore){
        super(0, 200);
        this.miscore = miScore;

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }
    public InteractionClusterScore(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames) {
        super(binaryInteractionList, mappingIdDbNames);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<BinaryInteraction> binaryInteractionList) {
        super(binaryInteractionList);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(InputStream is, boolean hasHeader) throws ClusterServiceException {
        super(is, hasHeader);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(Reader r, boolean hasHeader) throws ClusterServiceException {
        super(r, hasHeader);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(Iterator<BinaryInteraction> iterator) throws ClusterServiceException {
        super(iterator);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(int queryStart, int queryRange) {
        super(queryStart, queryRange);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(int queryStart, int queryRange, String mappingIdDbNames) {
        super(queryStart, queryRange, mappingIdDbNames);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(String mappingIdDbNames) {
        super(mappingIdDbNames);
        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<String> queryAcc, List<String> querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);

        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }

        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<String> queryAcc, List<String> querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        if (queryAcc != null){
            this.queryAccs = queryAcc;
        }
        if (querySource != null){
            this.querySources = querySource;
        }

        this.miscore = new MIScore();

        this.interactionMapping = new HashMap<Integer, EncoreInteractionForScoring>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    protected void runService(MIScore scoreGenerator) {
        logger.debug("runService");
        super.runService();

        logger.debug("Create a map of method terms using parent terms");
        ArrayList<String> methodParentTerms = new ArrayList<String>();
        methodParentTerms.add("MI:0013");
        methodParentTerms.add("MI:0090");
        methodParentTerms.add("MI:0254");
        methodParentTerms.add("MI:0255");
        methodParentTerms.add("MI:0401");
        methodParentTerms.add("MI:0428");
        Map<String, Map<String,String>> mapOfMethodTerms = MIO.getMapOfTerms(methodParentTerms);

        logger.debug("Create a map of type terms using parent terms");
        ArrayList<String> typeParentTerms = new ArrayList<String>();
        typeParentTerms.add("MI:0208");
        typeParentTerms.add("MI:0403");
        typeParentTerms.add("MI:0407");
        Map<String, Map<String,String>> mapOfTypeTerms = MIO.getMapOfTerms(typeParentTerms);

        logger.debug("Update interactions");
        for(Integer interactionId:this.getInteractionMapping().keySet()){
            logger.debug("Get encore interaction object");
            EncoreInteractionForScoring eI = this.getInteractionMapping().get(interactionId);
            logger.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
            logger.info("IntA: "+ eI.getInteractorA() + " , IntB: " + eI.getInteractorB());

            logger.debug("Find methods and types per publication");
            ArrayList<String> methods = new ArrayList<String>();
            ArrayList<String> types = new ArrayList<String>();
            Map<MethodTypePair, List<String>> methodTypeToPudmed = eI.getMethodTypePairListMap();
            for(MethodTypePair methodType:methodTypeToPudmed.keySet()){
                List<String> uniquePublications = eI.getMethodTypePairListMap().get(methodType);

                String method = methodType.getMethod();
                String type = methodType.getType();

                for (int i = 0; i < uniquePublications.size(); i++){
                    methods.add(method);
                    types.add(type);
                }
            }

            logger.debug("Calculate number of Pubmed ids");
            int numberOfPubmedIds = eI.getDistinctPublications().size();

            logger.debug("Calculate confidence score");
            Float scoreResult = 0f;

            if (scoreGenerator != null){
                scoreGenerator.setTypeWeight(typeWeight);
                scoreGenerator.setMethodWeight(methodWeight);
                scoreGenerator.setPublicationWeight(publicationWeight);
                scoreGenerator.setMethodScore(methods, mapOfMethodTerms, customOntologyMethodScores);
                scoreGenerator.setTypeScore(types, mapOfTypeTerms, customOntologyTypeScores);
                scoreGenerator.setPublicationScore(numberOfPubmedIds, customPublicationNumberWithHighestScore);
                scoreResult = scoreGenerator.getScore();
            }

            logger.debug("Reassign confidence score if it is already present");
            boolean intactPsiscore = false;
            List<Confidence> confidenceValues = eI.getConfidenceValues();
            confidenceValueLoop:
            for(Confidence confidenceValue:confidenceValues){
                if(confidenceValue.getType() == null){
                    logger.warn("Confidence value problem: " + confidenceValue.getValue());
                } else {
                    if(confidenceValue.getType().equalsIgnoreCase(getScoreName())){
                        confidenceValue.setValue(scoreResult.toString());
                        intactPsiscore = true;
                        break confidenceValueLoop;
                    }
                }
            }

            if(!intactPsiscore){
                logger.debug("Set new confidence score");
                Confidence confidenceValue = new ConfidenceImpl();
                confidenceValue.setValue(scoreResult.toString());
                confidenceValue.setType(getScoreName());
                eI.addConfidenceValue(confidenceValue);
            }
        }
    }

    @Override
    public void runService() {
        if (this.miscore == null){
            this.miscore = new MIScore();

        }
        runService(this.miscore);
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
    protected EncoreInteractionForScoring convertEncoreInteractionFrom(BinaryInteraction binaryInteraction, String[] strings) {
        this.binary2Encore.setIdDbNameList(strings);
        return binary2Encore.getEncoreInteractionForScoring(binaryInteraction);
    }

    @Override
    protected void processMethodAndType(EncoreInteractionForScoring encoreInteraction, EncoreInteractionForScoring mappingEncoreInteraction) {
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
    protected EncoreInteractionForScoring mergeWithExistingEncoreInteraction(EncoreInteractionForScoring encoreInteraction, int interactionIdFound) {
        EncoreInteractionForScoring mappingEcoreInteraction;// include additional information about exp, pubmed, ... for this interaction id
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

    @Override
    public void saveScoreInMitab(String fileName) throws IOException {
        PsimiTabWriter writer = new PsimiTabWriter();
        File file = new File(fileName);

        Map<Integer, EncoreInteractionForScoring> interactionMapping = getInteractionMapping();
        Encore2Binary iConverter = new Encore2Binary(getMappingIdDbNames());

        for(Integer mappingId:interactionMapping.keySet()){
            EncoreInteractionForScoring eI = interactionMapping.get(mappingId);
            BinaryInteraction bI = iConverter.getBinaryInteractionForScoring(eI);

            writer.writeOrAppend(bI, file, false);
        }
    }

    public double[] getScores(){
        if(this.getInteractionMapping() == null){
            runService();
        }

        int scoreListSize = this.getInteractionMapping().size();
        double [] scores = new double[scoreListSize];
        int i = 0;

        for(EncoreInteractionForScoring eI:this.getInteractionMapping().values()){
            List<Confidence> confidenceValues = eI.getConfidenceValues();
            Double score = null;
            for(Confidence confidenceValue:confidenceValues){
                if(confidenceValue.getType().equalsIgnoreCase(getScoreName())){
                    score = Double.parseDouble(confidenceValue.getValue());
                }
            }
            if(score == null){
                logger.error("No score for this interaction: " + eI.getId());
            }
            scores[i] = score;
            i++;
        }
        return scores;
    }

    public void saveScores(){
        fileName = "scores.txt";
        saveScores(fileName);
    }

    public void saveScores(String fileName){
        /* Retrieve results */
        if(this.getInteractionMapping() == null || this.interactionMapping.isEmpty()){
            runService();
        }

        Map<Integer, EncoreInteractionForScoring> interactionMapping = getInteractionMapping();
        String delimiter = ",";

        logger.info("Saving scores in "+fileName+"...");

        try {
            FileWriter fstream = new FileWriter(fileName + ".txt");

            for(Integer mappingId:interactionMapping.keySet()){
                EncoreInteractionForScoring eI = interactionMapping.get(mappingId);

                // convert and write in mitab
                if (eI != null){
                    String score = null;

                    for(Confidence confidenceValue:eI.getConfidenceValues()){
                        if(confidenceValue.getType().equalsIgnoreCase(getScoreName())){
                            score = confidenceValue.getValue();
                        }
                    }

                    // write score in a text file
                    if (score != null){
                        fstream.write(score);
                    }
                    else {
                        fstream.write("0");
                    }

                    fstream.write(delimiter);
                    fstream.flush();
                }
            }

            //Close the output stream
            fstream.close();

        } catch (Exception e) {
            logger.error("It is not possible to write the results in the file " + fileName);
            e.printStackTrace();
        }
    }

    public Map<String, Float> getCustomOntologyTypeScores() {
        return customOntologyTypeScores;
    }

    public void setCustomOntologyTypeScores(Map<String, Float> customOntologyTypeScores) {
        this.customOntologyTypeScores = customOntologyTypeScores;
    }

    public Map<String, Float> getCustomOntologyMethodScores() {
        return customOntologyMethodScores;
    }

    public void setCustomOntologyMethodScores(Map<String, Float> customOntologyMethodScores) {
        this.customOntologyMethodScores = customOntologyMethodScores;
    }

    public Integer getCustomPublicationNumberWithHighestScore() {
        return customPublicationNumberWithHighestScore;
    }

    public void setCustomPublicationNumberWithHighestScore(Integer publicationNumberWithHighestScore) {
        this.customPublicationNumberWithHighestScore = publicationNumberWithHighestScore;
    }

    public Float getTypeWeight() {
        return typeWeight;
    }

    public void setTypeWeight(Float typeWeight) {
        this.typeWeight = typeWeight;
    }

    public Float getMethodWeight() {
        return methodWeight;
    }

    public void setMethodWeight(Float methodWeight) {
        this.methodWeight = methodWeight;
    }

    public Float getPublicationWeight() {
        return publicationWeight;
    }

    public void setPublicationWeight(Float publicationWeight) {
        this.publicationWeight = publicationWeight;
    }

    public MIScore getMiscore() {
        return miscore;
    }

    public void setMiscore(MIScore miscore) {
        this.miscore = miscore;
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

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }
}
