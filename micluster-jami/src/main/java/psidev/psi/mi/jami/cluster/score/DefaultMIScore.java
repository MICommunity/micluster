package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 18/07/2014.
 */
public class DefaultMIScore extends AbstractMIScore {

    public DefaultMIScore(String filename) {
        super(filename);
    }

    @Override
    public Double getMethodScore(String methodId) {
        return this.methods.get(methodId);
    }

    @Override
    public Double getTypeScore(String typeId) {
        return this.types.get(typeId);
    }

    @Override
    public Double getPublicationScore(String pubmed) {
        return 0.0d;
    }
}
