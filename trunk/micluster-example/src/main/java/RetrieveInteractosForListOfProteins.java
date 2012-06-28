import psidev.psi.mi.tab.model.Confidence;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;


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
public class RetrieveInteractosForListOfProteins {
    public static void main(String[] args) {
        /* List of integrins provided by MBinfo ... https://docs.google.com/viewer?a=v&pid=sites&srcid=bWVjaGFub2Jpby5pbmZvfG1iaW5mb3xneDpjMzNkN2NkYjI5OTA4ZjI */
        String proteins = "P56199,P17301,P26006,P13612,P08648,P23229,Q13683,P53708,Q13797,O75578,Q9UKX5,Q13349,P38570,P20701,P11215,P06756,P20702,P05556,P05107,P05106,P16144,P18084,P18564,P26010,P26012,P68133,P68032,P62736,P60709,P63261,P63267,P08670,Q9Y490,Q9Y4G6,P21333,O75369,Q14315,Q9HBL0,Q68CZ2,Q8IZW8,Q63HR2,Q9BQL6,Q96AC1,Q86UX7,P49023,Q05397,Q14289,Q13418,P12931,P02751,P25391,P24043,Q16787,Q16363,O15230,P07942,P55268,Q13751,A4D0S4,P11047,Q13753,Q9Y6N6,P02671,P02675,P02679,P04004,P24821,P07996,P35442,P10451,Q6UXI9,P05362,P13598,P32942,Q14773,Q9UMF0,P62834,P61224,P18206,P42336,P42338,P48736,O00329,P27986,O00459,Q92569,Q99570,Q8WYR1,Q5UE93,Q53ET0,P31749,P31751,Q9Y243,P28482,P27361,P56945,P46108,P45983,P45984,P53779,P63000,P61586,P60953,Q14185,Q9Y2X7,Q14161,Q14155,Q8WWW0";
        /* PSICQUIC services with molecular interactions (excluding GeneMANIA) */
        String services ="APID,ChEMBL,BioGrid,IntAct,InnateDB,DIP,MPIDB,MINT,MatrixDB,iRefIndex,BIND,Interoporc,STRING,Reactome-FIs,Reactome,InnateDB-IMEx,MolCon,UniProt,I2D-IMEx,I2D,DrugBank,TopFind,Spike,VirHostNet,BindingDB,MBInfo";
        /* Score cutoff to select interactions with a high score (more annotation evidence) */
        Double cutoffScore = 0.40;
        /* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        final String allMappingNames = "uniprotkb,chebi,irefindex";

        /* print results header */
        System.out.println("Protein"+ "\t" + "Interactors");

        /* Merge and score molecular interactions from different services per protein */
        String[] proteinArray = proteins.split(",");
        String[] serviceArray = services.split(",");
        for(String protein:proteinArray){
            /* MIQL Query */
            String query = "identifier:"+ protein;
            /* Cluster data */
            InteractionClusterScore iC = new InteractionClusterScore();
            iC.addMIQLQuery(query);
            iC.setQuerySources(Arrays.asList(serviceArray));
            iC.setMappingIdDbNames(allMappingNames);
            iC.runService();
            /* Process data and print results */
            printResults(iC, protein, cutoffScore);

        }
    }

    /**
     * Process data and print results
     * @param iC InteractionClusterScore result
     * @param protein Protein accession
     * @param cutoffScore Score cut off. Just interactors with a score above the cut off will be displayed.
     */
    private static void printResults(InteractionClusterScore iC, String protein, Double cutoffScore){
        Map<Integer, EncoreInteraction> interactionMapping = iC.getInteractionMapping();

        Map<String,Double> interactor2score = new HashMap<String, Double>();

        /* PRINT SOME RESULTS */
        String firstSeparator = "\t";
        String secondSeparator = ",";

        /* Loop clustered results */
        for(Integer i:interactionMapping.keySet()){
            EncoreInteraction eI = interactionMapping.get(i);

            /* Get score */
            String score = "";
            List<Confidence> confidenceScores = eI.getConfidenceValues();
            for(Confidence confidenceScore:confidenceScores){
                if(confidenceScore.getType().equalsIgnoreCase("miscore")){
                    score = confidenceScore.getValue();
                }
            }

            /* Get uniprot or chebi acc for interactor A */
            String interactorA = "";
            if(eI.getInteractorAccsA().containsKey("uniprotkb")){
                interactorA = eI.getInteractorAccsA().get("uniprotkb");
            } else if(eI.getInteractorAccsA().containsKey("chebi")){
                interactorA = eI.getInteractorAccsA().get("chebi");
            }

            /* Get uniprot for interactor B */
            String interactorB = "";
            if(eI.getInteractorAccsB().containsKey("uniprotkb")){
                interactorB = eI.getInteractorAccsB().get("uniprotkb");
            }  else if(eI.getInteractorAccsB().containsKey("chebi")){
                interactorB = eI.getInteractorAccsB().get("chebi");
            }

            /* Mke sure both interactors are uniprot or chebi accessions */
            boolean isAccs = false;
            if(interactorA != "" && interactorB != ""){
                String uniprotPattern = "^([A-N,R-Z][0-9][A-Z][A-Z, 0-9][A-Z, 0-9][0-9])|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])$";
                String chebiPattern = "^CHEBI:\\d+$";
                /* Check interactor A */
                boolean isAccA = false;
                if(interactorA.matches(uniprotPattern)){
                    isAccA = true;
                } else if(interactorA.matches(chebiPattern)){
                    isAccA = true;
                }
                /* Check interactor B */
                boolean isAccB = false;
                if(interactorB.matches(uniprotPattern)){
                    isAccB = true;
                } else if(interactorB.matches(chebiPattern)){
                    isAccB = true;
                }
                if(isAccA && isAccB){
                    isAccs = true;
                }
            }

            /* Associate score to interactor just taking into account interactors with uniprot accessions */
            if(isAccs){
                if(!interactorA.equals(protein)){
                    interactor2score.put(interactorA,Double.valueOf(score));
                } else {
                    interactor2score.put(interactorB,Double.valueOf(score));
                }
            }
        }
        /* Sort this map to get the interactors with the higher scores first */
        interactor2score = sortByValue(interactor2score);
        /* Create the list of interactors inclufding the score value */
        String interactorListCSV = "";
        for(String interactor:interactor2score.keySet()){
            if(interactor2score.get(interactor) > cutoffScore){
                if(interactorListCSV != ""){
                    interactorListCSV += secondSeparator;
                }
                interactorListCSV += interactor + "(" + interactor2score.get(interactor) + ")";
            }
        }
        /* Print proteins and their interactors */
        System.out.println(protein + firstSeparator + interactorListCSV);

    }


    /**
     * Sort a map using its values. Higher values first.
     * @param map
     * @return
     */
    private static Map sortByValue(Map map) {
         List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Collections.reverse(list);
        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
             Map.Entry entry = (Map.Entry)it.next();
             result.put(entry.getKey(), entry.getValue());
             }
        return result;
    }

}
