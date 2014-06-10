package uk.ac.ebi.enfin.mi.cluster.model;

import psidev.psi.mi.tab.model.BinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.exception.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.model.EncoreBinaryInteraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Interface for interaction cluster
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/04/11</pre>
 */

public interface BinaryInteractionCluster<T extends EncoreBinaryInteraction> extends Serializable{

    public void runService();

    public Map<Integer, T> getInteractionMapping();

    public void setInteractionMapping(Map<Integer, T> interactionMapping);

    public Map<String, List<Integer>> getInteractorMapping();

    public void setInteractorMapping(Map<String, List<Integer>> interactorMapping);

    public int getInteractionMappingId();

    public void setInteractionMappingId(int interactionMappingId);

    public Map<String, String> getSynonymMapping();
    public void setSynonymMapping(Map<String, String> synonymMapping);

    public void setMappingIdDbNames( String mappingIdDbNames );

    public String getMappingIdDbNames();

    public Iterator<BinaryInteractionCluster> getBinaryInteractionIterator();

    public void setBinaryInteractionIterator(Iterator<BinaryInteractionCluster> binaryInteractionIterator);

    public void setBinaryInteractionIterator(InputStream is, boolean hasHeader) throws ClusterServiceException;

    public void setBinaryInteractionIterator(Reader r, boolean hasHeader) throws ClusterServiceException;

    public void saveResultsInMitab(String fileName) throws IOException;

}
