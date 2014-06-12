package uk.ac.ebi.enfin.mi.cluster.model;

import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.tab.model.Author;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.CrossReference;

import java.io.Serializable;
import java.util.*;

/**
 * Interface for binary interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/04/11</pre>
 */

public interface EncoreBinaryInteraction extends Serializable{

    public int getId();

    public void setId(int id);

    public Map<String, String> getInteractorAccsA();

    public void setInteractorAccsA(Map<String, String> interactorAccsA);

    public void addInteractorAccsA(Map<String, String> interactorAccsA);

    public void addInteractorAccsA(String dbName, String acc);

    public Map<String, List<String>> getOtherInteractorAccsA();

    public void setOtherInteractorAccsA(Map<String, List<String>> otherInteractorAccsA);

    public void addOtherInteractorAccsA(Map<String, List<String>> otherInteractorAccsA);

    public void addOtherInteractorAccsA(String dbName, String acc);

    public Map<String, String> getInteractorAccsB();

    public void setInteractorAccsB(Map<String, String> interactorAccsB);

    public void addInteractorAccsB(Map<String, String> interactorAccsB);

    public void addInteractorAccsB(String dbName, String acc);


    public Map<String, List<String>> getOtherInteractorAccsB();

    public void setOtherInteractorAccsB(Map<String, List<String>> otherInteractorAccsB);

    public void addOtherInteractorAccsB(Map<String, List<String>> otherInteractorAccsB);

    public void addOtherInteractorAccsB(String dbName, String acc);

    public String getInteractorA();

    public String getInteractorA(String mappingIdDbNames);

    public String getInteractorB();

    public String getInteractorB(String mappingIdDbNames);

    public List<Xref> getPublicationIds();

    public void setPublicationIds(List<Xref> publicationIds);

    public void addPublicationId(Xref publicationId);

    public void addPublicationId(List<Xref> publicationIds);


    public Map<String, List<String>> getMethodToPubmed();

    public Map<String, List<String>> getTypeToPubmed();


    public Map<String, String> getExperimentToPubmed();

    public void setExperimentToPubmed(Map<String, String> experimentToPubmed);

    public void addExperimentToPubmed(Map<String, String> experimentToPubmed);

    public void addExperimentToPubmed(String experiment, String pubmed);


    public Map<String, List<String>> getExperimentToDatabase();

    public void setExperimentToDatabase(Map<String, List<String>> experimentToDatabase);

    public void addExperimentToDatabase(Map<String, List<String>> experimentToDatabase);

    public void addExperimentToDatabase(String experiment, String database);

    public List<Confidence> getConfidenceValues();

    public void setConfidenceValues(List<Confidence> confidenceValues);

    public void addConfidenceValues(List<Confidence> confidenceValues);

    public void addConfidenceValue(Confidence confidenceValue);

    public List<Author> getAuthors();

    public void setAuthors(List<Author> authors) ;

    public void addAuthors(List<Author> authors);

    public void addAuthor(Author author);

    public List<CrossReference> getSourceDatabases();

    public void setSourceDatabases(List<Xref> sourceDatabases);

    public void addSourceDatabases(List<Xref> sourceDatabases);

    public void addSourceDatabase(CrossReference sourceDatabase);

    public List<CrossReference> getOrganismsA();

    public void setOrganismsA(List<Xref> organismsA);

    public void addOrganismsA(List<Xref> organismsA);

    public void addOrganismA(Xref organismA);

    public List<CrossReference> getOrganismsB();

    public void setOrganismsB(List<Xref> organismsB);

    public void addOrganismsB(List<Xref> organismsB);

    public void addOrganismB(Xref organismB);

    public String getMappingIdDbNames();

    public void setMappingIdDbNames(String mappingIdDbNames);

}