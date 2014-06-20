package uk.ac.ebi.enfin.mi.cluster.score;

import org.apache.log4j.Logger;
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
public class InteractionClusterScore extends InteractionCluster {
    private static final Logger logger = Logger.getLogger(InteractionClusterScore.class);
    protected String fileName;
    protected MIOntology MIO = new MIOntology();
    protected Map<String,Float> customOntologyTypeScores = new HashMap<String,Float>();
    protected Map<String,Float> customOntologyMethodScores = new HashMap<String,Float>();
    protected Integer customPublicationNumberWithHighestScore = null;
    protected Float typeWeight = 1.0f;
    protected Float methodWeight = 1.0f;
    protected Float publicationWeight = 1.0f;
    protected String scoreName = "miscore";

    protected MIScore miscore;

    public InteractionClusterScore(){
        super(0, 200);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    //todo: miScore define as well in runService. Look at removing one of them
    public InteractionClusterScore(MIScore miScore){
        super(0, 200);
        this.miscore = miScore;
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }
    public InteractionClusterScore(List<BinaryInteraction> binaryInteractionList, String mappingIdDbNames) {
        super(binaryInteractionList, mappingIdDbNames);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<BinaryInteraction> binaryInteractionList) {
        super(binaryInteractionList);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(InputStream is, boolean hasHeader) throws ClusterServiceException {
        super(is, hasHeader);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(Reader r, boolean hasHeader) throws ClusterServiceException {
        super(r, hasHeader);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(Iterator<BinaryInteraction> iterator) throws ClusterServiceException {
        super(iterator);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(int queryStart, int queryRange) {
        super(queryStart, queryRange);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(int queryStart, int queryRange, String mappingIdDbNames) {
        super(queryStart, queryRange, mappingIdDbNames);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(String mappingIdDbNames) {
        super(mappingIdDbNames);
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<String> miqlQueries, List<String> querySource, int queryStart, int queryRange){
        super(queryStart, queryRange);
        if (miqlQueries != null){
            this.queryAccs = miqlQueries;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<String> miqlQueries, List<String> querySource, int queryStart, int queryRange, String mappingIdDbNames){
        super(queryStart, queryRange, mappingIdDbNames);
        if (miqlQueries != null){
            this.queryAccs = miqlQueries;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
        this.miscore = new MIScore();
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    public InteractionClusterScore(List<String> miqlQueries, List<String> querySource, int queryStart, int queryRange, String mappingIdDbNames, MIScore miScore){
        super(queryStart, queryRange, mappingIdDbNames);
        if (miqlQueries != null){
            this.queryAccs = miqlQueries;
        }
        if (querySource != null){
            this.querySources = querySource;
        }
        this.miscore = miScore;
        this.interactionMapping = new HashMap<Integer, EncoreInteraction>();
        this.interactorMapping = new HashMap<String, List<Integer>>();
        this.synonymMapping = new HashMap<String, String>();
    }

    protected void runService(MIScore scoreGenerator) {
        logger.debug("runService");
        if(scoreGenerator.isUseOls() == false){
            MIO = new MIOntology(false);
        }
        super.runService();
        processScore();
    }

    private void processScore() {
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
        typeParentTerms.add("MI:0407");
        Map<String, Map<String,String>> mapOfTypeTerms = MIO.getMapOfTerms(typeParentTerms);
        /* No need to look for MI:0403 since it has no children. Just include them in the mappingParentTerms */
        mapOfTypeTerms.put("MI:0403", new HashMap<String, String>());
        /* No need to look for MI:0914 and "MI:0915 in OLS since they are parent terms of MI:0407 */
        mapOfTypeTerms.put("MI:0914", new HashMap<String, String>());
        mapOfTypeTerms.put("MI:0915", new HashMap<String, String>());



        logger.debug("Update interactions");
        for(Integer interactionId:this.getInteractionMapping().keySet()){
            logger.debug("Get encore interaction object");
            EncoreInteraction eI = this.getInteractionMapping().get(interactionId);
            logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
            logger.debug("IntA: "+ eI.getInteractorA() + " , IntB: " + eI.getInteractorB());

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

            if (this.miscore != null){
                this.miscore.setTypeWeight(typeWeight);
                this.miscore.setMethodWeight(methodWeight);
                this.miscore.setPublicationWeight(publicationWeight);
                this.miscore.setMethodScore(methods, mapOfMethodTerms, customOntologyMethodScores);
                this.miscore.setTypeScore(types, mapOfTypeTerms);
                this.miscore.setPublicationScore(numberOfPubmedIds, customPublicationNumberWithHighestScore);
                scoreResult = this.miscore.getScore();
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

    public void saveScoreInMitab(String fileName) throws IOException {
        saveResultsInMitab(fileName);
    }

    public double[] getScores(){
        if(this.getInteractionMapping() == null){
            runService();
        }

        int scoreListSize = this.getInteractionMapping().size();
        double [] scores = new double[scoreListSize];
        int i = 0;

        for(EncoreInteraction eI:this.getInteractionMapping().values()){
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

        Map<Integer, EncoreInteraction> interactionMapping = getInteractionMapping();
        String delimiter = ",";

        logger.info("Saving scores in "+fileName+"...");

        try {
            FileWriter fstream = new FileWriter(fileName);

            for(Integer mappingId:interactionMapping.keySet()){
                EncoreInteraction eI = interactionMapping.get(mappingId);

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

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }
}
