package uk.ac.ebi.enfin.mi.cluster.utils;

import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CompositeInputStream Tester.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.3
 */
public class CompositeInputStreamTest {

    @Test
    public void join_inputstream_own() throws Exception {

        // 2 lines
        File f1 = new File( CompositeInputStreamTest.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );

        // 89 lines
        File f2 = new File( CompositeInputStreamTest.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );

        List<InputStream> readers = new ArrayList<InputStream>();
        readers.add( new FileInputStream( f1 ) );
        readers.add( new FileInputStream( f2 ) );

        InputStream is = new CompositeInputStream( readers.iterator() );

        int count = 0;
        BufferedReader in = new BufferedReader( new InputStreamReader( is ) );
        String str;
        while ( ( str = in.readLine() ) != null ) {
            count++;
        }
        in.close();

        Assert.assertEquals( 91, count );
    }
}
