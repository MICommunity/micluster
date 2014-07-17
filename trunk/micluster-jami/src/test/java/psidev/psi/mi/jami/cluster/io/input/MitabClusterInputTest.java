package psidev.psi.mi.jami.cluster.io.input;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.tab.extension.MitabInteractionEvidence;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MitabClusterInputTest {

    private MitabClusterInput mitab = null;

    @Before
    public void setUp() throws Exception {
        //It has 106 lines
        this.mitab = new MitabClusterInput(
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