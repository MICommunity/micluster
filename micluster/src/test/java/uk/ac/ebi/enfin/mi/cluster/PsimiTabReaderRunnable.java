package uk.ac.ebi.enfin.mi.cluster;

import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anjali on 10/01/17.
 */
public class PsimiTabReaderRunnable implements Runnable {

    private String protienId;
    private URL url;
    private ConcurrentHashMap<String,List<BinaryInteraction>> protienInteractionsMap;

    public PsimiTabReaderRunnable(String protienId,URL url,ConcurrentHashMap<String,List<BinaryInteraction>> protienInteractionsMap){
        this.protienId=protienId;
        this.setUrl(url);
        this.setProtienInteractionsMap(protienInteractionsMap);
    }


    public void run() {

    //    System.out.println("Thread no"+Thread.currentThread().getName()+"Id is "+ protienId);

       // HashMap<String,List<BinaryInteraction>> protienInteractionsMap=new HashMap<String, List<BinaryInteraction>>();
        List<BinaryInteraction> binaryInteractionList=new ArrayList<BinaryInteraction>();
        try {
            PsimiTabReader mitabReader = new PsimiTabReader();

            binaryInteractionList.addAll(mitabReader.read(getUrl()));
            synchronized (this) {
                getProtienInteractionsMap().put(protienId, binaryInteractionList);
            }
        }catch(Exception e){

        }
       // return protienInteractionsMap;
    }


    public String getProtienId() {
        return protienId;
    }

    public void setProtienId(String protienId) {
        this.protienId = protienId;
    }


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }


    public ConcurrentHashMap<String, List<BinaryInteraction>> getProtienInteractionsMap() {
        return protienInteractionsMap;
    }

    public void setProtienInteractionsMap(ConcurrentHashMap<String, List<BinaryInteraction>> protienInteractionsMap) {
        this.protienInteractionsMap = protienInteractionsMap;
    }
}
