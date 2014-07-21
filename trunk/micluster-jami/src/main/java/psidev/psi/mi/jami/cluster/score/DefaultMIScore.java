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
        if (this.methodWeight != null) {
            return this.methodWeight * this.methods.get(methodId);
        }
        return this.methods.get(methodId);
    }

    @Override
    public Double getTypeScore(String typeId) {
        if(this.typeWeight != null) {
            return this.typeWeight * this.types.get(typeId);
        }
        return this.types.get(typeId);
    }

    @Override
    public Double getPublicationScore(String pubmed) {
        return 0.0d;
    }

    @Override
    public void setMethodWeight(Double weight) {
        if (weight != null) this.methodWeight = weight;
    }

    @Override
    public void setTypeWeight(Double weight) {
        if (weight != null) this.typeWeight = weight;
    }

    @Override
    public void setPublicationWeight(Double weight) {
        //Nothing to do with that
    }
}
