package uk.ac.ebi.enfin.mi.cluster.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultAlias;
import psidev.psi.mi.jami.model.impl.DefaultInteractor;
import psidev.psi.mi.jami.model.impl.DefaultOrganism;
import psidev.psi.mi.jami.model.impl.DefaultXref;
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
    public BinaryInteractionCluster getBinaryInteraction(EncoreBinaryInteraction encoreInteraction){
        /* Create Identifiers and Alternative Identifiers */
        List<Xref> psiIdentifierA= new ArrayList<Xref>();
        List<Xref> psiAlternativeIdentifierA= new ArrayList<Xref>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<Xref> psiIdentifierB= new ArrayList<Xref>();
        List<Xref> psiAlternativeIdentifierB= new ArrayList<Xref>();
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
        Organism psiOrganismA = new DefaultOrganism(encoreInteraction.getId());
        psiOrganismA.set(encoreInteraction.getOrganismsA());
        Organism psiOrganismB = new DefaultOrganism();
        psiOrganismB.setIdentifiers(encoreInteraction.getOrganismsB());

        /* Create interactor A */
        Interactor psiInteractorA = new DefaultInteractor(psiIdentifierA);
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
        List<Xref> psiDetectionMethods = new ArrayList<Xref>();
        for(String methodId:encoreInteraction.getMethodToPubmed().keySet()){
            Xref psiDetectionMethod = new DefaultXref();
            psiDetectionMethod.setIdentifier(methodId);
            if(miMethodOntologyTerms.containsKey(methodId)){
                psiDetectionMethod.setText(miMethodOntologyTerms.get(methodId));
                psiDetectionMethod.setDatabase("psi-mi");
            }
            psiDetectionMethods.add(psiDetectionMethod);
        }

        /* Create interaction types */
        List<Xref> psiInteractionTypes = new ArrayList<Xref>();
        for(String typeId:encoreInteraction.getTypeToPubmed().keySet()){
            Xref psiInteractionType = new DefaultXref();
            psiInteractionType.setIdentifier(typeId);
            if(miTypeOntologyTerms.containsKey(typeId)){
                psiInteractionType.setText(miTypeOntologyTerms.get(typeId));
                psiInteractionType.setDatabase("psi-mi");
            }
            psiInteractionTypes.add(psiInteractionType);
        }

        /* Create interaction accessions */
        List<Xref> interactionAcs = new ArrayList<Xref>();
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
                        Xref interactionAc = new DefaultXref(expDb, expAc);
                        interactionAcs.add(interactionAc);
                    }
                }
            }
        }

        /* Create Binary interaction */
        BinaryInteractionCluster bI = new BinaryInteractionCluster(psiInteractorA, psiInteractorB);
        bI.setAuthors(encoreInteraction.getAuthors());
        bI.setInteractionAcs(interactionAcs);
        bI.setSourceDatabases(encoreInteraction.getSourceDatabases());
        bI.setConfidenceValues(encoreInteraction.getConfidenceValues());
        bI.setDetectionMethods(psiDetectionMethods);
        bI.setInteractionTypes(psiInteractionTypes);
        bI.setPublications(encoreInteraction.getPublicationIds());
        return bI;
    }

    public BinaryInteractionCluster getBinaryInteractionForScoring(EncoreInteraction encoreInteraction){
        /* Create Identifiers and Alternative Identifiers */
        List<Xref> psiIdentifierA= new ArrayList<Xref>();
        List<Xref> psiAlternativeIdentifierA= new ArrayList<Xref>();
        String encoreInteractorA = encoreInteraction.getInteractorA(mappingIdDbNames);
        Map<String,String> encoreInteractorAccsA = encoreInteraction.getInteractorAccsA();
        updateIdentifiers(psiIdentifierA, psiAlternativeIdentifierA, encoreInteractorAccsA, encoreInteractorA);
        List<Xref> psiIdentifierB= new ArrayList<Xref>();
        List<Xref> psiAlternativeIdentifierB= new ArrayList<Xref>();
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
        Organism psiOrganismA = new DefaultOrganism();
        psiOrganismA.setIdentifiers(encoreInteraction.getOrganismsA());
        Organism psiOrganismB = new DefaultOrganism();
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
        List<Xref> psiDetectionMethods = new ArrayList<Xref>();
        /* Create interaction types */
        List<Xref> psiInteractionTypes = new ArrayList<Xref>();
        for(MethodTypePair pair : encoreInteraction.getMethodTypePairListMap().keySet()){
            Collection<String> pubmedIds = encoreInteraction.getMethodTypePairListMap().get(pair);

            for (String pubmed : pubmedIds){
                if (pair.getMethod() != null){

                    Xref psiDetectionMethod = new DefaultXref();
                    psiDetectionMethod.setIdentifier(pair.getMethod());
                    if(miMethodOntologyTerms.containsKey(pair.getMethod())){
                        psiDetectionMethod.setText(miMethodOntologyTerms.get(pair.getMethod()));
                        psiDetectionMethod.setDatabase("psi-mi");
                    }


                    psiDetectionMethods.add(psiDetectionMethod);
                }

                if (pair.getType() != null){

                    Xref psitype = new DefaultXref();
                    psitype.setIdentifier(pair.getType());
                    if(miTypeOntologyTerms.containsKey(pair.getType())){
                        psitype.setText(miTypeOntologyTerms.get(pair.getType()));
                        psitype.setDatabase("psi-mi");
                    }
                    psiInteractionTypes.add(psitype);
                }
            }
        }

        /* Create interaction accessions */
        List<Xref> interactionAcs = new ArrayList<Xref>();
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
                        Xref interactionAc = new DefaultXref(expDb, expAc);
                        interactionAcs.add(interactionAc);
                    }
                }
            }
        }

        /* Create Binary interaction */
        BinaryInteractionCluster bI = new BinaryInteractionCluster(psiInteractorA, psiInteractorB);
        bI.setAuthors(encoreInteraction.getAuthors());
        bI.setInteractionAcs(interactionAcs);
        bI.setSourceDatabases(encoreInteraction.getSourceDatabases());
        bI.setConfidenceValues(encoreInteraction.getConfidenceValues());
        bI.setDetectionMethods(psiDetectionMethods);
        bI.setInteractionTypes(psiInteractionTypes);
        bI.setPublications(encoreInteraction.getPublicationIds());
        return bI;
    }

    private void updateIdentifiers(Collection<Xref> psiIdentifier, Collection<Xref> psiAlternativeIdentifier, Map<String, String> encoreInteractors, String encoreInteractor) {
        for(String db:encoreInteractors.keySet()){
            String acc = encoreInteractors.get(db);
            if(acc == encoreInteractor){
                Xref pI = new DefaultXref(db,acc);
                psiIdentifier.add(pI);
            } else {
                Xref pI = new DefaultXref(db,acc);
                psiAlternativeIdentifier.add(pI);
            }
        }
    }

    private void updateAlias(Collection<Alias> psiAlias, Map<String, List<String>> encoreOtherInteractor) {
        for(String db:encoreOtherInteractor.keySet()){
            List<String> accs = encoreOtherInteractor.get(db);
            for(String acc:accs){
                Alias pA = new DefaultAlias(db,acc);
                psiAlias.add(pA);
            }
        }
    }
}
