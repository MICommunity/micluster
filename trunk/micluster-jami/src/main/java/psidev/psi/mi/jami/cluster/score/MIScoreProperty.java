package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 23/07/2014.
 */
public class MIScoreProperty {

    public MIScoreProperty() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private String Id;
    private String name;
    private Double score;
}
