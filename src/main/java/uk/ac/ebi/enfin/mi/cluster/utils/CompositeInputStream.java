package uk.ac.ebi.enfin.mi.cluster.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * InputStream spanning over multiple InputStreams.
 * <p/>
 * The code of this class is mostly copied from a package class in project Google Guava:
 * http://code.google.com/p/guava-libraries/source/browse/trunk/src/com/google/common/io/MultiInputStream.java
 *
 * @author Chris Nokleberg
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.3
 */
public class CompositeInputStream extends InputStream {

    private Iterator<? extends InputStream> it;
    private InputStream in;

    /**
     * Creates a new instance.
     *
     * @param it an iterator that will provide each substream
     */
    public CompositeInputStream( Iterator<? extends InputStream> it ) throws IOException {
        this.it = it;
        advance();
    }

    @Override
    public void close() throws IOException {
        if ( in != null ) {
            try {
                in.close();
            } finally {
                in = null;
            }
        }
    }

    /**
     * Closes the current input stream and opens the next one, if any.
     */
    private void advance() throws IOException {
        close();
        if ( it.hasNext() ) {
            in = it.next();
        }
    }

    @Override
    public int available() throws IOException {
        if ( in == null ) {
            return 0;
        }
        return in.available();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        if ( in == null ) {
            return -1;
        }
        int result = in.read();
        if ( result == -1 ) {
            advance();
            return read();
        }
        return result;
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException {
        if ( in == null ) {
            return -1;
        }
        int result = in.read( b, off, len );
        if ( result == -1 ) {
            advance();
            return read( b, off, len );
        }
        return result;
    }

    @Override
    public long skip( long n ) throws IOException {
        if ( in == null || n <= 0 ) {
            return 0;
        }
        long result = in.skip( n );
        if ( result != 0 ) {
            return result;
        }
        if ( read() == -1 ) {
            return 0;
        }
        return 1 + in.skip( n - 1 );
    }
}