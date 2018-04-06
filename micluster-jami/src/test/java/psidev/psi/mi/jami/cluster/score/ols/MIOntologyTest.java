package psidev.psi.mi.jami.cluster.score.ols;

import org.junit.Assert;
import org.junit.Test;

public class MIOntologyTest {
    @Test
    public void testMIOntology() throws Exception {
        MIOntology miOntology = MIOntology.getInstance(true); //Loading from the OLS web service
        Assert.assertTrue(miOntology.getParents("MI:0437").size() == 6);
        miOntology = MIOntology.getInstance(false); //Loading from the OLS file
        Assert.assertTrue(miOntology.getParents("MI:0437").size() == 6);
    }
}