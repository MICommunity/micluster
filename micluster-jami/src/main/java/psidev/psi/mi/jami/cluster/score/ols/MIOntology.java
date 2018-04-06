package psidev.psi.mi.jami.cluster.score.ols;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by anjali on 04/04/18.
 */
public class MIOntology {

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private MIONode root;
    private Map<String, MIONode> map;
    private final static String olsUrl = "https://www.ebi.ac.uk/ols/api/ontologies/mi/terms?obo_id=MI:0000";
    private final static String olsFile = "psimiOntology.json";
    private Logger logger = Logger.getLogger(MIOntology.class);
    private static MIOntology miOntology;


    private MIOntology(boolean useOls) {
        this.map = new HashMap<String, MIONode>();
        this.root = null;
        if (useOls) {
            loadFromOls();
        } else {
            loadFromFile();
        }
    }

    public static MIOntology getInstance(boolean useOls) {
        if (miOntology == null) {
            miOntology = new MIOntology(useOls);
        }
        return miOntology;
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

        //  String jsonQuery = "http://www.ebi.ac.uk/ols/v2/json/termchildren?termId="+parentTerm+"&ontology=MI&depth=1000";
        // String formattedParentTerm = parentTerm.replaceAll(":", "_");
        //  String jsonQuery = "http://www.ebi.ac.uk/ols/api/ontologies/mi/terms/http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252F" + formattedParentTerm + "/descendants";
        String jsonQuery = olsUrl;
        String jsonText = "";
        String descendantUrl = "";
        try {
            jsonText = getJsonForUrl(jsonQuery);// mainQry
            if (jsonText.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonText);
                JsonNode embedded = jsonNode.get("_embedded");
                JsonNode termChildren = embedded.get("terms");

                String id = "";
                String value = "";
                boolean hasChildren = false;

                for (final JsonNode objNode : termChildren) {
                    id = objNode.get("obo_id").textValue();
                    value = objNode.get("label").textValue();
                    hasChildren = objNode.get("has_children").booleanValue();

                    MIONode parent = new MIONode(id, value);
                    this.map.put(id, parent);
                    if (hasChildren) {

                        if (objNode.get("has_children").booleanValue()) {
                            String childrenUrl = objNode.get("_links").get("hierarchicalChildren").get("href").textValue();

                           if (childrenUrl != null && !childrenUrl.trim().equals("")) {
                                String childrenJson = getJsonForUrl(childrenUrl);
                                if (childrenJson.length() > 0) {
                                    JsonNode descendantJsonNode = mapper.readTree(childrenJson);
                                    if (descendantJsonNode != null) {
                                        JsonNode embedded_ = descendantJsonNode.get("_embedded");
                                        JsonNode termChildren_ = embedded_.get("terms");
                                        processChildrenForOls(parent, termChildren_);
                                    }
                                }
                            }
                        }
                    }
                }
                //mapIdName.put(termId,termName); // Include the parentTerm


            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


    /**
     * Gets Json text from the given query url
     *
     * @param jsonQuery
     * @return
     */
    public String getJsonForUrl(String jsonQuery) {
        String jsonText = "";
        try {
            URL url = new URL(jsonQuery);
            URLConnection olsConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(olsConnection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                //jsonText += inputLine;
                builder.append(inputLine);
                /*in.close();
                break;*/
            }
            jsonText = builder.toString();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return jsonText;
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

    private void processChildrenForOls(MIONode father, JsonNode terms) {
        ObjectMapper mapper = new ObjectMapper();
        String childrenUrl = "";
        MIONode child = null;

        try {
        for (final JsonNode objNode : terms) {

            String id = objNode.get("obo_id").textValue();
            String value = objNode.get("label").textValue();

            child = new MIONode(id, value, father);


            if (objNode.get("has_children").booleanValue()){
                childrenUrl = objNode.get("_links").get("hierarchicalChildren").get("href").textValue();
                if (childrenUrl != null && !childrenUrl.trim().equals("")) {

                    String childrenJson = getJsonForUrl(childrenUrl);

                    if (childrenJson.length() > 0) {
                        JsonNode descendantJsonNode = mapper.readTree(childrenJson);
                        if (descendantJsonNode != null) {

                            JsonNode embedded = descendantJsonNode.get("_embedded");
                            JsonNode termChildren = embedded.get("terms");

                            processChildrenForOls(child, termChildren);
                        }
                    }

                }


        }

            map.put(id, child);
            father.getChildren().add(child);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
