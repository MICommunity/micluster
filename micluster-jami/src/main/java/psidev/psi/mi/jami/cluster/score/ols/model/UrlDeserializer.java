package psidev.psi.mi.jami.cluster.score.ols.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlDeserializer extends JsonDeserializer<URL> {

    private Pattern urlPrefix = Pattern.compile("^(https?://|ftp://).*");

    @Override
    public URL deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);
        String stringUrl = node.asText();
        if (!urlPrefix.matcher(stringUrl).matches()) {
            return new URL("http://" + stringUrl);
        } else {
            return new URL(stringUrl);
        }
    }

}
