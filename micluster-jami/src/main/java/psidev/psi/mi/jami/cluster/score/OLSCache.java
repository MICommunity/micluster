package psidev.psi.mi.jami.cluster.score;

import org.apache.log4j.Logger;
import uk.ac.ebi.enfin.mi.score.ols.MIOntology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 10/01/17.
 */
public class OLSCache {
    private static final Logger logger = Logger.getLogger(OLSCache.class);
    private Map<String, Map<String,String>> mapOfMethodTermsCache;
    private Map<String, Map<String,String>> mapOfTypeTermsCache;
    protected MIOntology MIO = new MIOntology();
    private static OLSCache olsCache;
    public OLSCache(){
        getMITermsFromOLS();
    }

    public static OLSCache getInstance() {
        if (olsCache == null) {
            olsCache = new OLSCache();
        }
        return olsCache;
    }

    public Map<String, Map<String, String>> getMapOfMethodTermsCache() {
        return mapOfMethodTermsCache;
    }

    public void setMapOfMethodTermsCache(Map<String, Map<String, String>> mapOfMethodTermsCache) {
        this.mapOfMethodTermsCache = mapOfMethodTermsCache;
    }

    public Map<String, Map<String, String>> getMapOfTypeTermsCache() {
        return mapOfTypeTermsCache;
    }

    public void setMapOfTypeTermsCache(Map<String, Map<String, String>> mapOfTypeTermsCache) {
        this.mapOfTypeTermsCache = mapOfTypeTermsCache;
    }

    private void getMITermsFromOLS(){
        ArrayList<String> methodParentTerms = new ArrayList<String>();
        methodParentTerms.add("MI:0013");
        methodParentTerms.add("MI:0090");
        methodParentTerms.add("MI:0254");
        methodParentTerms.add("MI:0255");
        methodParentTerms.add("MI:0401");
        methodParentTerms.add("MI:0428");
        Map<String, Map<String,String>> mapOfMethodTerms = MIO.getMapOfTerms(methodParentTerms);

        logger.debug("Create a map of type terms using parent terms");
        ArrayList<String> typeParentTerms = new ArrayList<String>();
        typeParentTerms.add("MI:0208");
        typeParentTerms.add("MI:0407");

        setMapOfMethodTermsCache(mapOfMethodTerms);

        Map<String, Map<String,String>> mapOfTypeTerms = MIO.getMapOfTerms(typeParentTerms);
        /* No need to look for MI:0403 since it has no children. Just include them in the mappingParentTerms */
        mapOfTypeTerms.put("MI:0403", new HashMap<String, String>());
        /* No need to look for MI:0914 and "MI:0915 in OLS since they are parent terms of MI:0407 */
        mapOfTypeTerms.put("MI:0914", new HashMap<String, String>());
        mapOfTypeTerms.put("MI:0915", new HashMap<String, String>());

        setMapOfTypeTermsCache(mapOfTypeTerms);
    }
}
