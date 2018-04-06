package psidev.psi.mi.jami.cluster.score;

import psidev.psi.mi.jami.cluster.score.ols.MIOntology;

import java.io.IOException;
import java.util.*;

/**
 * Abstract class that provides necessary infrastructure to
 * manage the implementation of MIScore.
 *
 * Created by maitesin on 18/07/2014.
 */
public abstract class AbstractMIScore implements MIScore {
    /***********************/
    /***   Constructor   ***/
    /***********************/
    public AbstractMIScore(String filename){
        //Set the default values
        this.methods = new ArrayList<String>();
        this.types = new ArrayList<String>();
        this.typesValues = new HashMap<String, MIScoreProperty>();
        this.methodsValues = new HashMap<String, MIScoreProperty>();
        this.typeWeight = 1.0d;
        this.methodWeight = 1.0d;
        this.miOntology = MIOntology.getInstance(true);
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

    @Override
    public void addMethod(String methodId){
        this.methods.add(methodId);
    }

    @Override
    public void addType(String typeId) {
        this.types.add(typeId);
    }

    @Override
    public void addPublication(String pubId) {
        //Nothing to do here
    }

    @Override
    public double getPublicationsScore() {
        return 0.0d;
    }

    @Override
    public void setMethodWeight(double weight) {
        this.methodWeight = weight;
    }

    @Override
    public void setTypeWeight(double weight) {
        this.typeWeight = weight;
    }

    @Override
    public void setPublicationWeight(double weight) {
        //Nothing to do here
    }

    @Override
    public void clear() {
        this.methods.clear();
        this.types.clear();
    }
    /***************************/
    /***   Private Methods   ***/
    /***************************/
    private void insertTypeProperty(Properties properties, Object key, String[] fields) {
        String prefix = key.toString().substring(0, key.toString().length()-fields[0].length());
        this.typesValues.put(properties.getProperty(key.toString()), getMIScoreproperty(properties, prefix, fields));
    }

    private void insertMethodProperty(Properties properties, Object key, String[] fields) {
        String prefix = key.toString().substring(0, key.toString().length()-fields[0].length());
        this.methodsValues.put(properties.getProperty(key.toString()), getMIScoreproperty(properties, prefix, fields));
    }

    protected abstract MIScoreProperty getMIScoreproperty(Properties properties, String prefix, String[] fields);

    protected Double getValue(String id, Map<String, MIScoreProperty> map) {
        MIScoreProperty miScoreProperty = map.get(id);
        if (miScoreProperty != null) {
            return miScoreProperty.getScore();
        }
        else {
            for (String father : this.miOntology.getParents(id)) {
                if (map.get(father) != null) {
                    return map.get(father).getScore();
                }
            }
        }
        return map.get("unknown").getScore();
    }

    protected Double getMethodValue(String id) {
        return getValue(id, this.methodsValues);
    }

    protected Double getTypeValue(String id) {
        return getValue(id, this.typesValues);
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected Map<String, MIScoreProperty> methodsValues;
    protected Map<String, MIScoreProperty> typesValues;
    protected double methodWeight;
    protected double typeWeight;
    protected Collection<String> methods;
    protected Collection<String> types;
    protected MIOntology miOntology;
}
