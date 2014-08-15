package psidev.psi.mi.jami.cluster.merge;

import psidev.psi.mi.jami.enricher.impl.CompositeInteractorEnricher;
import psidev.psi.mi.jami.enricher.impl.minimal.MinimalInteractorBaseEnricher;
import psidev.psi.mi.jami.model.Interactor;

/**
 * Created by maitesin on 31/07/2014.
 */
public abstract class AbstractInteractorMerger implements InteractorMerger{
    private CompositeInteractorEnricher enricher;

    public CompositeInteractorEnricher getEnricher() {
        if(this.enricher == null){
            this.enricher = new CompositeInteractorEnricher(new MinimalInteractorBaseEnricher<Interactor>());
        }
        return enricher;
    }

    public void setEnricher(CompositeInteractorEnricher enricher) {
        if (enricher != null) {
            this.enricher = enricher;
        }
    }
}
