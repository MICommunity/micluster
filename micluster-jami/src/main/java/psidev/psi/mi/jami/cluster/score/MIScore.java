package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 18/07/2014.
 */
public interface MIScore {
    public double getMethodsScore();
    public double getTypesScore();
    public double getPublicationsScore();
    public void addMethod(String methodId);
    public void addType(String typeId);
    public void addPublication(String pubId);
    public void setMethodWeight(double weight);
    public void setTypeWeight(double weight);
    public void setPublicationWeight(double weight);
    public void clear();
}
