/**
 * Copyright (C) 20012-2013 Jose Villaveces Max Planck Institute for Biology of
 * Ageing
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.mpi.age.mimerge.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import psidev.psi.mi.tab.PsimiTabException;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;

import uk.ac.ebi.enfin.mi.cluster.ClusterServiceException;
import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
import uk.ac.ebi.enfin.mi.cluster.EncoreBinaryInteraction;
import uk.ac.ebi.enfin.mi.cluster.InteractionCluster;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;

/**
 * This tasklet does the clustering
 */
public class MergerTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(MergerTasklet.class);
	
	private String id, query, mappingIds="uniprotkb,intact,ddbj/embl/genbank,chebi,irefindex,hgnc,ensembl";
	private String[] services, serviceurls;
	private Map<String,Float> methods, types;
	private int pubnumber = -1;
	private boolean score = false;
	private ExecutionContext exContext;
	private File file;
	
	private InteractionCluster iCluster;
	
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		
		final Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
		exContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		
		query = (params.get("q") == null) ? null : (String) params.get("q");
		services = (params.get("service") == null) ? null : params.get("service").toString().split(",");
		serviceurls = (params.get("serviceurl") == null) ? null : params.get("serviceurl").toString().split(",");
		file = (params.get("file") == null) ? null : FileManager.INSTANCE.getFile(params.get("file").toString());
		id = (String) params.get("id");
		
		if(params.get("mapping") != null)
			mappingIds = (String) params.get("mapping");
		
		if(params.get("score") != null)
			score = Boolean.parseBoolean((String) params.get("score"));
		
		if(params.get("methods") != null && params.get("types") != null && params.get("pubnumber") != null){
			this.methods = mapList((String) params.get("methods"));
			this.types = mapList((String) params.get("types"));
			this.pubnumber = (Integer) params.get("pubnumber");
		}
		
		if(id == null){
			exContext.put("status", "Parameter 'id' must be defined");
			throw new IllegalArgumentException("Parameter 'id' must be defined");
		}
		
		if(query == null && serviceurls == null && file == null){
			exContext.put("status",  "Either 'q' or 'serviceurl' or 'file' parameter must be defined");
			throw new IllegalArgumentException("Either 'q' or 'serviceurl' or 'file' parameter must be defined");
		}
		
		if(query != null && services == null){
			exContext.put("status",  "Parameter 'service' must be defined");
			throw new IllegalArgumentException("Parameter 'service' must be defined");
		}
		
		cluster();
		
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * Clusters the interactions
	 * @throws ClusterServiceException
	 * @throws IOException
	 * @throws ConverterException
	 * @throws PsimiTabException
	 */
	private void cluster() throws ClusterServiceException, IOException, ConverterException, PsimiTabException{
		Date start = new Date();
		exContext.put("status",  "Clustering running");
		
		if(score){
			iCluster = new InteractionClusterScore();
			((InteractionClusterScore) iCluster).setScoreName("miscore");
			
			if(methods != null && types != null && pubnumber != -1){
				((InteractionClusterScore) iCluster).setScoreName("presonalized_score");
				((InteractionClusterScore) iCluster).setCustomOntologyTypeScores(types);
				((InteractionClusterScore) iCluster).setCustomOntologyMethodScores(methods);
				((InteractionClusterScore) iCluster).setCustomPublicationNumberWithHighestScore(pubnumber);
			}
		}else{
			iCluster = new InteractionCluster();
		}
		
		iCluster.setMappingIdDbNames(mappingIds);
		
		if(services != null){
	        iCluster.addMIQLQuery(query);
	        
	        if(services.length==1 && services[0].equalsIgnoreCase("all")){
	        	iCluster.setQuerySourcesFromPsicquicRegistry();
	        }else{
	        	iCluster.setQuerySources(Arrays.asList(services));
	        }
		}else if(file != null){
			File[] fArr = new File[1];
			fArr[0] = file; 
			
	        iCluster.setBinaryInteractionIterator(fArr, false);
		}else{
			PsimiTabReader mitabReader = new PsimiTabReader();
	        List<BinaryInteraction> binaryInteractions = new ArrayList<BinaryInteraction>();
	        
	        for(String urlStr : serviceurls){
				URL url = new URL(urlStr);
				binaryInteractions.addAll(mitabReader.read(url));
	        }
			iCluster.setBinaryInteractionIterator(binaryInteractions.iterator());
		}
		
		/* Run Cluster */
		iCluster.runService();
		
		ArrayList<BinaryInteraction> interactions = new ArrayList<BinaryInteraction>();
	    Encore2Binary iConverter = new Encore2Binary(iCluster.getMappingIdDbNames());
	    for(EncoreBinaryInteraction eI : iCluster.getInteractionMapping().values()){
	    	interactions.add(iConverter.getBinaryInteraction(eI));
	    }
	    
	    
	    FileManager.INSTANCE.saveToFile(id, interactions);
        
	    exContext.put("status",  "Clustering finished after "+getTimeDiff(start, new Date()));
        
	}
	
	/**
	 * Transforms a methods or types string into a map with the ontology id 
	 * and the float value associated to that term.
	 * @param lstStr
	 * @return
	 */
	private Map<String, Float> mapList(String lstStr){
		Map<String, Float> map = new HashMap<String, Float>();
		
		String[] lst = lstStr.split(",");
		if(lstStr != ""){
			String key = "";
			boolean isKey = true;
			for(int i=0; i<lst.length; i++){
				if(isKey){
					key = lst[i];
					isKey = false;
				}else{
					try{
						float value = Float.parseFloat(lst[i]);
						map.put(key, value);
						isKey = true;
					}catch(NumberFormatException e){
						exContext.put("status", "Couldn't parse the value "+lst[i]+" it is not of type float.");
						throw new IllegalArgumentException("Couldn't parse the value "+lst[i]+" it is not of type float.");
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * Calculates the difference between to dates in order to report 
	 * when the cluster is done.
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	private String getTimeDiff(Date dateOne, Date dateTwo) {
        String diff = "";
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(hours);
        long secconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        
        diff = String.format("%d hour(s) %d min(s) %d seccond(s)", hours, minutes, secconds);
        return diff;
	}
}
