package uk.ac.ebi.enfin.mi.cluster.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * Reader spanning over multiple readers.
 * <p/>
 * The code of this class is mostly copied from a package class in project Google Guava:
 * http://code.google.com/p/guava-libraries/source/browse/trunk/src/com/google/common/io/MultiReader.java
 *
 * @author Bin Zhu
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.3
 */

public class CompositeReader extends Reader {

    private final Iterator<? extends Reader> it;
    private Reader current;

    public CompositeReader( Iterator<? extends Reader> readers ) throws IOException {
        this.it = readers;
        advance();
    }

    /**
     * Closes the current reader and opens the next one, if any.
     */
    private void advance() throws IOException {
        close();
        if ( it.hasNext() ) {
            current = it.next();
        }
    }

    @Override
    public int read( char cbuf[], int off, int len ) throws IOException {
        if ( current == null ) {
            return -1;
        }
        int result = current.read( cbuf, off, len );
        if ( result == -1 ) {
            advance();
            return read( cbuf, off, len );
        }
        return result;
    }

    @Override
    public long skip( long n ) throws IOException {
        if( n < 0 ) {
            throw new IllegalArgumentException( "n is negative" );
        }
        if ( n > 0 ) {
            while ( current != null ) {
                long result = current.skip( n );
                if ( result > 0 ) {
                    return result;
                }
                advance();
            }
        }
        return 0;
    }

    @Override
    public boolean ready() throws IOException {
        return ( current != null ) && current.ready();
    }

    @Override
    public void close() throws IOException {
        if ( current != null ) {
            try {
                current.close();
            } finally {
                current = null;
            }
        }
    }
}