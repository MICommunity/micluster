package psidev.psi.mi.jami.cluster.score.ols;

import org.junit.Assert;
import org.junit.Test;

public class MIOntologyTest {
    @Test
    public void testMIOntology() throws Exception {
        MIOntology remoteOntology = new MIOntology(); //Loading from the OLS web service
        Assert.assertEquals(7, remoteOntology.getParents("MI:0437").size());
        MIOntology localOntology = new MIOntology(false); //Loading from the OLS file
        Assert.assertEquals(7, localOntology.getParents("MI:0437").size());
    }
}