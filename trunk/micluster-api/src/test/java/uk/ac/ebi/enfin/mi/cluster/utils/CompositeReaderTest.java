package uk.ac.ebi.enfin.mi.cluster.utils;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CompositeReader Tester.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.3
 */
public class CompositeReaderTest {

    @Test
    public void join_readers_guava() throws Exception {
        Charset utf8 = Charset.forName( "UTF-8" );

        // 2 lines
        File f1 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );

        // 89 lines
        File f2 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );

        // NOTE downside, I can only give a File, not a Reader or an InputStream
        InputSupplier<InputStreamReader> rs1 = Files.newReaderSupplier( f1, utf8 );
        InputSupplier<InputStreamReader> rs2 = Files.newReaderSupplier( f2, utf8 );

        InputSupplier<Reader> combined = CharStreams.join( rs1, rs2 );

        final Reader reader = combined.getInput();

        int count = 0;
        BufferedReader in = new BufferedReader( reader );
        String str;
        while ( ( str = in.readLine() ) != null ) {
            count++;
        }
        in.close();

        Assert.assertEquals( 91, count );
    }

    @Test
    public void join_readers_own() throws Exception {


        // 2 lines
        File f1 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );

        // 89 lines
        File f2 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );

        List<Reader> readers = new ArrayList<Reader>();
        readers.add( new FileReader( f1 ) );
        readers.add( new FileReader( f2 ) );

        Reader reader = new CompositeReader( readers.iterator() );

        int count = 0;
        BufferedReader in = new BufferedReader( reader );
        String str;
        while ( ( str = in.readLine() ) != null ) {
            count++;
        }
        in.close();

        Assert.assertEquals( 91, count );
    }

    @Test
    public void join_inputstream_via_readers_own() throws Exception {

        // 2 lines
        File f1 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_innateDB.tsv" ).getFile() );

        // 89 lines
        File f2 = new File( CompositeReaderTest.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );

        List<Reader> readers = new ArrayList<Reader>();
        readers.add( new InputStreamReader( new FileInputStream( f1 ) ) );
        readers.add( new InputStreamReader( new FileInputStream( f2 ) ) );

        Reader reader = new CompositeReader( readers.iterator() );

        int count = 0;
        BufferedReader in = new BufferedReader( reader );
        String str;
        while ( ( str = in.readLine() ) != null ) {
            count++;
        }
        in.close();

        Assert.assertEquals( 91, count );
    }
}
