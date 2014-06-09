package uk.ac.ebi.enfin.mi.cluster.model;

import java.util.List;
import java.util.Map;

/**
 * User: rafael
 * Date: 23-Aug-2010
 * Time: 18:09:39
 */
public class EncoreIdentifiers {
    Map<String,String> accessions;
    Map<String, List<String>> otherAccessions;

    public Map<String, String> getAccessions() {
        return accessions;
    }

    public void setAccessions(Map<String, String> accessions) {
        this.accessions = accessions;
    }

    public Map<String, List<String>> getOtherAccessions() {
        return otherAccessions;
    }

    public void setOtherAccessions(Map<String, List<String>> otherAccessions) {
        this.otherAccessions = otherAccessions;
    }
}
