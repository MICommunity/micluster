package uk.ac.ebi.enfin.mi.score.distribution;

import org.apache.log4j.Logger;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteractionForScoring;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.util.List;
import java.util.Map;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 15:41:21
 */
public class EncoreMiscoreDistribution implements MiscoreDistribution {
    private static final Logger logger = Logger.getLogger(EncoreMiscoreDistribution.class);
    private InteractionClusterScore interactionClusterScore;
    private String database;
    private String query;
    public EncoreMiscoreDistribution(String database) {
        this.database = database;
        interactionClusterScore = new InteractionClusterScore();
        interactionClusterScore.addQueryAcc("*");
        if(database.equalsIgnoreCase("all")){
            interactionClusterScore.setQuerySourcesFromPsicquicRegistry();
        } else {
            interactionClusterScore.addQuerySource(database);
        }
        /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
        interactionClusterScore.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        interactionClusterScore.runService();
    }

    public EncoreMiscoreDistribution(String database, String query) {
        this.database = database;
        interactionClusterScore = new InteractionClusterScore();
        interactionClusterScore.addQueryAcc(query);
        if(database.equalsIgnoreCase("all")){
            interactionClusterScore.setQuerySourcesFromPsicquicRegistry();
        } else {
            interactionClusterScore.addQuerySource(database);
        }
        /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
        interactionClusterScore.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        interactionClusterScore.runService();
    }

    public EncoreMiscoreDistribution(List<String> databases, String query) {
        this.database = databases.toString();
        interactionClusterScore = new InteractionClusterScore();
        interactionClusterScore.addQueryAcc(query);
        for(String database:databases){
            interactionClusterScore.addQuerySource(database);
        }
        /* Set priority for molecule accession mapping (Find database names in MI Ontology) */
        interactionClusterScore.setMappingIdDbNames("uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi");
        interactionClusterScore.runService();
    }

    public Map<Integer, EncoreInteractionForScoring> getInteractionMapping() {
        return interactionClusterScore.getInteractionMapping();
    }

    public double[] getScores(){
        return interactionClusterScore.getScores();
    }

    public void createChart(){
        String pngFileName = "results/" + database + "_Histogram.png";
        createChart(pngFileName);
    }

    public void createChart(String pngFileName){
        String message = "Score distribution for " + interactionClusterScore.getScores().length + " clustered interactions from " + database;
        if(query != null){
            message = message + " querying for " + query;
        }
        MiscoreHistogram miH = new MiscoreHistogram();
        miH.setTitle(message);
        miH.setNumberOfBars(20);
        miH.setValues(getScores());
        miH.createChart(pngFileName);
    }

    public void saveScores(){
        saveScores("results/" + database + "_Scores.txt");
    }

    public void saveScores(String scoreFileName){
        logger.info("saving files");
        interactionClusterScore.saveScores(scoreFileName);
    }

}
