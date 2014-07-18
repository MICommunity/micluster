package psidev.psi.mi.jami.cluster.io.input;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

public class DefaultMitabClusterInputTest {

    private DefaultMitabClusterInput mitab = null;

    @Before
    public void setUp() throws Exception {
        //It has 106 lines
        this.mitab = new DefaultMitabClusterInput(
                new FileInputStream("src/test/resources/mitab_samples/EBI-988710_AND_EBI-1003831.tsv"));
    }

    @Test
    public void testHasNext() throws Exception {
        Assert.assertTrue(this.mitab.hasNext());
        this.mitab.next();
        Assert.assertTrue(this.mitab.hasNext());
        this.mitab.next();
        Assert.assertFalse(this.mitab.hasNext());
    }

}