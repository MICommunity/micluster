package psidev.psi.mi.jami.cluster.score;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by maitesin on 18/07/2014.
 */
public abstract class AbstractMIScore implements MIScore {
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public AbstractMIScore(String filename){
        //Set the default values
        this.types = new HashMap<String, MIScoreProperty>();
        this.methods = new HashMap<String, MIScoreProperty>();
        this.typeWeight = 1.0d;
        this.methodWeight = 1.0d;
        //Process the file
        Properties properties = new Properties();
        String [] fields = null; //id,name,score,unNormalizedScore
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fields = properties.getProperty("properties").split(",");
        for (Object key : properties.keySet()) {
            String skey = key.toString();
            if(skey.startsWith("type") && skey.endsWith(fields[0])){
                //Type property
                insertTypeProperty(properties, key, fields);
            }
            else if(skey.startsWith("method") && skey.endsWith(fields[0])){
                //Method property
                insertMethodProperty(properties, key, fields);
            }
        }
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/
    private void insertTypeProperty(Properties properties, Object key, String[] fields) {
        String prefix = key.toString().substring(0, key.toString().length()-fields[0].length());
        this.types.put(properties.getProperty(key.toString()), getMIScoreproperty(properties, prefix, fields));
    }

    private void insertMethodProperty(Properties properties, Object key, String[] fields) {
        String prefix = key.toString().substring(0, key.toString().length()-fields[0].length());
        this.methods.put(properties.getProperty(key.toString()), getMIScoreproperty(properties, prefix, fields));
    }


    private MIScoreProperty getMIScoreproperty(Properties properties, String prefix, String[] fields) {
        MIScoreProperty property = new MIScoreProperty();
        if (fields.length > 4) {
            property.setId(properties.getProperty(prefix + fields[0])); //ID
            property.setName(properties.getProperty(prefix + fields[1])); //Name
            property.setScore(Double.parseDouble(properties.getProperty(prefix + fields[2]))); //Score
            property.setUnNormalizedScore(Double.parseDouble(properties.getProperty(prefix + fields[3]))); //Unnormalized Score
        }
        return property;
    }


    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected Map<String, MIScoreProperty> methods;
    protected Map<String, MIScoreProperty> types;
    protected Double methodWeight;
    protected Double typeWeight;

}
