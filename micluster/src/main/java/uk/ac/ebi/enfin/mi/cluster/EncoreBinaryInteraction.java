package uk.ac.ebi.enfin.mi.cluster;

import psidev.psi.mi.tab.model.*;

import java.io.Serializable;
import java.util.*;

/**
 * Interface for binary interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/04/11</pre>
 */

public interface EncoreBinaryInteraction extends Serializable {

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

    public List<CrossReference> getPublicationIds();

    public void setPublicationIds(List<CrossReference> publicationIds);

    public void addPublicationId(CrossReference publicationId);

    public void addPublicationId(List<CrossReference> publicationIds);


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

    public void setAuthors(List<Author> authors);

    public void addAuthors(List<Author> authors);

    public void addAuthor(Author author);

    public List<CrossReference> getSourceDatabases();

    public void setSourceDatabases(List<CrossReference> sourceDatabases);

    public void addSourceDatabases(List<CrossReference> sourceDatabases);

    public void addSourceDatabase(CrossReference sourceDatabase);

    public List<CrossReference> getOrganismsA();

    public void setOrganismsA(List<CrossReference> organismsA);

    public void addOrganismsA(List<CrossReference> organismsA);

    public void addOrganismA(CrossReference organismA);

    public List<CrossReference> getOrganismsB();

    public void setOrganismsB(List<CrossReference> organismsB);

    public void addOrganismsB(List<CrossReference> organismsB);

    public void addOrganismB(CrossReference organismB);

    public String getMappingIdDbNames();

    public void setMappingIdDbNames(String mappingIdDbNames);

    // MI TAB 2.6

    public List<String> getExpansions();

    public void setExpansions(List<String> expansions);

    public void addExpansions(List<String> expansions);

    public void addExpansion(String expansion);

    public List<String> getBiologicalRolesA();

    public void setBiologicalRolesA(List<String> biologicalRolesA);

    public void addBiologicalRolesA(List<String> biologicalRolesA);

    public void addBiologicalRoleA(String biologicalRoleA);

    public List<String> getBiologicalRolesB();

    public void setBiologicalRolesB(List<String> biologicalRolesB);

    public void addBiologicalRolesB(List<String> biologicalRolesB);

    public void addBiologicalRoleB(String biologicalRoleB);

    public List<String> getExperimentalRolesA();

    public void setExperimentalRolesA(List<String> experimentalRolesA);

    public void addExperimentalRolesA(List<String> experimentalRolesA);

    public void addExperimentalRoleA(String experimentalRoleA);

    public List<String> getExperimentalRolesB();

    public void setExperimentalRolesB(List<String> experimentalRolesB);

    public void addExperimentalRolesB(List<String> experimentalRolesB);

    public void addExperimentalRoleB(String experimentalRoleB);

    public List<String> getInteractorTypesA();

    public void setInteractorTypesA(List<String> interactorTypes);

    public void addInteractorTypesA(List<String> interactorTypes);

    public void addInteractorTypeA(String interactorType);

    public List<String> getInteractorTypesB();

    public void setInteractorTypesB(List<String> interactorTypes);

    public void addInteractorTypesB(List<String> interactorTypes);

    public void addInteractorTypeB(String interactorType);

    public List<CrossReference> getXrefsA();

    public void setXrefsA(List<CrossReference> xrefsA);

    public void addXrefsA(List<CrossReference> xrefsA);

    public void addXrefA(CrossReference xrefA);

    public List<CrossReference> getXrefsB();

    public void setXrefsB(List<CrossReference> xrefsB);

    public void addXrefsB(List<CrossReference> xrefsB);

    public void addXrefB(CrossReference xrefB);

    public List<CrossReference> getXrefsInteraction();

    public void setXrefsInteraction(List<CrossReference> xrefsInteraction);

    public void addXrefsInteraction(List<CrossReference> xrefsInteraction);

    public void addXrefInteraction(CrossReference xrefInteraction);

    public List<Annotation> getAnnotationsA();

    public void setAnnotationsA(List<Annotation> annotationsA);

    public void addAnnotationsA(List<Annotation> annotationsA);

    public void addAnnotationA(Annotation annotationA);

    public List<Annotation> getAnnotationsB();

    public void setAnnotationsB(List<Annotation> annotationsB);

    public void addAnnotationsB(List<Annotation> annotationsB);

    public void addAnnotationB(Annotation annotationB);

    public List<Annotation> getAnnotationsInteraction();

    public void setAnnotationsInteraction(List<Annotation> annotationsInteraction);

    public void addAnnotationsInteraction(List<Annotation> annotationsInteraction);

    public void addAnnotationInteraction(Annotation annotationInteraction);

    public List<CrossReference> getHostOrganisms();

    public void setHostOrganisms(List<CrossReference> hostOrganisms);

    public void addHostOrganisms(List<CrossReference> hostOrganisms);

    public void addHostOrganism(CrossReference hostOrganism);

    public List<Parameter> getParameters();

    public void setParameters(List<Parameter> parameters);

    public void addParameters(List<Parameter> parameters);

    public void addParameter(Parameter parameter);

    public List<Date> getCreationDates();

    public void setCreationDates(List<Date> creationDates);

    public void addCreationDates(List<Date> creationDates);

    public void addCreationDate(Date creationDate);

    public List<Date> getUpdateDates();

    public void setUpdateDates(List<Date> updateDates);

    public void addUpdateDates(List<Date> updateDates);

    public void addUpdateDate(Date updateDate);

    public List<Checksum> getChecksumsA();

    public void setChecksumsA(List<Checksum> checksumsA);

    public void addChecksumsA(List<Checksum> checksumsA);

    public void addChecksumA(Checksum checksumA);

    public List<Checksum> getChecksumsB();

    public void setChecksumsB(List<Checksum> checksumsB);

    public void addChecksumsB(List<Checksum> checksumsB);

    public void addChecksumB(Checksum checksumB);

    public List<Checksum> getChecksumsInteraction();

    public void setChecksumsInteraction(List<Checksum> checksumsInteraction);

    public void addChecksumsInteraction(List<Checksum> checksumsInteraction);

    public void addChecksumInteraction(Checksum checksumInteraction);

    public List<Boolean> getNegatives();

    public void setNegatives(List<Boolean> negatives);

    public void addNegatives(List<Boolean> negatives);

    public void addNegative(Boolean negative);


    // TODO: add getters and setters for the missing MITAB 2.7 and 2.8 fields
}
