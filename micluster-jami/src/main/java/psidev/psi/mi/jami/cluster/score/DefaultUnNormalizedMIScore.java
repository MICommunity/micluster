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
    public double getMethodsScore() {
        double totalMethods = 0.0d;
        for(String methodId : this.methods)
            //TODO load the whole ontology to find the parents
            if (this.methodsValues.get(methodId) != null) {
                totalMethods += this.methodWeight * this.methodsValues.get(methodId).getScore();
            }
        return totalMethods;
    }

    @Override
    public double getTypesScore() {
        double totalTypes = 0.0d;
        for(String typeId : this.types)
            totalTypes += this.typeWeight * this.typesValues.get(typeId).getScore();
        return totalTypes;
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
