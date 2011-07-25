package uk.ac.ebi.enfin.mi.cluster;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.wsclient.UniversalPsicquicClient;
import org.hupo.psi.mi.psicquic.wsclient.PsicquicClientException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.URLEncoder;
import java.net.URL;
import java.io.IOException;

import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.search.SearchResult;

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

    public ServiceType getService() {
        return service;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer countInteractions(String query) {
        Integer psicquicCount = null;
        if (service.isActive()){
            try {
                String encoded = URLEncoder.encode(query, "UTF-8");
                encoded = encoded.replaceAll("\\+", "%20");
                System.out.println(service.getSoapUrl());
                String url = service.getRestUrl()+"query/"+ encoded +"?format=count";
                logger.info("Querying ..." + url);
                final InputStream is = new URL( url ).openStream();
                String strCount = IOUtils.toString( is );
                is.close();
                psicquicCount = Integer.parseInt(strCount);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e);
            }
        } else {
            logger.warn(serviceName + "psicquic service not active");
        }
        return psicquicCount;
    }

    public SearchResult<BinaryInteraction> getFisrtTenInteractions(String query) {
        return getInteractions(query, 0, 10);
    }

    public SearchResult<BinaryInteraction> getInteractions(String query, int firstResult, int maxNumberOfResults) {

        // TODO we could use the simple PSICQUIC client and get the data using compressed MITAB output:
        // TODO c.f. http://code.google.com/p/psicquic/wiki/ClientCodeSample
        // TODO c.f. http://code.google.com/p/psicquic/source/browse/trunk/psicquic-simple-client/src/example/java/org/hupo/psi/mi/psicquic/wsclient/PsicquicSimpleExampleCompression.java

        String url = service.getSoapUrl();
        UniversalPsicquicClient client = new UniversalPsicquicClient(url);

        final Client c = ClientProxy.getClient(client.getService());

        final HTTPConduit http = (HTTPConduit) c.getConduit();
        final HTTPClientPolicy httpClientPolicy = http.getClient();

        httpClientPolicy.setConnectionTimeout(0);
        httpClientPolicy.setReceiveTimeout(0);

        SearchResult<BinaryInteraction> searchResult = null;
        try {
            searchResult = client.getByQuery(query, firstResult, maxNumberOfResults);
        } catch (PsicquicClientException e) {

            // TODO deal with errors
            e.printStackTrace();
        }
        return searchResult;
    }
}
