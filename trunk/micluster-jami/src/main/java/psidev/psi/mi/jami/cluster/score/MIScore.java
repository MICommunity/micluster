package psidev.psi.mi.jami.cluster.score;

/**
 * Created by maitesin on 18/07/2014.
 */
public interface MIScore {
    /**
     *
     * @return
     */
    public double getMethodsScore();

    /**
     *
     * @return
     */
    public double getTypesScore();

    /**
     *
     * @return
     */
    public double getPublicationsScore();

    /**
     *
     * @param methodId
     */
    public void addMethod(String methodId);

    /**
     *
     * @param typeId
     */
    public void addType(String typeId);

    /**
     *
     * @param pubId
     */
    public void addPublication(String pubId);

    /**
     *
     * @param weight
     */
    public void setMethodWeight(double weight);

    /**
     *
     * @param weight
     */
    public void setTypeWeight(double weight);

    /**
     *
     * @param weight
     */
    public void setPublicationWeight(double weight);

    /**
     *
     */
    public void clear();
}
