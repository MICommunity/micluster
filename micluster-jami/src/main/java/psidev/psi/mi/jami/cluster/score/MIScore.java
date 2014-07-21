package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 18/07/2014.
 */
public interface MIScore {
    public Double getMethodScore(String methodId);
    public Double getTypeScore(String typeId);
    public Double getPublicationScore(String pubmed);
    public void setMethodWeight(Double weight);
    public void setTypeWeight(Double weight);
    public void setPublicationWeight(Double weight);
}
