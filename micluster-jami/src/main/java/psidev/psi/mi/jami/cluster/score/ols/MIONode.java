package psidev.psi.mi.jami.cluster.score.ols;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is used to build the ontology.
 * <p>
 * Created by maitesin on 03/03/2015.
 */
public class MIONode {
    /************************/
    /***   Constructors   ***/
    /************************/
    public MIONode(String id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
    }

    /*******************************/
    /***   Getters and Setters   ***/
    /*******************************/

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MIONode> getChildren() {
        return children;
    }

    public List<MIONode> getParents() {
        return parents;
    }

    public void addParent(MIONode parent) {
        this.parents.add(parent);
        parent.children.add(this);
    }

    public void addChild(MIONode child) {
        this.children.add(child);
        child.parents.add(this);
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private final String id;
    private final String name;
    private final List<MIONode> children;
    private final List<MIONode> parents;
}
