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

    private static final Log log = LogFactory.getLog( Encore2Binary.class );

    private Map<String,String> miMethodOntologyTerms;
    private Map<String,String> miTypeOntologyTerms;
    protected String mappingIdDbNames;

    public Encore2Binary(String mappingIdDbNames) {
        MIOntology olsHelper = new MIOntology();
        this.miMethodOntologyTerms = olsHelper.getJsonChildren("MI:0001");
        this.miTypeOntologyTerms = olsHelper.getJsonChildren("MI:0190");
        this.mappingIdDbNames = mappingIdDbNames;
    }

    public Encore2Binary() {
        MIOntology olsHelper = new MIOntology();
        this.miMethodOntologyTerms = olsHelper.getJsonChildren("MI:0001");
        this.miTypeOntologyTerms = olsHelper.getJsonChildren("MI:0190");
        this.mappingIdDbNames = null;
    }

    @Deprecated
    public BinaryInteraction getBinaryInteraction(EncoreBinaryInteraction encoreInteraction){
        /* Create Identifiers and Alternative Identifiers */
        List<CrossReference> psiIdentifierA= new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierA= new ArrayList<CrossReference>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<CrossReference> psiIdentifierB= new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierB= new ArrayList<CrossReference>();
        String encoreInteractorB = encoreInteraction.getInteractorB(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsB = encoreInteraction.getInteractorAccsB();
        updateIdentifiers(psiIdentifierB, psiAlternativeIdentifierB, encoreInteractorAccsB, encoreInteractorB);

        /* Create Alias */
        List<Alias> psiAliasA = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorA = encoreInteraction.getOtherInteractorAccsA();
        updateAlias(psiAliasA,encoreOtherInteractorA);
        List<Alias> psiAliasB = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorB = encoreInteraction.getOtherInteractorAccsB();
        updateAlias(psiAliasB,encoreOtherInteractorB);

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

        /* Create interactor B */
        Interactor psiInteractorB = new Interactor();
        psiInteractorB.setIdentifiers(psiIdentifierB);
        psiInteractorB.setAlternativeIdentifiers(psiAlternativeIdentifierB);
        psiInteractorB.setAliases(psiAliasB);
        psiInteractorB.setOrganism(psiOrganismB);

        /* Create detection methods */
        List<CrossReference> psiDetectionMethods = new ArrayList<CrossReference>();
        for(String methodId:encoreInteraction.getMethodToPubmed().keySet()){
            CrossReference psiDetectionMethod = new CrossReferenceImpl();
            psiDetectionMethod.setIdentifier(methodId);
            if(miMethodOntologyTerms.containsKey(methodId)){
                psiDetectionMethod.setText(miMethodOntologyTerms.get(methodId));
                psiDetectionMethod.setDatabase("psi-mi");
            }
            psiDetectionMethods.add(psiDetectionMethod);
        }

        /* Create interaction types */
        List<CrossReference> psiInteractionTypes = new ArrayList<CrossReference>();
        for(String typeId:encoreInteraction.getTypeToPubmed().keySet()){
            CrossReference psiInteractionType = new CrossReferenceImpl();
            psiInteractionType.setIdentifier(typeId);
            if(miTypeOntologyTerms.containsKey(typeId)){
                psiInteractionType.setText(miTypeOntologyTerms.get(typeId));
                psiInteractionType.setDatabase("psi-mi");
            }
            psiInteractionTypes.add(psiInteractionType);
        }

        /* Create interaction accessions */
        List<CrossReference> interactionAcs = new ArrayList<CrossReference>();
        Map<String, List<String>> experimentToDatabase = encoreInteraction.getExperimentToDatabase();
        if(experimentToDatabase != null){
            for(String expAc:experimentToDatabase.keySet()){
                if(expAc == null || expAc.length()==0){
                    log.warn( "Could not process experimentAc ("+(expAc == null ? "null" : "'"+expAc+"'")+") of EncoreInteraction(id="+ encoreInteraction.getId()+") as it is null or empty " );
                    continue; // skip this experimentAC
                }
                List<String> expDbs = experimentToDatabase.get(expAc);
                if(expDbs != null){
                    for(String expDb:expDbs){
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
        return bI;
    }

    public BinaryInteraction getBinaryInteractionForScoring(EncoreInteraction encoreInteraction){
        /* Create Identifiers and Alternative Identifiers */
        List<CrossReference> psiIdentifierA= new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierA= new ArrayList<CrossReference>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<CrossReference> psiIdentifierB= new ArrayList<CrossReference>();
        List<CrossReference> psiAlternativeIdentifierB= new ArrayList<CrossReference>();
        String encoreInteractorB = encoreInteraction.getInteractorB(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsB = encoreInteraction.getInteractorAccsB();
        updateIdentifiers(psiIdentifierB, psiAlternativeIdentifierB, encoreInteractorAccsB, encoreInteractorB);

        /* Create Alias */
        List<Alias> psiAliasA = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorA = encoreInteraction.getOtherInteractorAccsA();
        updateAlias(psiAliasA,encoreOtherInteractorA);
        List<Alias> psiAliasB = new ArrayList<Alias>();
        Map<String, List<String>> encoreOtherInteractorB = encoreInteraction.getOtherInteractorAccsB();
        updateAlias(psiAliasB,encoreOtherInteractorB);

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

        /* Create interactor B */
        Interactor psiInteractorB = new Interactor();
        psiInteractorB.setIdentifiers(psiIdentifierB);
        psiInteractorB.setAlternativeIdentifiers(psiAlternativeIdentifierB);
        psiInteractorB.setAliases(psiAliasB);
        psiInteractorB.setOrganism(psiOrganismB);

        /* Create detection methods */
        List<CrossReference> psiDetectionMethods = new ArrayList<CrossReference>();
        /* Create interaction types */
        List<CrossReference> psiInteractionTypes = new ArrayList<CrossReference>();
        for(MethodTypePair pair : encoreInteraction.getMethodTypePairListMap().keySet()){
            Collection<String> pubmedIds = encoreInteraction.getMethodTypePairListMap().get(pair);

            for (String pubmed : pubmedIds){
                if (pair.getMethod() != null){

                    CrossReference psiDetectionMethod = new CrossReferenceImpl();
                    psiDetectionMethod.setIdentifier(pair.getMethod());
                    if(miMethodOntologyTerms.containsKey(pair.getMethod())){
                        psiDetectionMethod.setText(miMethodOntologyTerms.get(pair.getMethod()));
                        psiDetectionMethod.setDatabase("psi-mi");
                    }


                    psiDetectionMethods.add(psiDetectionMethod);
                }

                if (pair.getType() != null){

                    CrossReference psitype = new CrossReferenceImpl();
                    psitype.setIdentifier(pair.getMethod());
                    if(miTypeOntologyTerms.containsKey(pair.getType())){
                        psitype.setText(miTypeOntologyTerms.get(pair.getType()));
                        psitype.setDatabase("psi-mi");
                    }
                    psiInteractionTypes.add(psitype);
                }
            }
        }

        /* Create interaction accessions */
        List<CrossReference> interactionAcs = new ArrayList<CrossReference>();
        Map<String, List<String>> experimentToDatabase = encoreInteraction.getExperimentToDatabase();
        if(experimentToDatabase != null){
            for(String expAc:experimentToDatabase.keySet()){
                if(expAc == null || expAc.length()==0){
                    log.warn( "Could not process experimentAc ("+(expAc == null ? "null" : "'"+expAc+"'")+") of EncoreInteraction(id="+ encoreInteraction.getId()+") as it is null or empty " );
                    continue; // skip this experimentAC
                }
                List<String> expDbs = experimentToDatabase.get(expAc);
                if(expDbs != null){
                    for(String expDb:expDbs){
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
        return bI;
    }

    private void updateIdentifiers(Collection<CrossReference> psiIdentifier, Collection<CrossReference> psiAlternativeIdentifier, Map<String, String> encoreInteractors, String encoreInteractor) {
        for(String db:encoreInteractors.keySet()){
            String acc = encoreInteractors.get(db);
            if(acc == encoreInteractor){
                CrossReference pI = new CrossReferenceImpl(db,acc);
                psiIdentifier.add(pI);
            } else {
                CrossReference pI = new CrossReferenceImpl(db,acc);
                psiAlternativeIdentifier.add(pI);
            }
        }
    }

    private void updateAlias(Collection<Alias> psiAlias, Map<String, List<String>> encoreOtherInteractor) {
        for(String db:encoreOtherInteractor.keySet()){
            List<String> accs = encoreOtherInteractor.get(db);
            for(String acc:accs){
                Alias pA = new AliasImpl(db,acc);
                psiAlias.add(pA);
            }
        }
    }
}
