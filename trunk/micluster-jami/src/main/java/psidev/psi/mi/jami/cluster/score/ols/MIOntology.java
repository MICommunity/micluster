package psidev.psi.mi.jami.cluster.score.ols;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class that uses the MIONode to build in memory
 * an Ontology. To build it can use the JSON got
 * it from the ontology-lookup web site or from
 * the file "psimiOntology.json".
 *
 * Created by maitesin on 03/03/2015.
 */
public class MIOntology {

    /************************/
    /***   Constructors   ***/
    /************************/
    public MIOntology() {
        this(true);
    }

    public MIOntology(boolean useOls){
        this.map = new HashMap<String, MIONode>();
        this.root = null;
        if (useOls) {
            loadFromOls();
        }
        else {
            loadFromFile();
        }
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public List<String> getParents(String id) {
        List<String> parents = new ArrayList<String>();
        MIONode node = this.map.get(id);
        if (node != null) {
            while (node.getFather() != null) {
                parents.add(node.getId());
                node = node.getFather();
            }
        }
        return parents;
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/
    private void loadFromOls() {
        try {
            JSONObject json = getJsonFromOls();
            if (json != null) {
                buildTree(json);
            }
        } catch (IOException e) {
            if(log.isInfoEnabled()) {
                log.info("Unable to get the Json from the OLS", e);
            }
            e.printStackTrace();
        }

    }

    private JSONObject getJsonFromOls() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(olsUrl);
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            return (JSONObject) JSONValue.parse(reader);
        }
        else {
            if (log.isInfoEnabled()) {
                log.info("Unable to connect to: " + olsUrl);
                log.info("Will not load the ontology and only will use the values in the file.");
            }
        }
        return null;
    }

    private void loadFromFile() {
        JSONObject json = (JSONObject) JSONValue.parse(new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(olsFile))));
        if (json != null) {
            buildTree(json);
        }
    }

    private void buildTree(JSONObject json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        JSONArray children = (JSONArray) json.get("children");
        this.root = new MIONode(id, name);
        processChildren(this.root, children);
        this.map.put(id, this.root);
    }

    private void processChildren(MIONode father, JSONArray array) {
        Iterator<JSONObject> it = array.iterator();
        JSONObject json = null;
        String id = null, name = null;
        MIONode child = null;
        while (it.hasNext()) {
            json = it.next();
            id = (String) json.get("id");
            name = (String) json.get("name");
            child = new MIONode(id, name, father);
            if (json.get("children") != null) { //children exist in the json object
                processChildren(child, (JSONArray) json.get("children"));
            }
            father.getChildren().add(child);
            this.map.put(id, child);
        }
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private MIONode root;
    private Map<String, MIONode> map;
    private final static String olsUrl = "http://www.ebi.ac.uk/ontology-lookup/json/termchildren?termId=MI:0000&ontology=MI&depth=1000";
    private final static String olsFile = "psimiOntology.json";
    private Logger log = Logger.getLogger(MIOntology.class);
}
