package psidev.psi.mi.jami.cluster.score.ols;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.log4j.Logger;
import psidev.psi.mi.jami.cluster.score.ols.model.OLSResult;
import psidev.psi.mi.jami.cluster.score.ols.model.SimpleOLSNode;
import psidev.psi.mi.jami.cluster.score.ols.model.UrlDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that uses the MIONode to build in memory
 * an Ontology. To build it can use the JSON got
 * it from the OLS or from
 * the file "psimiOntology.json", generated with
 * miscore package.
 * <p>
 * Created by maitesin on 03/03/2015.
 */
public class MIOntology {

    /************************/
    /***   Constructors   ***/
    /************************/
    public MIOntology() {
        this(true);
    }

    public MIOntology(boolean useOls) {
        this.map = new HashMap<>();
        this.root = null;
        this.useOLS = useOls;
        if (!useOls) loadFromFile();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public List<String> getParents(String id) {
        if (useOLS) return getAncestors(id);
        return new ArrayList<>(processParents(id, new LinkedHashSet<>()));
    }

    /***************************/
    /***   Private Methods   ***/
    /***************************/

    private Set<String> processParents(String id, Set<String> parents) {
        MIONode node = this.map.get(id);
        if (node == null) return parents;

        for (MIONode parent : node.getParents()) {
            parents.add(parent.getId());
            processParents(parent.getId(), parents);
        }

        return parents;
    }

    private List<String> getAncestors(String id) {
        List<String> ancestors;
        try {
            URL url = new URL("https://www.ebi.ac.uk/ols/api/ontologies/mi/terms/" +
                    "http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252F" + id.replace(":", "_") + "/hierarchicalAncestors");
            OLSResult result = mapper.readValue(url, OLSResult.class);
            ancestors = result.get_embedded()
                    .getTerms().stream()
                    .map(OLSResult.Term::getObo_id)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            while (result.get_links().containsKey(OLSResult.Link.Type.next)) {
                mapper.readValue(result.get_links().get(OLSResult.Link.Type.next).getHref(), OLSResult.class);
                ancestors.addAll(result.get_embedded()
                        .getTerms().stream()
                        .map(OLSResult.Term::getObo_id)
                        .collect(Collectors.toList())
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ancestors;
    }

    private void loadFromFile() {
        InputStream resource = MIOntology.class.getClassLoader().getResourceAsStream(olsFile);
        if (resource == null) return;
        try {
            SimpleOLSNode json = mapper.readValue(resource, SimpleOLSNode.class);
            if (json != null) {
                buildTree(json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildTree(SimpleOLSNode node) {
        this.root = processNode(node);
    }

    private MIONode processNode(SimpleOLSNode simpleNode) {
        MIONode node = this.map.computeIfAbsent(simpleNode.getId(), key -> new MIONode(key, simpleNode.getName()));
        if (simpleNode.getChildren() != null) {
            for (SimpleOLSNode child : simpleNode.getChildren()) {
                processNode(child).addParent(node);
            }
        }
        return node;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private MIONode root;
    private final Map<String, MIONode> map;

    private final boolean useOLS;
    private static final String olsFile = "psimiOntology.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule().addDeserializer(URL.class, new UrlDeserializer()))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Logger log = Logger.getLogger(MIOntology.class);
}
