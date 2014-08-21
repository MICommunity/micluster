package psidev.psi.mi.jami.cluster;

import psidev.psi.mi.jami.cluster.model.InteractionCluster;
import psidev.psi.mi.jami.cluster.merge.InteractorMerger;
import psidev.psi.mi.jami.cluster.model.Interactor2Interactions;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.xref.DefaultExternalIdentifierComparator;
import psidev.psi.mi.jami.utils.comparator.xref.DefaultXrefComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

import java.util.*;

/**
 * Created by maitesin on 25/07/2014.
 */
public abstract class AbstractInteractionClusterManager<I extends Interaction,T extends InteractionCluster<I>> implements InteractionClusterManager<I,T> {

    /********************************/
    /***   Abstract Constructor   ***/
    /********************************/
    public AbstractInteractionClusterManager(){
        this.id2Interactor = new TreeMap<Xref, Interactor2Interactions>(new UnambiguousXrefComparator());
        this.idGenerator = 0L;
        this.interaction2InteractionCluster = new HashMap<Interaction, T>();
        this.interactionClusters = new ArrayList<T>();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    public void clear(){
        this.id2Interactor.clear();
        this.idGenerator = 0L;
        this.interactionClusters.clear();
    }

    /*****************************/
    /***   Protected Methods   ***/
    /*****************************/
    protected Long getNextId(){
        return ++this.idGenerator;
    }

    /********************************/
    /***   Protected Attributes   ***/
    /********************************/
    protected Map<Xref, Interactor2Interactions> id2Interactor;
    protected Long idGenerator;
    protected Map<Interaction, T> interaction2InteractionCluster;
    protected Collection<T> interactionClusters;
    protected InteractorMerger merger = null;
}
