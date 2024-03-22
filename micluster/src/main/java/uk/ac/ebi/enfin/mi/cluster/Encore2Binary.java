package uk.ac.ebi.enfin.mi.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.tab.model.*;
import uk.ac.ebi.enfin.mi.score.ols.MIOntology;

import java.util.*;

/**
 * User: rafael
 * Date: 24-Aug-2010
 * Time: 16:05:23
 */
//todo: test encore2Binary
public class Encore2Binary {

    private static final Log log = LogFactory.getLog(Encore2Binary.class);

    private Map<String, String> miMethodOntologyTerms;
    private Map<String, String> miInteractionTypeOntologyTerms;
    private Map<String, String> miInteractorTypeOntologyTerms;
    private Map<String, String> miBiologicalRoleOntologyTerms;
    private Map<String, String> miExperimentalRoleOntologyTerms;
    private Map<String, String> miExpansionOntologyTerms;
    protected String mappingIdDbNames;

    public Encore2Binary(String mappingIdDbNames) {
        MIOntology olsHelper = new MIOntology();
        this.miMethodOntologyTerms = olsHelper.getJsonChildren("MI:0001");
        this.miInteractionTypeOntologyTerms = olsHelper.getJsonChildren("MI:0190");
        this.miInteractorTypeOntologyTerms = olsHelper.getJsonChildren("MI:0313");
        this.miBiologicalRoleOntologyTerms = olsHelper.getJsonChildren("MI:0500");
        this.miExperimentalRoleOntologyTerms = olsHelper.getJsonChildren("MI:0495");
        this.miExpansionOntologyTerms = olsHelper.getJsonChildren("MI:1059");
        this.mappingIdDbNames = mappingIdDbNames;
    }

    public Encore2Binary() {
        MIOntology olsHelper = new MIOntology();
        this.miMethodOntologyTerms = olsHelper.getJsonChildren("MI:0001");
        this.miInteractionTypeOntologyTerms = olsHelper.getJsonChildren("MI:0190");
        this.miInteractorTypeOntologyTerms = olsHelper.getJsonChildren("MI:0313");
        this.miBiologicalRoleOntologyTerms = olsHelper.getJsonChildren("MI:0500");
        this.miExperimentalRoleOntologyTerms = olsHelper.getJsonChildren("MI:0495");
        this.miExpansionOntologyTerms = olsHelper.getJsonChildren("MI:1059");
        this.mappingIdDbNames = null;
    }

    @Deprecated
    public BinaryInteraction getBinaryInteraction(EncoreBinaryInteraction encoreInteraction) {
        /* Create Identifiers and Alternative Identifiers */
        List<CrossReference> psiIdentifierA = new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierA = new ArrayList<CrossReference>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String, String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<CrossReference> psiIdentifierB = new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierB = new ArrayList<CrossReference>();
        String encoreInteractorB = encoreInteraction.getInteractorB(mappingIdDbNames);
        Map<String, String> encoreInteractorAccsB = encoreInteraction.getInteractorAccsB();
        updateIdentifiers(psiIdentifierB, psiAlternativeIdentifierB, encoreInteractorAccsB, encoreInteractorB);

        /* Create Alias */
        List<Alias> psiAliasA = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorA = encoreInteraction.getOtherInteractorAccsA();
        updateAlias(psiAliasA, encoreOtherInteractorA);
        List<Alias> psiAliasB = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorB = encoreInteraction.getOtherInteractorAccsB();
        updateAlias(psiAliasB, encoreOtherInteractorB);

        /* Create Organism */
        Organism psiOrganismA = new OrganismImpl();
        psiOrganismA.setIdentifiers(encoreInteraction.getOrganismsA());
        Organism psiOrganismB = new OrganismImpl();
        psiOrganismB.setIdentifiers(encoreInteraction.getOrganismsB());

        /* Create interactor A */
        Interactor psiInteractorA = new Interactor();
        psiInteractorA.setIdentifiers(psiIdentifierA);
        psiInteractorA.setAlternativeIdentifiers(psiAlternativeIdentifierA);
        psiInteractorA.setAliases(psiAliasA);
        psiInteractorA.setOrganism(psiOrganismA);
        psiInteractorA.setBiologicalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getBiologicalRolesA(), miBiologicalRoleOntologyTerms));
        psiInteractorA.setExperimentalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getExperimentalRolesA(), miExperimentalRoleOntologyTerms));
        psiInteractorA.setInteractorTypes(mapPsiIdsToCrossReferences(encoreInteraction.getInteractorTypesA(), miInteractorTypeOntologyTerms));
        psiInteractorA.setXrefs(encoreInteraction.getXrefsA());
        psiInteractorA.setAnnotations(encoreInteraction.getAnnotationsA());
        psiInteractorA.setChecksums(encoreInteraction.getChecksumsA());

        // TODO: add calls to setters for the missing MITAB 2.6, 2.7 and 2.8 interactors fields

        /* Create interactor B */
        Interactor psiInteractorB = new Interactor();
        psiInteractorB.setIdentifiers(psiIdentifierB);
        psiInteractorB.setAlternativeIdentifiers(psiAlternativeIdentifierB);
        psiInteractorB.setAliases(psiAliasB);
        psiInteractorB.setOrganism(psiOrganismB);
        psiInteractorB.setBiologicalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getBiologicalRolesB(), miBiologicalRoleOntologyTerms));
        psiInteractorB.setExperimentalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getExperimentalRolesB(), miExperimentalRoleOntologyTerms));
        psiInteractorB.setInteractorTypes(mapPsiIdsToCrossReferences(encoreInteraction.getInteractorTypesB(), miInteractorTypeOntologyTerms));
        psiInteractorB.setXrefs(encoreInteraction.getXrefsB());
        psiInteractorB.setAnnotations(encoreInteraction.getAnnotationsB());
        psiInteractorB.setChecksums(encoreInteraction.getChecksumsB());

        // TODO: add calls to setters for the missing MITAB 2.6, 2.7 and 2.8 interactors fields

        /* Create detection methods */
        List<CrossReference> psiDetectionMethods = mapPsiIdsToCrossReferences(encoreInteraction.getMethodToPubmed().keySet(), miMethodOntologyTerms);

        /* Create interaction types */
        List<CrossReference> psiInteractionTypes = mapPsiIdsToCrossReferences(encoreInteraction.getTypeToPubmed().keySet(), miInteractionTypeOntologyTerms);

        /* Create interaction accessions */
        List<CrossReference> interactionAcs = new ArrayList<CrossReference>();
        Map<String, List<String>> experimentToDatabase = encoreInteraction.getExperimentToDatabase();
        if (experimentToDatabase != null) {
            for (String expAc : experimentToDatabase.keySet()) {
                if (expAc == null || expAc.length() == 0) {
                    log.warn("Could not process experimentAc (" + (expAc == null ? "null" : "'" + expAc + "'") + ") of EncoreInteraction(id=" + encoreInteraction.getId() + ") as it is null or empty ");
                    continue; // skip this experimentAC
                }
                List<String> expDbs = experimentToDatabase.get(expAc);
                if (expDbs != null) {
                    for (String expDb : expDbs) {
                        CrossReference interactionAc = new CrossReferenceImpl(expDb, expAc);
                        interactionAcs.add(interactionAc);
                    }
                }
            }
        }

        /* Create Binary interaction */
        BinaryInteraction bI = new BinaryInteractionImpl(psiInteractorA, psiInteractorB);
        bI.setAuthors(encoreInteraction.getAuthors());
        bI.setInteractionAcs(interactionAcs);
        bI.setSourceDatabases(encoreInteraction.getSourceDatabases());
        bI.setConfidenceValues(encoreInteraction.getConfidenceValues());
        bI.setDetectionMethods(psiDetectionMethods);
        bI.setInteractionTypes(psiInteractionTypes);
        bI.setPublications(encoreInteraction.getPublicationIds());

        bI.setComplexExpansion(mapPsiIdsToCrossReferences(encoreInteraction.getExpansions(), miExpansionOntologyTerms));
        bI.setXrefs(encoreInteraction.getXrefsInteraction());
        bI.setAnnotations(encoreInteraction.getAnnotationsInteraction());
        bI.setParameters(encoreInteraction.getParameters());
        bI.setCreationDate(encoreInteraction.getCreationDates());
        bI.setUpdateDate(encoreInteraction.getUpdateDates());
        bI.setChecksums(encoreInteraction.getChecksumsInteraction());
        bI.setNegativeInteraction(encoreInteraction.getNegatives().contains(true));

        /* Create Host Organism */
        Organism psiHostOrganism = new OrganismImpl();
        psiHostOrganism.setIdentifiers(encoreInteraction.getHostOrganisms());

        bI.setHostOrganism(psiHostOrganism);

        // TODO: add calls to setters for the missing MITAB 2.6, 2.7 and 2.8 interaction fields

        return bI;
    }

    public BinaryInteraction getBinaryInteractionForScoring(EncoreInteraction encoreInteraction) {
        /* Create Identifiers and Alternative Identifiers */
        List<CrossReference> psiIdentifierA = new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierA = new ArrayList<CrossReference>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String, String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<CrossReference> psiIdentifierB = new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierB = new ArrayList<CrossReference>();
        String encoreInteractorB = encoreInteraction.getInteractorB(mappingIdDbNames);
        Map<String, String> encoreInteractorAccsB = encoreInteraction.getInteractorAccsB();
        updateIdentifiers(psiIdentifierB, psiAlternativeIdentifierB, encoreInteractorAccsB, encoreInteractorB);

        /* Create Alias */
        List<Alias> psiAliasA = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorA = encoreInteraction.getOtherInteractorAccsA();
        updateAlias(psiAliasA, encoreOtherInteractorA);
        List<Alias> psiAliasB = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorB = encoreInteraction.getOtherInteractorAccsB();
        updateAlias(psiAliasB, encoreOtherInteractorB);

        /* Create Organism */
        Organism psiOrganismA = new OrganismImpl();
        psiOrganismA.setIdentifiers(encoreInteraction.getOrganismsA());
        Organism psiOrganismB = new OrganismImpl();
        psiOrganismB.setIdentifiers(encoreInteraction.getOrganismsB());

        /* Create interactor A */
        Interactor psiInteractorA = new Interactor();
        psiInteractorA.setIdentifiers(psiIdentifierA);
        psiInteractorA.setAlternativeIdentifiers(psiAlternativeIdentifierA);
        psiInteractorA.setAliases(psiAliasA);
        psiInteractorA.setOrganism(psiOrganismA);
        psiInteractorA.setBiologicalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getBiologicalRolesA(), miBiologicalRoleOntologyTerms));
        psiInteractorA.setExperimentalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getExperimentalRolesA(), miExperimentalRoleOntologyTerms));
        psiInteractorA.setInteractorTypes(mapPsiIdsToCrossReferences(encoreInteraction.getInteractorTypesA(), miInteractorTypeOntologyTerms));
        psiInteractorA.setXrefs(encoreInteraction.getXrefsA());
        psiInteractorA.setAnnotations(encoreInteraction.getAnnotationsA());
        psiInteractorA.setChecksums(encoreInteraction.getChecksumsA());

        /* Create interactor B */
        Interactor psiInteractorB = new Interactor();
        psiInteractorB.setIdentifiers(psiIdentifierB);
        psiInteractorB.setAlternativeIdentifiers(psiAlternativeIdentifierB);
        psiInteractorB.setAliases(psiAliasB);
        psiInteractorB.setOrganism(psiOrganismB);
        psiInteractorB.setBiologicalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getBiologicalRolesB(), miBiologicalRoleOntologyTerms));
        psiInteractorB.setExperimentalRoles(mapPsiIdsToCrossReferences(encoreInteraction.getExperimentalRolesB(), miExperimentalRoleOntologyTerms));
        psiInteractorB.setInteractorTypes(mapPsiIdsToCrossReferences(encoreInteraction.getInteractorTypesB(), miInteractorTypeOntologyTerms));
        psiInteractorB.setXrefs(encoreInteraction.getXrefsB());
        psiInteractorB.setAnnotations(encoreInteraction.getAnnotationsB());
        psiInteractorB.setChecksums(encoreInteraction.getChecksumsB());

        /* Create detection methods */
        List<CrossReference> psiDetectionMethods = new ArrayList<CrossReference>();
        /* Create interaction types */
        List<CrossReference> psiInteractionTypes = new ArrayList<CrossReference>();
        for (MethodTypePair pair : encoreInteraction.getMethodTypePairListMap().keySet()) {
            Collection<String> pubmedIds = encoreInteraction.getMethodTypePairListMap().get(pair);

            for (String pubmed : pubmedIds) {
                if (pair.getMethod() != null)
                    psiDetectionMethods.add(mapPsiIdsToCrossReferences(pair.getMethod(), miMethodOntologyTerms));
                if (pair.getType() != null)
                    psiInteractionTypes.add(mapPsiIdsToCrossReferences(pair.getType(), miInteractionTypeOntologyTerms));
            }
        }

        /* Create interaction accessions */
        List<CrossReference> interactionAcs = new ArrayList<CrossReference>();
        Map<String, List<String>> experimentToDatabase = encoreInteraction.getExperimentToDatabase();
        if (experimentToDatabase != null) {
            for (String expAc : experimentToDatabase.keySet()) {
                if (expAc == null || expAc.length() == 0) {
                    log.warn("Could not process experimentAc (" + (expAc == null ? "null" : "'" + expAc + "'") + ") of EncoreInteraction(id=" + encoreInteraction.getId() + ") as it is null or empty ");
                    continue; // skip this experimentAC
                }
                List<String> expDbs = experimentToDatabase.get(expAc);
                if (expDbs != null) {
                    for (String expDb : expDbs) {
                        CrossReference interactionAc = new CrossReferenceImpl(expDb, expAc);
                        interactionAcs.add(interactionAc);
                    }
                }
            }
        }

        /* Create Binary interaction */
        BinaryInteraction bI = new BinaryInteractionImpl(psiInteractorA, psiInteractorB);
        bI.setAuthors(encoreInteraction.getAuthors());
        bI.setInteractionAcs(interactionAcs);
        bI.setSourceDatabases(encoreInteraction.getSourceDatabases());
        bI.setConfidenceValues(encoreInteraction.getConfidenceValues());
        bI.setDetectionMethods(psiDetectionMethods);
        bI.setInteractionTypes(psiInteractionTypes);
        bI.setPublications(encoreInteraction.getPublicationIds());
        bI.setComplexExpansion(mapPsiIdsToCrossReferences(encoreInteraction.getExpansions(), miExpansionOntologyTerms));
        bI.setXrefs(encoreInteraction.getXrefsInteraction());
        bI.setAnnotations(encoreInteraction.getAnnotationsInteraction());
        bI.setParameters(encoreInteraction.getParameters());
        bI.setCreationDate(encoreInteraction.getCreationDates());
        bI.setUpdateDate(encoreInteraction.getUpdateDates());
        bI.setChecksums(encoreInteraction.getChecksumsInteraction());
        bI.setNegativeInteraction(encoreInteraction.getNegatives().contains(true));

        /* Create Host Organism */
        Organism psiHostOrganism = new OrganismImpl();
        psiHostOrganism.setIdentifiers(encoreInteraction.getHostOrganisms());

        bI.setHostOrganism(psiHostOrganism);

        return bI;
    }

    private void updateIdentifiers(Collection<CrossReference> psiIdentifier, Collection<CrossReference> psiAlternativeIdentifier, Map<String, String> encoreInteractors, String encoreInteractor) {
        for (String db : encoreInteractors.keySet()) {
            String acc = encoreInteractors.get(db);
            if (acc == encoreInteractor) {
                CrossReference pI = new CrossReferenceImpl(db, acc);
                psiIdentifier.add(pI);
            } else {
                CrossReference pI = new CrossReferenceImpl(db, acc);
                psiAlternativeIdentifier.add(pI);
            }
        }
    }

    private void updateAlias(Collection<Alias> psiAlias, Map<String, List<String>> encoreOtherInteractor) {
        for (String db : encoreOtherInteractor.keySet()) {
            List<String> accs = encoreOtherInteractor.get(db);
            for (String acc : accs) {
                Alias pA = new AliasImpl(db, acc);
                psiAlias.add(pA);
            }
        }
    }

    private List<CrossReference> mapPsiIdsToCrossReferences(Collection<String> ids, Map<String, String> ontologyTerms) {
        List<CrossReference> references = new ArrayList<>();
        if (ids == null) return references;
        for (String id : ids) {
            CrossReference reference = new CrossReferenceImpl();
            reference.setIdentifier(id);
            if (ontologyTerms.containsKey(id)) {
                reference.setText(ontologyTerms.get(id));
                reference.setDatabase("psi-mi");
            }
            references.add(reference);
        }
        return references;
    }

    private CrossReference mapPsiIdsToCrossReferences(String id, Map<String, String> ontologyTerms) {
        if (id == null) return null;
        CrossReference reference = new CrossReferenceImpl();
        reference.setIdentifier(id);
        if (ontologyTerms.containsKey(id)) {
            reference.setText(ontologyTerms.get(id));
            reference.setDatabase("psi-mi");
        }
        return reference;
    }


}
