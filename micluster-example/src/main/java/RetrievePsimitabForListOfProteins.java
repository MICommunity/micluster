import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;


import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Example of how to retrieve interactors for a list of
 * proteins from several molecular interaction resources
 * using "micluster". "micluster" uses PSICQUIC to retrieve
 * molecular interactions and "miscore" to calculate
 * scores based on annotation evidence.
 *    - micluster: http://code.google.com/p/micluster/
 *    - miscore: http://code.google.com/p/miscore/
 *    - PSICQUIC: http://code.google.com/p/psicquic/
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version 1.0
 * @since 1.0
 */
public class RetrievePsimitabForListOfProteins {
    public static void main(String[] args) throws ConverterException, IOException {
        /* List of integrins provided by MBinfo ... https://docs.google.com/viewer?a=v&pid=sites&srcid=bWVjaGFub2Jpby5pbmZvfG1iaW5mb3xneDpjMzNkN2NkYjI5OTA4ZjI */
        String proteins = "P56199,P17301,P26006,P13612,P08648,P23229,Q13683,P53708,Q13797,O75578,Q9UKX5,Q13349,P38570,P20701,P11215,P06756,P20702,P05556,P05107,P05106,P16144,P18084,P18564,P26010,P26012,P68133,P68032,P62736,P60709,P63261,P63267,P08670,Q9Y490,Q9Y4G6,P21333,O75369,Q14315,Q9HBL0,Q68CZ2,Q8IZW8,Q63HR2,Q9BQL6,Q96AC1,Q86UX7,P49023,Q05397,Q14289,Q13418,P12931,P02751,P25391,P24043,Q16787,Q16363,O15230,P07942,P55268,Q13751,A4D0S4,P11047,Q13753,Q9Y6N6,P02671,P02675,P02679,P04004,P24821,P07996,P35442,P10451,Q6UXI9,P05362,P13598,P32942,Q14773,Q9UMF0,P62834,P61224,P18206,P42336,P42338,P48736,O00329,P27986,O00459,Q92569,Q99570,Q8WYR1,Q5UE93,Q53ET0,P31749,P31751,Q9Y243,P28482,P27361,P56945,P46108,P45983,P45984,P53779,P63000,P61586,P60953,Q14185,Q9Y2X7,Q14161,Q14155,Q8WWW0";
        /* PSICQUIC services with molecular interactions (excluding GeneMANIA) */
        String services ="APID,ChEMBL,BioGrid,IntAct,InnateDB,DIP,MPIDB,MINT,MatrixDB,iRefIndex,BIND,Interoporc,STRING,Reactome-FIs,Reactome,InnateDB-IMEx,MolCon,UniProt,I2D-IMEx,I2D,DrugBank,TopFind,Spike,VirHostNet,BindingDB,MBInfo";
        /* Score cutoff to select interactions with a high score (more annotation evidence) */
        Double cutoffScore = 0.40;
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,chebi,irefindex";
        /* File where results will be printed */
        String resultsFileLocation ="src/main/resources/results/RetrievePsimitabForListOfProteins.txt";

        /* Test with few proteins and services */
        proteins = "P56199,P17301,P26006,P13612,P08648";
        services ="APID,ChEMBL,BioGrid,IntAct,InnateDB";

        /* Build query */
        String query = buildMiqlQuery(proteins);

        System.out.println("Application started ...");
        /* Cluster data */
        String[] serviceArray = services.split(",");
        InteractionClusterScore iC = new InteractionClusterScore();
        iC.addMIQLQuery(query);
        iC.setQuerySources(Arrays.asList(serviceArray));
        iC.setMappingIdDbNames(allMappingNames);
        iC.runService();

        /* Process data and print results */
        printResults(iC, cutoffScore);
        System.out.println("... application ended.");
    }

    /**
     * Build MIQL Query
     * @return
     */
    private static String buildMiqlQuery(String proteins){
        String[] proteinArray = proteins.split(",");
        String query = "identifier:(";
        for(String protein:proteinArray){
            /* MIQL Query */
            query += protein;
            query += " OR ";
        }
        query = query.substring(0, query.length()-4);
        query += ")";
        return query;
    }

    /**
     * Process data and print results
     * @param iC InteractionClusterScore result
     * @param cutoffScore Score cut off. Just interactors with a score above the cut off will be displayed.
     */
    private static void printResults(InteractionClusterScore iC, Double cutoffScore) throws ConverterException, IOException {
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();

        /* PRINT SOME RESULTS */
        String firstSeparator = "\t";
        String secondSeparator = ",";

        /* Convert EncoreInteractions into BinaryInteractions */
        List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
        Encore2Binary iConverter = new Encore2Binary(iC.getMappingIdDbNames());
        for(EncoreInteraction eI:iC.getInteractionMapping().values()){
            /* Get score */
            String score = "";
            List<Confidence> confidenceScores = eI.getConfidenceValues();
            for(Confidence confidenceScore:confidenceScores){
                if(confidenceScore.getType().equalsIgnoreCase("miscore")){
                    score = confidenceScore.getValue();
                }
            }
            if(Double.valueOf(score) > cutoffScore){
                binaryInteractions.add(iConverter.getBinaryInteractionForScoring(eI));
            }
        }
        /* Print PSI MITAB clustered binary interactions */
        File file = new File("src/main/resources/results/RetrievePsimitabForListOfProteins.txt");
        PsimiTabWriter writer = new PsimiTabWriter();
        writer.write(binaryInteractions, file);
    }


}
