package uk.ac.ebi.enfin.mi.cluster;

import org.junit.Before;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Example files used for testing purposes
 *
 * @author Rafael Jimenez (rafael@ebi.ac.uk)
 * @version $Id$
 * @since 1.6
 */
public abstract class ExampleFiles {
    public final File brca2_mint = new File( ExampleFiles.class.getResource( "/mitab_samples/brca2_mint.tsv" ).getFile() );
    public final File brca2_intact = new File( ExampleFiles.class.getResource( "/mitab_samples/brca2_intact.tsv" ).getFile() );
    public final File brca2_innatedb = new File( ExampleFiles.class.getResource( "/mitab_samples/brca2_innatedb.tsv" ).getFile() );
    public final File brca2_biogrid = new File( ExampleFiles.class.getResource( "/mitab_samples/brca2_biogrid.tsv" ).getFile() );
    public final File P37173_mint = new File( ExampleFiles.class.getResource( "/mitab_samples/P37173_mint.tsv" ).getFile() );
    public final File P37173_intact = new File( ExampleFiles.class.getResource( "/mitab_samples/P37173_intact.tsv" ).getFile() );
    public final File P37173_innatedb = new File( ExampleFiles.class.getResource( "/mitab_samples/P37173_innatedb.tsv" ).getFile() );
    public final File P07200_intact = new File( ExampleFiles.class.getResource( "/mitab_samples/P07200_intact.tsv" ).getFile() );
    public final File P07200_irefindex = new File( ExampleFiles.class.getResource( "/mitab_samples/P07200_irefindex.tsv" ).getFile() );
    public final String allMappingNames = "uniprotkb,intact,ddbj/embl/genbank,refseq,chebi,irefindex,hgnc,ensembl";

    public int countLines(File file) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        lnr.skip(Long.MAX_VALUE);
        return lnr.getLineNumber();
    }
    

}
