package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.model.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Class which extends the EncoreInteraction class to create
 * EncoreInteraction from Psicquic Binary Interactions. Include
 * participant databases to map from MI:0473
 *
 * @author Rafael
 * @since 02-Jun-2010
 * Time: 10:40:20
 */

public class Binary2Encore {
    static Logger logger = Logger.getLogger(EncoreInteraction.class);
    public final static Pattern UNIPROT_ACC = Pattern.compile("[A-Z][0-9][A-Z0-9]{3}[0-9]|[A-Z][0-9][A-Z0-9]{3}[0-9]-[0-9]+", Pattern.CASE_INSENSITIVE);
    public final static Pattern UNIPROT_ID = Pattern.compile("[A-Z0-9]{1,6}_[A-Z0-9]{3,5}", Pattern.CASE_INSENSITIVE);
    String[] idDbNameList = null; // "uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi"; // +other +uniprotkb_id

    public Binary2Encore() {
    }

    public Binary2Encore(String[] idDbNameList) {
        this.idDbNameList = idDbNameList;
    }

    /**
     * Fill the fields of an encore binary interaction and return the pubmed id
     *
     * @param encoreInteraction
     * @param binaryInteraction
     * @return
     */
    private String convertEncoreInteraction(EncoreBinaryInteraction encoreInteraction, BinaryInteraction binaryInteraction) {
        /* set an ID. Since this is a temporal interaction ID = 0 */
        encoreInteraction.setId(0);

        /* get pubmed ids for this interaction */
        /* It should be just one pubmed id per interaction */
        List<CrossReference> iPublications = binaryInteraction.getPublications();
        String validPublication;

        String pubmed = "";
        String doi = "";
        String imex = "";
        String other = "";
        pubLoop:
        for (CrossReference iPublication : iPublications) {
            if (iPublication.getDatabase().equalsIgnoreCase("pubmed")) {
                pubmed = iPublication.getIdentifier();
            } else if (iPublication.getDatabase().equalsIgnoreCase("doi")) {
                doi = iPublication.getIdentifier();
            } else if (iPublication.getDatabase().equalsIgnoreCase("imex")) {
                imex = iPublication.getIdentifier();
            } else {
                other = iPublication.getIdentifier();
            }
        }

        if (pubmed.length() == 0) {
            if (doi.length() == 0) {
                if (imex.length() == 0) {
                    validPublication = other;
                } else {
                    validPublication = imex;
                }
            } else {
                validPublication = doi;
            }
        } else {
            validPublication = pubmed;
        }

        encoreInteraction.addPublicationId(iPublications);

        /* get source name */
        String sourceDatabase = "";
        List<CrossReference> iSourceDatabases = binaryInteraction.getSourceDatabases();
        if (iSourceDatabases.size() > 0) {
            sourceDatabase = iSourceDatabases.get(0).getText();
        }

        /* get all the experiments for this interaction */
        List<CrossReference> iExps = binaryInteraction.getInteractionAcs();
        String experiment = "";
        String database = "";
        for (CrossReference iExp : iExps) {
            // Just take the first one (the first one normally is the original one and the others xref).
            // It is difficult to rely on xrefs since they might provide different information or miss some data.
            // todo: consider xref
            experiment = iExp.getIdentifier();
            database = iExp.getDatabase();
            break;
        }
        encoreInteraction.addExperimentToPubmed(experiment, validPublication);
        if (database.length() > 0) {
            encoreInteraction.addExperimentToDatabase(experiment, database);
        } else {
            encoreInteraction.addExperimentToDatabase(experiment, sourceDatabase);
        }

        /* get the interactor A */
        try {
            Interactor iA = binaryInteraction.getInteractorA();
            EncoreIdentifiers interactorAccsA = getInteractorAccs(iA);
            encoreInteraction.setInteractorAccsA(interactorAccsA.getAccessions());
            encoreInteraction.setOtherInteractorAccsA(interactorAccsA.getOtherAccessions());
            if (iA != null) {
                /* get taxId for interactor A */
                if (iA.getOrganism() != null) {
                    encoreInteraction.addOrganismsA(iA.getOrganism().getIdentifiers());
                } else {
                    logger.warn("Organism is null");
                }
                /* get all biologicalRoles for this interactor */
                if (iA.getBiologicalRoles() != null) {
                    List<CrossReference> biologicalRoles = iA.getBiologicalRoles();
                    for (CrossReference biologicalRole : biologicalRoles) {
                        encoreInteraction.addBiologicalRoleA(biologicalRole.getIdentifier());
                    }
                } else {
                    logger.warn("Biological Role A is null");
                }
                /* get all experimentalRoles for this interactor */
                if (iA.getExperimentalRoles() != null) {
                    List<CrossReference> experimentalRoles = iA.getExperimentalRoles();
                    for (CrossReference experimentalRole : experimentalRoles) {
                        encoreInteraction.addExperimentalRoleA(experimentalRole.getIdentifier());
                    }
                } else {
                    logger.warn("Experimental Role A is null");
                }
                /* get all types for this interactor */
                if (iA.getInteractorTypes() != null) {
                    List<CrossReference> iTypes = iA.getInteractorTypes();
                    for (CrossReference iType : iTypes) {
                        encoreInteraction.addInteractorTypeA(iType.getIdentifier());
                    }
                } else {
                    logger.warn("Interactor Type A is null");
                }
                /* get all xrefs for this interactor */
                if (iA.getXrefs() != null) {
                    encoreInteraction.addXrefsA(iA.getXrefs());
                } else {
                    logger.warn("Interactor Xrefs A is null");
                }
                /* get all annotations for this interactor */
                if (iA.getAnnotations() != null) {
                    encoreInteraction.addAnnotationsA(iA.getAnnotations());
                } else {
                    logger.warn("Interactor Annotations A is null");
                }
                /* get all xrefs for this interactor */
                if (iA.getChecksums() != null) {
                    encoreInteraction.addChecksumsA(iA.getChecksums());
                } else {
                    logger.warn("Interactor Checksum A is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* get the interactor B */
        try {
            Interactor iB = binaryInteraction.getInteractorB();
//            if(iB)
            EncoreIdentifiers interactorAccsB = getInteractorAccs(iB);
            encoreInteraction.setInteractorAccsB(interactorAccsB.getAccessions());
            encoreInteraction.setOtherInteractorAccsB(interactorAccsB.getOtherAccessions());
            if (iB != null) {
                /* get taxId for interactor B */
                if (iB.getOrganism() != null) {
                    encoreInteraction.addOrganismsB(iB.getOrganism().getIdentifiers());
                } else {
                    logger.warn("Organism is null");
                }
                /* get all biologicalRoles for this interactor */
                if (iB.getBiologicalRoles() != null) {
                    List<CrossReference> biologicalRoles = iB.getBiologicalRoles();
                    for (CrossReference biologicalRole : biologicalRoles) {
                        encoreInteraction.addBiologicalRoleB(biologicalRole.getIdentifier());
                    }
                } else {
                    logger.warn("Biological Role B is null");
                }
                /* get all experimentalRoles for this interactor */
                if (iB.getExperimentalRoles() != null) {
                    List<CrossReference> experimentalRoles = iB.getExperimentalRoles();
                    for (CrossReference experimentalRole : experimentalRoles) {
                        encoreInteraction.addExperimentalRoleB(experimentalRole.getIdentifier());
                    }
                } else {
                    logger.warn("Experimental Role B is null");
                }
                /* get all types for this interactor */
                if (iB.getInteractorTypes() != null) {
                    List<CrossReference> iTypes = iB.getInteractorTypes();
                    for (CrossReference iType : iTypes) {
                        encoreInteraction.addInteractorTypeB(iType.getIdentifier());
                    }
                } else {
                    logger.warn("Interactor Type B is null");
                }
                /* get all xrefs for this interactor */
                if (iB.getXrefs() != null) {
                    encoreInteraction.addXrefsB(iB.getXrefs());
                } else {
                    logger.warn("Interactor Xrefs B is null");
                }
                /* get all annotations for this interactor */
                if (iB.getAnnotations() != null) {
                    encoreInteraction.addAnnotationsB(iB.getAnnotations());
                } else {
                    logger.warn("Interactor Bnnotations B is null");
                }
                /* get all xrefs for this interactor */
                if (iB.getChecksums() != null) {
                    encoreInteraction.addChecksumsB(iB.getChecksums());
                } else {
                    logger.warn("Interactor Checksum B is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        /* get scores */
        if (binaryInteraction.getConfidenceValues() != null) {
            encoreInteraction.addConfidenceValues(binaryInteraction.getConfidenceValues());
        } else {
            logger.warn("Confidence Values is null");
        }

        /* get authors */
        if (binaryInteraction.getAuthors() != null) {
            encoreInteraction.addAuthors(binaryInteraction.getAuthors());
        } else {
            logger.warn("Authors is null");
        }
        /* get SourceDatabases */
        if (binaryInteraction.getSourceDatabases() != null) {
            encoreInteraction.addSourceDatabases(binaryInteraction.getSourceDatabases());
        } else {
            logger.warn("SourceDatabases is null");
        }
        /* Warning messages */
        if (validPublication.length() == 0) {
            logger.warn("Pubmed missing for \"" + experiment + "\" in \"" + database + "\"");
        }

        /* get expansions */
        if (binaryInteraction.getComplexExpansion() != null) {
            List<CrossReference> complexExpansion = binaryInteraction.getComplexExpansion();
            for (CrossReference expansion : complexExpansion) {
                encoreInteraction.addExpansion(expansion.getIdentifier());
            }
        } else {
            logger.warn("Expansion is null");
        }

        /* get xrefs */
        if (binaryInteraction.getXrefs() != null) {
            encoreInteraction.addXrefsInteraction(binaryInteraction.getXrefs());
        } else {
            logger.warn("Xrefs is null");
        }

        /* get annotations */
        if (binaryInteraction.getAnnotations() != null) {
            encoreInteraction.addAnnotationsInteraction(binaryInteraction.getAnnotations());
        } else {
            logger.warn("Annotations is null");
        }

        /* get host organisms */
        if (binaryInteraction.getHostOrganism() != null) {
            encoreInteraction.addHostOrganisms(binaryInteraction.getHostOrganism().getIdentifiers());
        } else {
            logger.warn("Host Organism is null");
        }

        /* get parameters */
        if (binaryInteraction.getParameters() != null) {
            encoreInteraction.addParameters(binaryInteraction.getParameters());
        } else {
            logger.warn("Parameters is null");
        }

        /* get creation date */
        if (binaryInteraction.getCreationDate() != null) {
            encoreInteraction.addCreationDates(binaryInteraction.getCreationDate());
        } else {
            logger.warn("Creation date is null");
        }

        /* get update date */
        if (binaryInteraction.getUpdateDate() != null) {
            encoreInteraction.addUpdateDates(binaryInteraction.getUpdateDate());
        } else {
            logger.warn("Creation date is null");
        }

        /* get checksum */
        if (binaryInteraction.getChecksums() != null) {
            encoreInteraction.addChecksumsInteraction(binaryInteraction.getChecksums());
        } else {
            logger.warn("Checksums is null");
        }

        /* get negative */
        encoreInteraction.addNegative(binaryInteraction.isNegativeInteraction());


        // TODO: add calls to setters for the missing MITAB 2.7 and 2.8 fields

        return validPublication;
    }

    public EncoreInteraction getEncoreInteraction(BinaryInteraction binaryInteraction) {
        EncoreInteraction encoreInteraction = new EncoreInteraction();

        String pubmed = convertEncoreInteraction(encoreInteraction, binaryInteraction);

        /* get all types for this interaction */
        List<CrossReference> iTypes = binaryInteraction.getInteractionTypes();
        for (CrossReference iType : iTypes) {
            encoreInteraction.addTypeToPubmed(iType.getIdentifier(), pubmed);
        }

        /* get all methods for this interaction */
        List<CrossReference> iMethods = binaryInteraction.getDetectionMethods();
        for (CrossReference iMethod : iMethods) {
            encoreInteraction.addMethodToPubmed(iMethod.getIdentifier(), pubmed);
        }

        return encoreInteraction;
    }

    public EncoreInteraction getEncoreInteractionForScoring(BinaryInteraction binaryInteraction) {
        EncoreInteraction encoreInteraction = new EncoreInteraction();

        String pubmed = convertEncoreInteraction(encoreInteraction, binaryInteraction);
        if (pubmed.length() > 0) {
            encoreInteraction.getDistinctPublications().add(pubmed);
        }

        /* get all types for this interaction */
        List<CrossReference> iTypes = binaryInteraction.getInteractionTypes();

        /* get all methods for this interaction */
        List<CrossReference> iMethods = binaryInteraction.getDetectionMethods();

        if (iTypes.size() == 1 && iMethods.size() > 1) {
            String type = iTypes.get(0).getIdentifier();

            for (CrossReference meth : iMethods) {
                MethodTypePair pair = new MethodTypePair(meth.getIdentifier(), type);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        } else if (iTypes.size() > 1 && iMethods.size() == 1) {
            String method = iMethods.get(0).getIdentifier();

            for (CrossReference type : iTypes) {
                MethodTypePair pair = new MethodTypePair(method, type.getIdentifier());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        } else if (iMethods.size() == iTypes.size()) {
            for (int i = 0; i < iMethods.size(); i++) {
                String method = iMethods.get(i).getIdentifier();
                String type = iTypes.get(i).getIdentifier();
                MethodTypePair pair = new MethodTypePair(method, type);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        } else if (iMethods.isEmpty() && iTypes.size() > 0) {
            for (CrossReference type : iTypes) {
                MethodTypePair pair = new MethodTypePair(null, type.getIdentifier());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        } else if (iTypes.isEmpty() && iMethods.size() > 0) {
            for (CrossReference meth : iMethods) {
                MethodTypePair pair = new MethodTypePair(meth.getIdentifier(), null);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }
        // do not associate any interaction type with any detection methods. It will be scored independently
        else {
            for (CrossReference type : iTypes) {
                MethodTypePair pair = new MethodTypePair(null, type.getIdentifier());

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
            for (CrossReference meth : iMethods) {
                MethodTypePair pair = new MethodTypePair(meth.getIdentifier(), null);

                if (encoreInteraction.getMethodTypePairListMap().containsKey(pair)) {
                    encoreInteraction.getMethodTypePairListMap().get(pair).add(pubmed);
                } else {
                    List<String> pubmeds = new ArrayList<String>();
                    pubmeds.add(pubmed);

                    encoreInteraction.getMethodTypePairListMap().put(pair, pubmeds);
                }
            }
        }

        return encoreInteraction;
    }

    private void updateAccMap(Map<String, String> accs, String acc, String[] idDbNameList, String sourceIdDbName) {
        if (idDbNameList != null && idDbNameList.length > 0) {
            for (int i = 0; i < idDbNameList.length; i++) {
                String idDbName = idDbNameList[i];
                if (sourceIdDbName.equalsIgnoreCase(idDbName)) {
                    //todo: add option to collect more uniprotkb accs and ids. They could be trembla dn swissprot.
                    //todo: add option to collect uniprotkb recomended names
                    if (idDbName.equalsIgnoreCase("uniprotkb")) {
                        if (accs.containsKey("uniprotkb")) {
                            // Uniprot acc already in accs
                        } else if (!accs.containsKey(sourceIdDbName)) {
                            accs.put(sourceIdDbName, acc);
                        } else {
                            if (UNIPROT_ACC.matcher(acc).matches()) {
                                /* If uniprotKb, do not take into account versions */
                                String upac = acc;
                                final int idx = acc.indexOf(".");
                                if (idx != -1) {
                                    upac = acc.substring(0, idx);
                                }
                                accs.put(sourceIdDbName, upac);
                            }
                        }
                    } else {
                        if (!accs.containsKey(sourceIdDbName)) {
                            accs.put(sourceIdDbName, acc);
                        }
                    }
                }
            }
        }
    }

    private void updateOtherAccMap(Map<String, List<String>> otherAccs, String sourceIdDbName, String acc) {
        List<String> otherAccsList = new ArrayList<String>();
        if (!otherAccs.containsKey(sourceIdDbName)) {
            otherAccsList.add(acc);
            otherAccs.put(sourceIdDbName, otherAccsList);
        } else {
            otherAccsList = otherAccs.get(sourceIdDbName);
            if (!otherAccsList.contains(acc)) {
                otherAccsList.add(acc);
                otherAccs.put(sourceIdDbName, otherAccsList);
            }
        }
    }

    private EncoreIdentifiers getInteractorAccs(Interactor interactor) {
        EncoreIdentifiers eIds = new EncoreIdentifiers();
        Map<String, String> accs = new HashMap<String, String>();
        Map<String, List<String>> otherAccs = new HashMap<String, List<String>>();

        if (interactor != null) {
            /* FIND ACCS FROM THE LIST OF IDS idBdNameList */
            /* Interactor id column : XREF */
            if (interactor.getIdentifiers() != null) {
                for (CrossReference xref : interactor.getIdentifiers()) {
                    String acc = xref.getIdentifier();
                    String sourceIdDbName = xref.getDatabase();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if (firtSize == newSize) {
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
            /* Alternative id column : aXref */
            if (interactor.getAlternativeIdentifiers() != null) {
                for (CrossReference aXref : interactor.getAlternativeIdentifiers()) {
                    String acc = aXref.getIdentifier();
                    String sourceIdDbName = aXref.getDatabase();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if (firtSize == newSize) {
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
            /* Alias id column : alias */
            if (interactor.getAliases() != null) {
                for (Alias alias : interactor.getAliases()) {
                    String acc = alias.getName();
                    String sourceIdDbName = alias.getDbSource();
                    int firtSize = accs.size();
                    updateAccMap(accs, acc, idDbNameList, sourceIdDbName);
                    int newSize = accs.size();
                    /* No acc found from the idDbNameList */
                    if (firtSize == newSize) {
                        updateOtherAccMap(otherAccs, sourceIdDbName, acc);
                    }
                }
            }
        }

        /* If accs is empty take the first acc from other Accs. Delete first acc from otherAcc */
        if (accs.size() == 0) {
            for (String otherAccDb : otherAccs.keySet()) {
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
