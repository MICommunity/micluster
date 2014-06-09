package uk.ac.ebi.enfin.mi.cluster.model;
import uk.ac.ebi.enfin.mi.cluster.model.AbstractEncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.model.MethodTypePair;

import java.util.*;

/**
 * Class to store minimum interaction information. It holds all the
 * necessary information to build MITAB25. It can include a binary
 * interaction as well as clustering information.
 *
 * @author Rafael
 * @since 24-May-2010
 * Time: 16:57:20
 */

public class EncoreInteraction extends AbstractEncoreInteraction {
    private Map<MethodTypePair, List<String>> methodTypePairListMap = new HashMap<MethodTypePair, List<String>>();
    private Set<String> distinctPublications = new HashSet<String>();

    public Map<MethodTypePair, List<String>> getMethodTypePairListMap() {
        return methodTypePairListMap;
    }

    public void setMethodTypePairListMap(Map<MethodTypePair, List<String>> methodTypePairListMap) {
        if (methodTypePairListMap != null){
            this.methodTypePairListMap = methodTypePairListMap;
        }
    }

    public Set<String> getDistinctPublications() {
        return distinctPublications;
    }

    public void setDistinctPublications(Set<String> distinctPublications) {
        this.distinctPublications = distinctPublications;
    }

    public Map<String, List<String>> getMethodToPubmed() {
        Map<String, List<String>> methodToPubmed = new HashMap<String, List<String>>();

        for (Map.Entry<MethodTypePair, List<String>> entry : methodTypePairListMap.entrySet()){

            MethodTypePair pair = entry.getKey();

            if (methodToPubmed.containsKey(pair.getMethod()) && pair.getMethod() != null){
                methodToPubmed.get(pair.getMethod()).addAll(entry.getValue());
            }
            else {
                if (pair.getMethod() != null){
                    methodToPubmed.put(pair.getMethod(), entry.getValue());
                }
            }
        }

        return methodToPubmed;
    }

    public Map<String, List<String>> getTypeToPubmed() {
        Map<String, List<String>> typeToPubmed = new HashMap<String, List<String>>();

        for (Map.Entry<MethodTypePair, List<String>> entry : methodTypePairListMap.entrySet()){

            MethodTypePair pair = entry.getKey();

            if (typeToPubmed.containsKey(pair.getType()) && pair.getType() != null){
                typeToPubmed.get(pair.getType()).addAll(entry.getValue());
            }
            else {
                if (pair.getType() != null){
                    typeToPubmed.put(pair.getType(), entry.getValue());
                }
            }
        }

        return typeToPubmed;
    }
}