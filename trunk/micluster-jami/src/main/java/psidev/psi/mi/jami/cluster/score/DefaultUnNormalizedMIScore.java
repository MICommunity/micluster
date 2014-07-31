package psidev.psi.mi.jami.cluster.score;

import java.util.Properties;

/**
 * Created by maitesin on 18/07/2014.
 */
public class DefaultUnNormalizedMIScore extends AbstractMIScore {

    public DefaultUnNormalizedMIScore(String filename) {
        super(filename);
    }

    @Override
    public Double getMethodScore(String methodId) {
        if (this.methodWeight != null) {
            return this.methodWeight * this.methods.get(methodId).getScore();
        }
        return this.methods.get(methodId).getScore();
    }

    @Override
    public Double getTypeScore(String typeId) {
        if(this.typeWeight != null) {
            return this.typeWeight * this.types.get(typeId).getScore();
        }
        return this.types.get(typeId).getScore();
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

    protected MIScoreProperty getMIScoreproperty(Properties properties, String prefix, String[] fields) {
        MIScoreProperty property = new MIScoreProperty();
        if (fields.length >= 4) {
            property.setId(properties.getProperty(prefix + fields[0])); //ID
            property.setName(properties.getProperty(prefix + fields[1])); //Name
            property.setScore(Double.parseDouble(properties.getProperty(prefix + fields[3]))); //Unnormalized Score
        }
        return property;
    }
}
