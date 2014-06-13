package psidev.psi.mi.jami.cluster.enrich;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.wsclient.PsicquicSimpleClient;
import psidev.psi.mi.jami.cluster.io.input.ClusterInput;
import psidev.psi.mi.jami.cluster.io.input.MitabClusterInput;
import psidev.psi.mi.jami.model.Interaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maitesin on 12/06/2014.
 */
public class PsicquicEnrichment {

    /************************/
    /***   Constructors   ***/
    /************************/
    public PsicquicEnrichment(String service) throws PsicquicRegistryClientException {
        this(new DefaultPsicquicRegistryClient().getService(service));
    }

    public PsicquicEnrichment(ServiceType service){
        this.service = service;
        this.psm = new PsicquicSimpleClient(service.getRestUrl());
        if (this.log.isInfoEnabled()) this.log.info("new psicquic simple client connected to " + service.getName()
                + " and with the url: " + service.getRestUrl());
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public Long countInteractions(String query) throws IOException {
        Long count = null;
        if(this.service.isActive()){
            count = countByQueryAndLogIt(query);
        }
        return count;
    }

    public List<Interaction> getInteractions(String query, int firstResult, int maxNumberOfResults) throws IOException {
        List<Interaction> interactions = new LinkedList<Interaction>();
        InputStream input = null;
        input = getByQueyAndLogIt(query, firstResult, maxNumberOfResults);
        ClusterInput mitabClusterInput = new MitabClusterInput(input);
        while (mitabClusterInput.hasNext()){
            interactions.add(mitabClusterInput.next());
        }
        return interactions;
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/

    private Long countByQueryAndLogIt(String query) throws IOException {
        Long count = null;
        try {
            count = this.psm.countByQuery(query);
        } catch (IOException e) {
            this.log.error("Problem to count by query to " + service.getName()
                    + ", with the url: " + service.getRestUrl()
                    + " and query: " + query, e);
            throw e;
        }
        return count;
    }

    private InputStream getByQueyAndLogIt(String query, int firstResult, int maxNumberOfResults) throws IOException {
        InputStream input = null;
        try {
            input = this.psm.getByQuery(query, PsicquicSimpleClient.MITAB25, firstResult, maxNumberOfResults);
        } catch (IOException e) {
            this.log.error("Problem to get by query to " + service.getName()
                    + ", with the url: " + service.getRestUrl()
                    + ", query: " + query
                    + ", first result: " + firstResult
                    + " and max number of results: " + maxNumberOfResults, e);
            throw e;
        }
        return input;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Logger log = Logger.getLogger(PsicquicEnrichment.class);
    private PsicquicSimpleClient psm = null;
    private ServiceType service = null;
}
