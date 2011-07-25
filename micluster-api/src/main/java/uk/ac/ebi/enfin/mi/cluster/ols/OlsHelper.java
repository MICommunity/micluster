package uk.ac.ebi.enfin.mi.cluster.ols;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rafael
 * Date: 25-Aug-2010
 * Time: 09:54:00
 */

//todo: these class is redundant. Look at uk.ac.ebi.enfin.mi.score.ols.MIOntology Isolate this class to use it in both.
public class OlsHelper {
        static Logger logger = Logger.getLogger(OlsHelper.class);
        private Map<String,String> mapIdName;
        public Map<String,String> getJsonChildren(String parentTerm){
        mapIdName = new HashMap<String,String>();
        String jsonQuery = "http://www.ebi.ac.uk/ontology-lookup/json/termchildren?termId="+parentTerm+"&ontology=MI&depth=1000";
        String jsonText = "";
        try {
            URL url = new URL(jsonQuery);
            URLConnection olsConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(olsConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                jsonText += inputLine;
                in.close();
                break;
            }
            if(jsonText.length() > 0){
                JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonText);
                String termId = json.getString("id");
                String termName = json.getString("name");
                try{
                    JSONArray termChildren = json.getJSONArray("children");
                    //mapIdName.put(termId,termName); // Include the parentTerm
                    getMapIdNameFromJsonArray(termChildren);
                } catch (JSONException e){
                    logger.warn("No children for " + termId);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return mapIdName;
    }

    private void getMapIdNameFromJsonArray(JSONArray termChildren){
        for (int i = 0; i < termChildren.size(); ++i) {
            JSONObject child = termChildren.getJSONObject(i);
            String termId = child.getString("id");
            String termName = child.getString("name");
            mapIdName.put(termId,termName);
            try{
                JSONArray nextTermChildren = child.getJSONArray("children");
                if(nextTermChildren.size() > 0){
                    getMapIdNameFromJsonArray(nextTermChildren);
                }
            } catch (JSONException e){
                logger.debug("No children for " + termId);
                logger.debug(e.getMessage());
            }
        }
    }
}
