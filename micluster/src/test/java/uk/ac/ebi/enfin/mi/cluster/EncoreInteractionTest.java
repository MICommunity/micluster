package uk.ac.ebi.enfin.mi.cluster;
import org.apache.log4j.Logger;
import org.junit.Test;

import static junit.framework.Assert.*;


/**
 * @author Rafael
 * @since 25-May-2010
 * Time: 09:40:39
 */
@Deprecated
public class EncoreInteractionTest {
    static Logger logger = Logger.getLogger( EncoreInteractionTest.class);

    @Test
    public void typeToPubmed(){
        EncoreInteraction eI = new EncoreInteraction();
        eI.addTypeToPubmed("type1","pubmed1");
        eI.addTypeToPubmed("type1","pubmed2");
        eI.addTypeToPubmed("type4","pubmed2");
        eI.addTypeToPubmed("type1","pubmed2");
        assertEquals(eI.getTypeToPubmed().size(), 2);
    }
}
