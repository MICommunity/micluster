package psidev.psi.mi.jami.cluster.io.output;

import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.Interaction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by maitesin on 17/07/2014.
 */
public class DefaultMitabClusterOutput extends AbstractMitabClusterOutput implements ClusterOutput {
    @Override
    public void initialiseContext(Map map) {
        super.initialiseContext(map);
    }

    @Override
    public void start() throws MIIOException {
        super.start();
    }

    @Override
    public void end() throws MIIOException {
        super.end();
    }

    @Override
    public void write(Interaction interaction) throws MIIOException {

    }

    @Override
    public void write(Collection collection) throws MIIOException {

    }

    @Override
    public void write(Iterator iterator) throws MIIOException {

    }

    @Override
    public void flush() throws MIIOException {
        super.flush();
    }

    @Override
    public void close() throws MIIOException {
        super.close();
    }

    @Override
    public void reset() throws MIIOException {
        super.reset();
    }
}
