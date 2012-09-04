package uk.ac.ebi.enfin.mi.cluster;

import org.apache.log4j.Logger;
import org.hupo.psi.mi.psicquic.registry.ServiceType;
import org.hupo.psi.mi.psicquic.registry.client.PsicquicRegistryClientException;
import org.hupo.psi.mi.psicquic.registry.client.registry.DefaultPsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.registry.client.registry.PsicquicRegistryClient;
import org.hupo.psi.mi.psicquic.wsclient.PsicquicSimpleClient;
import psidev.psi.mi.tab.PsimiTabException;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

    public Integer countInteractions(String query) throws IOException {
        Integer psicquicCount = null;
        if (service.isActive()){
            String encoded = URLEncoder.encode(query, "UTF-8");
            encoded = encoded.replaceAll("\\+", "%20");

            String url = service.getRestUrl();
            PsicquicSimpleClient client = new PsicquicSimpleClient(url);

            logger.info("Querying ..." + url);
            psicquicCount = Long.valueOf(client.countByQuery(query)).intValue();
        } else {
            logger.warn(serviceName + " psicquic service not active");
        }
        return psicquicCount;
    }

    public List<BinaryInteraction> getInteractions(String query, int firstResult, int maxNumberOfResults) throws IOException {
        List<BinaryInteraction> results = new ArrayList<BinaryInteraction>(Math.min(2048, maxNumberOfResults));

        String url = service.getRestUrl();
        PsicquicSimpleClient client = new PsicquicSimpleClient(url);

        InputStream is = client.getByQuery(query, PsicquicSimpleClient.MITAB25_COMPRESSED, firstResult, maxNumberOfResults);

        GZIPInputStream gzipIs = new GZIPInputStream(is);

        PsimiTabReader tabReader = new PsimiTabReader();

        BufferedReader in = new BufferedReader(new InputStreamReader(gzipIs));
        String str;
        while ((str = in.readLine()) != null) {
            BinaryInteraction interaction = null;
            try {
                interaction = tabReader.readLine(str);
            } catch (PsimiTabException e) {
                throw new IOException("Impossible to read the mitab line", e);
            }
            results.add(interaction);
        }
        in.close();

        return results;
    }
}
