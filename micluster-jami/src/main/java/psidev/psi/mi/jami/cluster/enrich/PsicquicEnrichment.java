package psidev.psi.mi.jami.cluster.enrich;

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
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public Long countInteractions(String query) throws IOException {
        Long count = null;
        if(this.service.isActive()){
            count = this.psm.countByQuery(query);
        }
        return count;
    }

    public List<Interaction> getInteractions(String query, int firstResult, int maxNumberOfResults) throws IOException {
        List<Interaction> interactions = new LinkedList<Interaction>();
        InputStream input = this.psm.getByQuery(query, PsicquicSimpleClient.MITAB25, firstResult, maxNumberOfResults);
        ClusterInput mitabClusterInput = new MitabClusterInput(input);
        while (mitabClusterInput.hasNext()){
            interactions.add(mitabClusterInput.next());
        }
        return interactions;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private PsicquicSimpleClient psm = null;
    private ServiceType service = null;
}
