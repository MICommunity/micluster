package psidev.psi.mi.jami.cluster.io.input;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class MitabClusterInputTest {

    private MitabClusterInput mitab = null;

    @Before
    public void setUp() throws Exception {
        //It has 106 lines
        this.mitab = new MitabClusterInput(new FileInputStream("src/test/resources/mitab_samples/brca2_intact.tsv"));
    }

    @Test
    public void testNext() throws Exception {

    }

    @Test
    public void testHasNext() throws Exception {
        Assert.assertTrue(this.mitab.hasNext());
        for(int i=0; i<105; ++i){
            this.mitab.next();
        }
        Assert.assertTrue(this.mitab.hasNext());
        this.mitab.next();
        Assert.assertFalse(this.mitab.hasNext());
    }
}