package uk.ac.ebi.enfin.mi.cluster.ols;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.*;

/**
 * User: rafael
 * Date: 25-Aug-2010
 * Time: 14:52:10
 */
public class TestOlsHelper {

    @Test
    public void getJsonChildren(){
        OlsHelper OH = new OlsHelper();
        Map<String,String> children = OH.getJsonChildren("MI:0660");
        assertNotNull(children);
    }
}
