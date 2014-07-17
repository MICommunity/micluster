package psidev.psi.mi.jami.cluster.io.input;

import psidev.psi.mi.jami.datasource.InteractionStream;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.tab.extension.datasource.DefaultMitabSource;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by maitesin on 12/06/2014.
 */
public class MitabClusterInput extends AbstractClusterInput {

    /************************/
    /***   Constructors   ***/
    /************************/
    public MitabClusterInput(InputStream input) {
        super();
        Map<String, Object> parsingOptions = optionfactory.getDefaultMitabOptions(input);
        InteractionStream stream = dataSourceFactory.getInteractionSourceWith(parsingOptions);
        this.interactionIterator = stream.getInteractionsIterator();
    }

    /**************************/
    /***   Public Methods   ***/
    /**************************/
    @Override
    public Interaction next(){ return this.interactionIterator.next();  }

    @Override
    public boolean hasNext() {
        return this.interactionIterator.hasNext();
    }

    /******************************/
    /***   Private Attributes   ***/
    /******************************/
    private Iterator<Interaction> interactionIterator;

}
