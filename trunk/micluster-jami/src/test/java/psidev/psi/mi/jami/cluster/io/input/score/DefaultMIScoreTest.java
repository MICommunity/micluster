package psidev.psi.mi.jami.cluster.io.input.score;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.cluster.score.DefaultUnNormalizedMIScore;
import psidev.psi.mi.jami.cluster.score.MIScore;

public class DefaultMIScoreTest {

    private MIScore miScore = null;
    private final String propertiesFile = "score/scoreCategories.properties";

    @Before
    public void setUp() throws Exception {
        this.miScore = new DefaultUnNormalizedMIScore(this.propertiesFile);
    }

    @Test
    public void testGetMethodScore() throws Exception {
        Assert.assertTrue(this.miScore.getMethodScore("MI:0013") == 3.0d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0090") == 2.0d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0254") == 0.3d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0255") == 0.3d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0401") == 3.0d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0428") == 0.6d);
        Assert.assertTrue(this.miScore.getMethodScore("unknown") == 0.15d);
    }

    @Test
    public void testGetTypeScore() throws Exception {
        Assert.assertTrue(this.miScore.getTypeScore("MI:0208") == 0.24d);
        Assert.assertTrue(this.miScore.getTypeScore("MI:0403") == 0.15d);
        Assert.assertTrue(this.miScore.getTypeScore("MI:0914") == 1.0d);
        Assert.assertTrue(this.miScore.getTypeScore("MI:0915") == 2.0d);
        Assert.assertTrue(this.miScore.getTypeScore("MI:0407") == 3.0d);
        Assert.assertTrue(this.miScore.getTypeScore("unknown") == 0.1d);
    }

    @Test
    public void testGetPublicationScore() throws Exception {
        Assert.assertTrue(this.miScore.getPublicationScore("does not matter") == 0.0d);
    }

    @Test
    public void testSetMethodWeight() throws Exception {
        Assert.assertTrue(this.miScore.getMethodScore("MI:0401") == 3.0d);
        this.miScore.setMethodWeight(2.0d);
        Assert.assertTrue(this.miScore.getMethodScore("MI:0401") == 6.0d);
    }

    @Test
    public void testSetTypeWeight() throws Exception {
        Assert.assertTrue(this.miScore.getTypeScore("MI:0407") == 3.0d);
        this.miScore.setTypeWeight(2.0d);
        Assert.assertTrue(this.miScore.getTypeScore("MI:0407") == 6.0d);
    }

    @Test
    public void testSetPublicationWeight() throws Exception {
        //The method publication weight does nothing.
        this.miScore.setPublicationWeight(20.0d);
    }
}