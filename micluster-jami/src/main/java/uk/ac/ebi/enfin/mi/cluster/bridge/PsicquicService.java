package uk.ac.ebi.enfin.mi.cluster.bridge;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.wsclient.PsicquicSimpleClient;
import psidev.psi.mi.tab.PsimiTabException;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import uk.ac.ebi.enfin.mi.cluster.model.BinaryInteractionCluster;
import uk.ac.ebi.enfin.mi.cluster.model.InteractorCluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to help on retriving Psicquic information for
 * one specific source
 * @author Rafael
 * @since 25-May-2010
 * Time: 10:52:09
 */

public class PsicquicService {
    private static Logger logger = Logger.getLogger(PsicquicService.class);
    private ServiceType service;
    private String serviceName;

    public PsicquicService(String serviceName){
        this.serviceName = serviceName;
        PsicquicRegistryClient registryClient = new DefaultPsicquicRegistryClient();
        try {
            service = registryClient.getService(serviceName);
            if(service == null){
                logger.warn("Could not find service for " + this.serviceName);
            }
        } catch (PsicquicRegistryClientException e) {
            e.printStackTrace();
            logger.error("Could not get " + serviceName + " psicquic service");
            logger.error(e);
        }
    }

    public PsicquicService(String serviceName, String registryUrl){
        this.serviceName = serviceName;
        PsicquicRegistryClient registryClient;
        if (registryUrl != null && registryUrl.length() > 0){
             registryClient = new DefaultPsicquicRegistryClient(registryUrl);
        }
        else {
            registryClient = new DefaultPsicquicRegistryClient();
        }
        try {
            service = registryClient.getService(serviceName);
            if(service == null){
                logger.warn("Could not find service for " + this.serviceName);
            }
        } catch (PsicquicRegistryClientException e) {
            e.printStackTrace();
            logger.error("Could not get " + serviceName + " psicquic service");
            logger.error(e);
        }
    }

    public ServiceType getService() {
        return service;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer countInteractions(String query) throws IOException {
        Integer psicquicCount = null;
        if (service.isActive()){
                String encoded = getUriEncondedQuery(query);

                String url = service.getRestUrl();
                PsicquicSimpleClient client = new PsicquicSimpleClient(url);

                logger.info("Querying ..." + url);
                psicquicCount = Long.valueOf(client.countByQuery(encoded)).intValue();
        } else {
            logger.warn(serviceName + " psicquic service not active");
        }
        return psicquicCount;
    }

    public List<BinaryInteractionCluster> getInteractions(String query, int firstResult, int maxNumberOfResults) throws IOException {
        List<BinaryInteractionCluster> results = new ArrayList<BinaryInteractionCluster>(Math.min(2048, maxNumberOfResults));

        String encoded = getUriEncondedQuery(query);

        String url = service.getRestUrl();
        PsicquicSimpleClient client = new PsicquicSimpleClient(url);

        InputStream is = client.getByQuery(encoded, PsicquicSimpleClient.MITAB25, firstResult, maxNumberOfResults);

        PsimiTabReader tabReader = new PsimiTabReader();

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String str;
        while ((str = in.readLine()) != null) {
            BinaryInteractionCluster interaction = null;
            try {
                interaction = InteractionCluster.getFromBinaryInteraction(tabReader.readLine(str));
            } catch (PsimiTabException e) {
                throw new IOException("Impossible to read the mitab line", e);
            }
            results.add(interaction);
        }
        in.close();

        return results;
    }

    private String getUriEncondedQuery(String query) throws URIException {
        return URIUtil.encodeQuery(query, "UTF-8");
    }

}
