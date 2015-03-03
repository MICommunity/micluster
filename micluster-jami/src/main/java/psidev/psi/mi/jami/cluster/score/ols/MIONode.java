package psidev.psi.mi.jami.cluster.score.ols;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maitesin on 03/03/2015.
 */
public class MIONode {
    /************************/
    /***   Constructors   ***/
    /************************/
    public MIONode(String id, String name) {
        this(id, name, null);
    }
    public MIONode(String id, String name, MIONode father) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<MIONode>();
        this.father = father;
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

    public MIONode getFather() {
        return father;
    }

    public void setFather(MIONode father) {
        this.father = father;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private String id;
    private String name;
    private List<MIONode> children;
    private MIONode father;
}
