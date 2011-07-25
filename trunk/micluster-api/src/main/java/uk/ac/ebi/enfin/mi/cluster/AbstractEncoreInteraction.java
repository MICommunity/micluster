package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import psidev.psi.mi.tab.model.Author;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.CrossReference;

import java.util.*;

/**
 * Abstract class for Encore interaction
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/04/11</pre>
 */

public abstract class AbstractEncoreInteraction implements EncoreBinaryInteraction{

    static Logger logger = Logger.getLogger(AbstractEncoreInteraction.class);
    protected int id;
    protected Map<String, String> interactorAccsA = new HashMap<String, String>(); //databaseName:acc
    protected Map<String, String> interactorAccsB = new HashMap<String, String>();; //databaseName:acc
    protected List<CrossReference> publicationIds = new ArrayList<CrossReference>(); //databaseName:acc
    protected Map<String, String> experimentToPubmed = new HashMap<String, String>();
    protected Map<String, List<String>> experimentToDatabase = new HashMap<String, List<String>>();
    protected List<Confidence> confidenceValues = new ArrayList<Confidence>();
    protected List<Author> authors = new ArrayList<Author>();
    protected List<CrossReference> sourceDatabases = new ArrayList<CrossReference>();
    protected Collection<CrossReference> organismsA = new ArrayList<CrossReference>();
    protected Collection<CrossReference> organismsB = new ArrayList<CrossReference>();
    protected Map<String, List<String>> otherInteractorAccsA = new HashMap<String, List<String>>();; //databaseName:acc
    protected Map<String, List<String>> otherInteractorAccsB = new HashMap<String, List<String>>();; //databaseName:acc
    protected String mappingIdDbNames = "uniprotkb,irefindex,ddbj/embl/genbank,refseq,chebi";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, String> getInteractorAccsA() {
        return interactorAccsA;
    }

    public void setInteractorAccsA(Map<String, String> interactorAccsA) {
        this.interactorAccsA = interactorAccsA;
    }

    public void addInteractorAccsA(Map<String, String> interactorAccsA) {
        for(String dbName:interactorAccsA.keySet()){
            String acc = interactorAccsA.get(dbName);
            this.addInteractorAccsA(dbName, acc);
        }
    }

    public void addInteractorAccsA(String dbName, String acc) {
        if(!this.interactorAccsA.containsKey(dbName)){
            this.interactorAccsA.put(dbName, acc);
        }
    }



    public Map<String, List<String>> getOtherInteractorAccsA() {
        return otherInteractorAccsA;
    }

    public void setOtherInteractorAccsA(Map<String, List<String>> otherInteractorAccsA) {
        this.otherInteractorAccsA = otherInteractorAccsA;
    }

    public void addOtherInteractorAccsA(Map<String, List<String>> otherInteractorAccsA) {
        this.otherInteractorAccsA = setMapping(this.otherInteractorAccsA, otherInteractorAccsA);
    }

    public void addOtherInteractorAccsA(String dbName, String acc) {
        this.otherInteractorAccsA = setMapping(this.otherInteractorAccsA, dbName, acc);
    }




    public Map<String, String> getInteractorAccsB() {
        return interactorAccsB;
    }

    public void setInteractorAccsB(Map<String, String> interactorAccsB) {
        this.interactorAccsB = interactorAccsB;
    }

    public void addInteractorAccsB(Map<String, String> interactorAccsB) {
        for(String dbName:interactorAccsB.keySet()){
            String acc = interactorAccsB.get(dbName);
            this.addInteractorAccsB(dbName, acc);
        }
    }

    public void addInteractorAccsB(String dbName, String acc) {
        if(!this.interactorAccsB.containsKey(dbName)){
            this.interactorAccsB.put(dbName, acc);
        }
    }


    public Map<String, List<String>> getOtherInteractorAccsB() {
        return otherInteractorAccsB;
    }

    public void setOtherInteractorAccsB(Map<String, List<String>> otherInteractorAccsB) {
        this.otherInteractorAccsB = otherInteractorAccsB;
    }

    public void addOtherInteractorAccsB(Map<String, List<String>> otherInteractorAccsB) {
        this.otherInteractorAccsB = setMapping(this.otherInteractorAccsB, otherInteractorAccsB);
    }

    public void addOtherInteractorAccsB(String dbName, String acc) {
        this.otherInteractorAccsB = setMapping(this.otherInteractorAccsB, dbName, acc);
    }




    public String getInteractorA() {
        return getInteractorA(mappingIdDbNames);
    }

    public String getInteractorA(String mappingIdDbNames) {
        if(mappingIdDbNames == null){
            mappingIdDbNames = this.mappingIdDbNames;
        }
        String acc = getInteractorFromAccs(interactorAccsA, mappingIdDbNames.split(","));
        return acc;
    }

    public String getInteractorB() {
        return getInteractorB(mappingIdDbNames);
    }

    public String getInteractorB(String mappingIdDbNames) {
        if(mappingIdDbNames == null){
            mappingIdDbNames = this.mappingIdDbNames;
        }
        String acc = getInteractorFromAccs(interactorAccsB, mappingIdDbNames.split(","));
        return acc;
    }

    public List<CrossReference> getPublicationIds() {
        return publicationIds;
    }

    public void setPublicationIds(List<CrossReference> publicationIds) {
        this.publicationIds = publicationIds;
    }

    public void addPublicationId(CrossReference publicationId) {
        if(!this.publicationIds.contains(publicationId)){
            this.publicationIds.add(publicationId);
        }
    }

    public void addPublicationId(List<CrossReference> publicationIds) {
        for(CrossReference publicationId:publicationIds){
            this.addPublicationId(publicationId);
        }
    }

    public Map<String, String> getExperimentToPubmed() {
        return experimentToPubmed;
    }

    public void setExperimentToPubmed(Map<String, String> experimentToPubmed) {
        this.experimentToPubmed = experimentToPubmed;
    }

    public void addExperimentToPubmed(Map<String, String> experimentToPubmed) {
        for(String experiment:experimentToPubmed.keySet()){
            String pubmed = experimentToPubmed.get(experiment);
            this.addExperimentToPubmed(experiment, pubmed);
        }
    }

    public void addExperimentToPubmed(String experiment, String pubmed) {
        if(!this.experimentToPubmed.containsKey(experiment)){
            this.experimentToPubmed.put(experiment, pubmed);
        }
    }


    public Map<String, List<String>> getExperimentToDatabase() {
        return experimentToDatabase;
    }

    public void setExperimentToDatabase(Map<String, List<String>> experimentToDatabase) {
        this.experimentToDatabase = experimentToDatabase;
    }

    public void addExperimentToDatabase(Map<String, List<String>> experimentToDatabase) {
        this.experimentToDatabase = setMapping(this.experimentToDatabase, experimentToDatabase);
    }

    public void addExperimentToDatabase(String experiment, String database) {
        this.experimentToDatabase = setMapping(this.experimentToDatabase, experiment, database);
    }

    public List<Confidence> getConfidenceValues() {
        return confidenceValues;
    }

    public void setConfidenceValues(List<Confidence> confidenceValues) {
        this.confidenceValues = confidenceValues;
    }

    public void addConfidenceValues(List<Confidence> confidenceValues) {
        for(Confidence confidenceValue:confidenceValues){
            addConfidenceValue(confidenceValue);
        }
    }

    public void addConfidenceValue(Confidence confidenceValue) {
        if(!this.confidenceValues.contains(confidenceValue)){
            this.confidenceValues.add(confidenceValue);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void addAuthors(List<Author> authors) {
        for(Author author:authors){
            addAuthor(author);
        }
    }

    public void addAuthor(Author author) {
        if(!this.authors.contains(author)){
            this.authors.add(author);
        }
    }

    public List<CrossReference> getSourceDatabases() {
        return sourceDatabases;
    }

    public void setSourceDatabases(List<CrossReference> sourceDatabases) {
        this.sourceDatabases = sourceDatabases;
    }

    public void addSourceDatabases(List<CrossReference> sourceDatabases) {
        for(CrossReference sourceDatabase:sourceDatabases){
            this.addSourceDatabase(sourceDatabase);
        }
    }

    public void addSourceDatabase(CrossReference sourceDatabase) {
        if(!this.sourceDatabases.contains(sourceDatabase)){
            this.sourceDatabases.add(sourceDatabase);
        }
    }

    public Collection<CrossReference> getOrganismsA() {
        return organismsA;
    }

    public void setOrganismsA(Collection<CrossReference> organismsA) {
        this.organismsA = organismsA;
    }

    public void addOrganismsA(Collection<CrossReference> organismsA) {
        for(CrossReference organismA:organismsA){
            addOrganismA(organismA);
        }
    }

    public void addOrganismA(CrossReference organismA) {
        if(!this.organismsA.contains(organismA)){
            this.organismsA.add(organismA);
        }
    }

    public Collection<CrossReference> getOrganismsB() {
        return organismsB;
    }

    public void setOrganismsB(Collection<CrossReference> organismsB) {
        this.organismsB = organismsB;
    }

    public void addOrganismsB(Collection<CrossReference> organismsB) {
        for(CrossReference organismB:organismsB){
            addOrganismB(organismB);
        }
    }

    public void addOrganismB(CrossReference organismB) {
        if(!this.organismsB.contains(organismB)){
            this.organismsB.add(organismB);
        }
    }

    public String getMappingIdDbNames() {
        return mappingIdDbNames;
    }

    public void setMappingIdDbNames(String mappingIdDbNames) {
        this.mappingIdDbNames = mappingIdDbNames;
    }

    /**
     * Check the original list and adds new keys and values if not present
     * @param originalList
     * @param query
     * @return
     */
    protected Map<String, List<String>> setMapping(Map<String, List<String>> originalList, Map<String, List<String>> query) {
        for(String key:query.keySet()){
            List<String> values = query.get(key);
            for(String value:values){
                originalList = setMapping(originalList, key, value);
            }
        }
        return originalList;
    }

    /**
     * Check the original list and adds new keys and values if not present
     * in the original list.
     * @param originalList
     * @param key
     * @param value
     * @return
     */
    protected Map<String, List<String>> setMapping(Map<String, List<String>> originalList, String key, String value) {
        if(value != null && key != null){
            Map<String, List<String>> originalListTemp = new HashMap<String, List<String>>();
            List<String> pubmeds = new ArrayList<String>();
            if(originalList.containsKey(key)){
                pubmeds = originalList.get(key);
                if(!originalList.get(key).contains(value)){
                    pubmeds.add(value);
                    originalList.put(key, pubmeds);
                }
            } else {
                pubmeds.add(value);
                originalListTemp.put(key, pubmeds);
            }
            originalList.putAll(originalListTemp);
        } else {
            logger.warn("The value or key is null in setMapping");
        }
        return originalList;
    }


    /**
     * Select interactors using a list of priority database Names
     * @param interactorAccs
     * @param idDbNameList
     * @return
     */
    protected String getInteractorFromAccs(Map<String,String> interactorAccs, String[] idDbNameList){
        String interactorAcc = null;
        for(String idDbName:idDbNameList){
            if(interactorAccs.containsKey(idDbName)){
                interactorAcc =  interactorAccs.get(idDbName);
                break;
            }
        }
        /* If interactor is not in the list take the first one */
        if(interactorAcc == null){
            interactorAcc = interactorAccs.values().iterator().next();
        }
        return interactorAcc;
    }

}
