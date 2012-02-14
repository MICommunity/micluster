package uk.ac.ebi.enfin.mi.cluster;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to store minimum interaction information. It holds all the
 * necessary information to build MITAB25. It can include a binary
 * interaction as well as clustering information.
 *
 * @author Rafael
 * @since 24-May-2010
 * Time: 16:57:20
 * @Deprecated Use EncoreInteractionForScoring instead
 */
@Deprecated
public class EncoreInteraction extends AbstractEncoreInteraction {
    static Logger logger = Logger.getLogger(EncoreInteraction.class);
    Map<String, List<String>> methodToPubmed = new HashMap<String, List<String>>();
    Map<String, List<String>> typeToPubmed = new HashMap<String, List<String>>();

    public EncoreInteraction(){}

    public Map<String, List<String>> getMethodToPubmed() {
        return methodToPubmed;
    }

    public void setMethodToPubmed(Map<String, List<String>> methodToPubmed) {
        this.methodToPubmed = methodToPubmed;
    }

    public void addMethodToPubmed(String method, String pubmed) {
        methodToPubmed = setMapping(methodToPubmed, method, pubmed);
    }

    public void addMethodToPubmed(Map<String, List<String>> methodToPubmed) {
        this.methodToPubmed = setMapping(this.methodToPubmed, methodToPubmed);
    }


    public Map<String, List<String>> getTypeToPubmed() {
        return typeToPubmed;
    }

    public void setTypeToPubmed(Map<String, List<String>> typeToPubmed) {
        this.typeToPubmed = typeToPubmed;
    }

    public void addTypeToPubmed(Map<String, List<String>> typeToPubmed) {
        this.typeToPubmed = setMapping(this.typeToPubmed, typeToPubmed);
    }

    public void addTypeToPubmed(String type, String pubmed) {
        typeToPubmed = setMapping(typeToPubmed, type, pubmed);
    }
}