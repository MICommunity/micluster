package uk.ac.ebi.enfin.mi.score.distribution;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: rafael
 * Date: 07-Jun-2010
 * Time: 15:41:21
 */
public class MiscoreDistributionImp implements MiscoreDistribution {
    private static final Logger logger = Logger.getLogger(MiscoreDistributionImp.class);
    private InteractionClusterScore iC;
    private String psicquicService;
    private String query;
    private String mappingIdDbNames = "uniprotkb,irefindex,ddbj/embl/genbank,chebi";
    private boolean jobFinished = false;


    public MiscoreDistributionImp(String query, String urlServiceName, String psicquicUrl ) {
        try {
            this.psicquicService = urlServiceName;
            iC = new InteractionClusterScore();
            URL intactQuery = null;
            intactQuery = new URL(psicquicUrl + query);
            PsimiTabReader mitabReader = new PsimiTabReader();
            List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
            binaryInteractions.addAll(mitabReader.read(intactQuery));
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConverterException e) {
            e.printStackTrace();
        }
    }

    public MiscoreDistributionImp(String fileName, File mitabFile, boolean mitabHeader) {
        try {
            this.psicquicService = fileName;
            iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(mitabFile, mitabHeader);
        } catch (ClusterServiceException e) {
            e.printStackTrace();
        }
    }

    public MiscoreDistributionImp(String fileSetName, File[] mitabFiles, boolean mitabHeader) {
        try {
            this.psicquicService = fileSetName;
            iC = new InteractionClusterScore();
            iC.setBinaryInteractionIterator(mitabFiles, mitabHeader);
        } catch (ClusterServiceException e) {
            e.printStackTrace();
        }
    }

    public MiscoreDistributionImp(String query, String psicquicService) {
        this.psicquicService = psicquicService;
        iC = new InteractionClusterScore();
        iC.addMIQLQuery(query);
        if(psicquicService.equalsIgnoreCase("all")){
            iC.setQuerySourcesFromPsicquicRegistry();
        } else {
            iC.addQuerySource(psicquicService);
        }
    }

    public MiscoreDistributionImp(String query, List<String> psicquicServices) {
        this.psicquicService = psicquicServices.toString();
        iC = new InteractionClusterScore();
        iC.addMIQLQuery(query);
        for(String psicquicService:psicquicServices){
            iC.addQuerySource(psicquicService);
        }
    }


    public Map<Integer, EncoreInteraction> getInteractionMapping() {
        return iC.getInteractionMapping();
    }

    public double[] getScores(){
        return iC.getScores();
    }

    public void createChart(){
        String pngFileName = "results/" + psicquicService + "_Histogram.png";
        createChart(pngFileName);
    }

    public void createChart(String pngFileName){
        runService();
        String message = "Score distribution for " + iC.getScores().length + " clustered interactions from " + psicquicService;
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
        saveScores("results/" + psicquicService + "_Scores.txt");
    }

    public void saveScores(String scoreFileName){
        runService();
        logger.info("saving files");
        iC.saveScores(scoreFileName);
    }

    public String getMappingIdDbNames() {
        return mappingIdDbNames;
    }

    public void setMappingIdDbNames(String mappingIdDbNames) {
        this.mappingIdDbNames = mappingIdDbNames;
    }

    private boolean isJobFinished() {
        return jobFinished;
    }

    private void setJobFinished(boolean jobFinished) {
        this.jobFinished = jobFinished;
    }

    private void runService(){
        if(!isJobFinished()){
            /* Set priority for molecule accession mapping (Find psicquicService names in MI Ontology) */
            iC.setMappingIdDbNames(mappingIdDbNames);
            iC.runService();
            setJobFinished(true);
        }
    }
}
