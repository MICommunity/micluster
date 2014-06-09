package uk.ac.ebi.enfin.mi.cluster.model;

import org.apache.log4j.Logger;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Xref;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Class which extends the EncoreInteraction class to create
 * EncoreInteraction from Psicquic Binary Interactions. Include
 * participant databases to map from MI:0473
 * @author Rafael
 * @since 02-Jun-2010
 * Time: 10:40:20
 */

public class Binary2Encore {
    static Logger logger = Logger.getLogger(EncoreInteraction.class);
    public final static Pattern UNIPROT_ACC = Pattern.compile("[O,P,Q][0-9][A-Z, 0-9]{3}[0-9] | \n" +
            "[A-N,R-Z]([0-9][A-Z][A-Z,0-9]{2}){1,2}[0-9]", Pattern.CASE_INSENSITIVE);
            //Pattern.compile("[A-Z][0-9][A-Z0-9]{3}[0-9]|[A-Z][0-9][A-Z0-9]{3}[0-9]-[0-9]+", Pattern.CASE_INSENSITIVE);
    public final static Pattern UNIPROT_ID = Pattern.compile("[A-Z0-9]{1,6}_[A-Z0-9]{3,5}", Pattern.CASE_INSENSITIVE);
    String[] idDbNameList = null; // "uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi"; // +other +uniprotkb_id

    public Binary2Encore() {
    }

    public Binary2Encore(String[] idDbNameList) {
        this.idDbNameList = idDbNameList;
    }

    /**
     * Fill the fields of an encore binary interaction and return the pubmed id
     * @param encoreInteraction
     * @param binaryInteraction
     * @return
     */
    private String convertEncoreInteraction(EncoreBinaryInteraction encoreInteraction, BinaryInteractionCluster binaryInteraction){
        /* set an ID. Since this is a temporal interaction ID = 0 */
        encoreInteraction.setId(0);

        /* get pubmed ids for this interaction */
        /* It should be just one pubmed id per interaction */
        List<Xref> iPublications = binaryInteraction.getPublications();
        String validPublication;

        String pubmed = "";
        String doi = "";
        String imex = "";
        String other = "";
        pubLoop:
        for(Xref iPublication:iPublications){
            if(iPublication.getDatabase().getShortName().equalsIgnoreCase("pubmed")){
                pubmed = iPublication.getId();
            } else if(iPublication.getDatabase().getShortName().equalsIgnoreCase("doi")){
                doi = iPublication.getId();
            } else if(iPublication.getDatabase().getShortName().equalsIgnoreCase("imex")){
                imex = iPublication.getId();
            } else {
                other = iPublication.getId();
            }
        }

        if (pubmed.length() == 0){
            if (doi.length() == 0){
                if (imex.length() == 0){
                    validPublication = other;
                }
                else {
                    validPublication = imex;
                }
            }
            else {
                validPublication = doi;
            }
        }
        else {
            validPublication = pubmed;
        }

        encoreInteraction.addPublicationId(iPublications);

        /* get source name */
        String sourceDatabase = "";
        List<Xref> iSourceDatabases = binaryInteraction.getSourceDatabases();
        if(iSourceDatabases.size() > 0){
            sourceDatabase = iSourceDatabases.get(0).getText();
        }

        /* get all the experiments for this interaction */
        List<Xref> iExps = (List<Xref>) binaryInteraction.getInteractionMapping();
        String experiment = "";
        String database = "";
        for(Xref iExp:iExps){
            // Just take the first one (the first one normally is the original one and the others xref).
            // It is difficult to rely on xrefs since they might provide different information or miss some data.
            // todo: consider xref
            experiment = iExp.getId();
            database = iExp.getDatabase().getShortName();
            break;
        }
        encoreInteraction.addExperimentToPubmed(experiment,  validPublication);
        if(database.length() > 0){
            encoreInteraction.addExperimentToDatabase(experiment, database);
        } else {
            encoreInteraction.addExperimentToDatabase(experiment, sourceDatabase);
        }

        /* get the interactor A */
        try{
            Interactor iA = binaryInteraction.();
            EncoreIdentifiers interactorAccsA = getInteractorAccs(iA);
            encoreInteraction.setInteractorAccsA(interactorAccsA.getAccessions());
            encoreInteraction.setOtherInteractorAccsA(interactorAccsA.getOtherAccessions());
            /* get taxId for interactor A */
            if(iA != null){
                if(iA.getOrganism() != null){
                    encoreInteraction.addOrganismsA(iA.getOrganism().getIdentifiers());
                } else {
                    logger.warn("Organism is null");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        /* get the interactor B */
        try{
            Interactor iB = binaryInteraction.getInteractorB();
//            if(iB)
            EncoreIdentifiers interactorAccsB = getInteractorAccs(iB);
            encoreInteraction.setInteractorAccsB(interactorAccsB.getAccessions());
            encoreInteraction.setOtherInteractorAccsB(interactorAccsB.getOtherAccessions());
            /* get taxId for interactor B */
            if(iB != null){
                if(iB.getOrganism() != null){
                    encoreInteraction.addOrganismsB(iB.getOrganism().getIdentifiers());
                } else {
                    logger.warn("Organism is null");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        /* get scores */
        if(binaryInteraction.getConfidenceValues() != null){
            encoreInteraction.addConfidenceValues(binaryInteraction.getConfidenceValues());
        } else {
            logger.warn("Confidence Values is null");
        }

        /* get authors */
        if(binaryInteraction.getAuthors() != null){
            encoreInteraction.addAuthors(binaryInteraction.getAuthors());
        } else {
            logger.warn("Authors is null");
        }
        /* get SourceDatabases */
        if(binaryInteraction.getSourceDatabases() != null){
            encoreInteraction.addSourceDatabases(binaryInteraction.getSourceDatabases());
        } else {
            logger.warn("SourceDatabases is null");
        }
        /* Warning messages */
        if(validPublication.length() == 0){
            logger.warn("Pubmed missing for \"" + experiment + "\" in \"" + database + "\"");
        }

        return validPublication;
    }

    public EncoreInteraction getEncoreInteraction(BinaryInteractionCluster binaryInteraction) {
        EncoreInteraction encoreInteraction = new EncoreInteraction();

        String pubmed = convertEncoreInteraction(encoreInteraction, binaryInteraction);

        /* get all types for this interaction */
        List<Xref> iTypes = binaryInteraction.getInteractionTypes();
        for(Xref iType:iTypes){
            encoreInteraction.addTypeToPubmed(iType.getId(), pubmed);
        }

        /* get all methods for this interaction */
        List<Xref> iMethods = binaryInteraction.getDetectionMethods();
        for(Xref iMethod:iMethods){
            encoreInteraction.addMethodToPubmed(iMethod.getId(), pubmed);
        }

        return encoreInteraction;
    }

    public EncoreInteraction getEncoreInteractionForScoring(BinaryInteractionCluster binaryInteraction) {
        EncoreInteraction encoreInteraction = new EncoreInteraction();

        String pubmed = convertEncoreInteraction(encoreInteraction, binaryInteraction);
        if (pubmed.length() > 0){
            encoreInteraction.getDistinctPublications().add(pubmed);
        }

        /* get all types for this interaction */
        List<Xref> iTypes = binaryInteraction.getInteractionTypes();

        /* get all methods for this interaction */
        List<Xref> iMethods = binaryInteraction.getDetectionMethods();

        if (iTypes.size() == 1 && iMethods.size() > 1){
            String type = iTypes.get(0).getId();

            for (Xref meth : iMethods){
                MethodTypePair pair = new MethodTypePair(meth.getId(), type);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        else if (iTypes.size() > 1 && iMethods.size() == 1){
            String method = iMethods.get(0).getId();

            for (Xref type : iTypes){
                MethodTypePair pair = new MethodTypePair(method, type.getId());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        else if (iMethods.size() == iTypes.size()) {
            for (int i = 0; i < iMethods.size() ; i++){
                String method = iMethods.get(i).getId();
                String type = iTypes.get(i).getId();
                MethodTypePair pair = new MethodTypePair(method, type);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        else if (iMethods.isEmpty() && iTypes.size() > 0){
            for (Xref type : iTypes){
                MethodTypePair pair = new MethodTypePair(null, type.getId());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        else if (iTypes.isEmpty() && iMethods.size() > 0){
            for (Xref meth : iMethods){
                MethodTypePair pair = new MethodTypePair(meth.getId(), null);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        // do not associate any interaction type with any detection methods. It will be scored independently
        else {
            for (Xref type : iTypes){
                MethodTypePair pair = new MethodTypePair(null, type.getId());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
            for (Xref meth : iMethods){
                MethodTypePair pair = new MethodTypePair(meth.getId(), null);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)){
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                }
                else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }

        return encoreInteraction;
    }

    private void updateAccMap(Map<String,String> accs, String acc, String[] idDbNameList, String sourceIdDbName){
        if(idDbNameList != null && idDbNameList.length > 0){
            for ( int i=0; i<idDbNameList.length; i++ ){
                String idDbName = idDbNameList[i];
                if (sourceIdDbName.equalsIgnoreCase(idDbName)) {
                    //todo: add option to collect more uniprotkb accs and ids. They could be trembla dn swissprot.
                    //todo: add option to collect uniprotkb recomended names
                    if(idDbName.equalsIgnoreCase("uniprotkb")){
                        if(accs.containsKey("uniprotkb")){
                            // Uniprot acc already in accs
                        } else if(!accs.containsKey(sourceIdDbName)){
                            accs.put(sourceIdDbName, acc);
                        } else {
                            if(UNIPROT_ACC.matcher(acc).matches()){
                                /* If uniprotKb, do not take into account versions */
                                String upac = acc;
                                final int idx = acc.indexOf( "." );
                                if( idx != -1 ) {
                                    upac = acc.substring( 0, idx );
                                }
                                accs.put(sourceIdDbName, upac );
                            }
                        }
                    } else {
                        if(!accs.containsKey(sourceIdDbName)){
                            accs.put(sourceIdDbName,acc);
                        }
                    }
                }
            }
        }
    }

    private void updateOtherAccMap(Map<String,List<String>> otherAccs, String sourceIdDbName, String acc){
        List<String> otherAccsList = new ArrayList<String>();
        if(!otherAccs.containsKey(sourceIdDbName)){
            otherAccsList.add(acc);
            otherAccs.put(sourceIdDbName, otherAccsList);
        } else {
            otherAccsList = otherAccs.get(sourceIdDbName);
            if(!otherAccsList.contains(acc)){
                otherAccsList.add(acc);
                otherAccs.put(sourceIdDbName, otherAccsList);
            }
        }
    }

    private EncoreIdentifiers getInteractorAccs(Interactor interactor){
        EncoreIdentifiers eIds = new EncoreIdentifiers();
        Map<String,String> accs = new HashMap<String,String>();
        Map<String,List<String>> otherAccs = new HashMap<String,List<String>>();

        if(interactor != null){
            /* FIND ACCS FROM THE LIST OF IDS idBdNameList */
            /* Interactor id column : XREF */
            if(interactor.getIdentifiers() != null){
                for (Xref xref : interactor.getIdentifiers()) {
                    String acc = xref.getId();
                    String sourceIdDbName = xref.getDatabase().getShortName();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if(firtSize == newSize){
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
            /* Alternative id column : aXref */
            if(interactor.getAlternativeIdentifiers() != null){
                for (Xref aXref : interactor.getAlternativeIdentifiers()) {
                    String acc = aXref.getId();
                    String sourceIdDbName = aXref.getDatabase();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if(firtSize == newSize){
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
            /* Alias id column : alias */
            if(interactor.getAliases() != null){
                for (Alias alias : interactor.getAliases()) {
                    String acc = alias.getName();
                    String sourceIdDbName = alias.getDbSource();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if(firtSize == newSize){
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
        }

        /* If accs is empty take the first acc from other Accs. Delete first acc from otherAcc */
        if(accs.size() == 0){
            for(String otherAccDb:otherAccs.keySet()){
                accs.put(otherAccDb, otherAccs.get(otherAccDb).get(0));
                otherAccs.remove(otherAccDb);
                break;
            }
        }

        /* First result are acc from the selected list. Second result are other accs. */
        eIds.setAccessions(accs);
        eIds.setOtherAccessions(otherAccs);
        return eIds;
    }

    public String[] getIdDbNameList() {
        return idDbNameList;
    }

    public void setIdDbNameList(String[] idDbNameList) {
        this.idDbNameList = idDbNameList;
    }
}
