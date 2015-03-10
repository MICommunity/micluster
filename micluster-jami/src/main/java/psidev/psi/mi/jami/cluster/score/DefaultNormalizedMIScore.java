package psidev.psi.mi.jami.cluster.score;

import java.util.Properties;

/**
 * This is the default Normalized version for the MIScore.
 *
 * Created by maitesin on 29/07/2014.
 */
public class DefaultNormalizedMIScore extends AbstractMIScore {

    public DefaultNormalizedMIScore(String filename) {
        super(filename);
    }

    @Override
    public double getMethodsScore() {
        double totalMethods = 0.0d;
        for(String methodId : this.methods)
            totalMethods += this.methodWeight * getMethodValue(methodId);
        return totalMethods;
    }

    @Override
    public double getTypesScore() {
        double totalTypes = 0.0d;
        for(String typeId : this.types)
            totalTypes += this.typeWeight * getTypeValue(typeId);
        return totalTypes;
    }

    @Override
    protected MIScoreProperty getMIScoreproperty(Properties properties, String prefix, String[] fields) {
        MIScoreProperty property = new MIScoreProperty();
        if (fields.length >= 3) {
            property.setId(properties.getProperty(prefix + fields[0])); //ID
            property.setName(properties.getProperty(prefix + fields[1])); //Name
            property.setScore(Double.parseDouble(properties.getProperty(prefix + fields[2]))); //Normalized Score
        }
        return property;
    }
}
