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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import psidev.psi.mi.tab.model.BinaryInteraction;

/**
 * This class controls the way jobs are submited
 */
public enum MergerManager {
	INSTANCE;
	
	@Value("${max.running.time}")
	private long maxRunning;
	
	@Value("${max.queueing.time}")
	private long maxQueueing;
	
	//Max Number of jobs at a given time
	@Value("${max.jobs}")
	private int maxJobs = 200;
	
	//App Context
	private WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	private JobLauncher jobLauncher = context.getBean(JobLauncher.class);
	private Job job = context.getBean(Job.class);
	
	//Map that contains the executions
	private Map<String, JobExecution> executions = new ConcurrentHashMap<String, JobExecution>();
	
	/**
	 * Starts a new job
	 * @param params
	 * @return
	 */
	public String submitJob(JobParametersBuilder params){
		if(maxJobs - getActiveExecutionsCount() > 0){
			try {
				String id = UUID.randomUUID().toString();
				
				params.addString("id", id);
				JobExecution ex = jobLauncher.run(job, params.toJobParameters());
				executions.put(id, ex);
				
				return id;
			} catch (JobExecutionAlreadyRunningException e) {
				e.printStackTrace();
			} catch (JobRestartException e) {
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				e.printStackTrace();
			}
		}
		return "Server bussy, please try agan later";
	}
	
	/**
	 * Returns the status of a given job
	 * @param id
	 * @return
	 */
	public String getStatus(String id){
		JobExecution ex = executions.get(id);
		if(ex != null){
			BatchStatus st = ex.getStatus();
			String status = "";
			if(BatchStatus.FAILED == st){
				status = "Clustering job aborted.";// Please make sure the given parameters comply with the specification.";
				
				List<Throwable> lst = ex.getAllFailureExceptions();
				for(Throwable e : lst){
					e.printStackTrace();
					status += "\n "+e.getMessage();
				}
			}else if(ex.getExecutionContext().containsKey("status")){
				status = ex.getExecutionContext().getString("status");
			}
			return st.name() + " - " + status;
		}
		
		if(FileManager.INSTANCE.getTaskFile(id) != null)
			return "COMPLETED - clustered job '"+id+"'";
		
		return "Unknown job '"+id+"'";
	}
	
	/**
	 * Job clearance
	 */
	public void checkExecutions(){
		for(String key : executions.keySet()){
			JobExecution ex = executions.get(key);
			
			//if something has been running for more than one day, then is killed
			if(ex.getStatus() == BatchStatus.STARTED){
				long milis = (new Date().getTime() - ex.getStartTime().getTime());
				if(milis > maxRunning){
					ex.stop();
				}
			//if a job is complete and has been around form more than a week, then is stoped and removed from the 
			//execution list
			}else{
				long milis = (new Date().getTime() - ex.getStartTime().getTime());
				if(milis > maxQueueing){
					ex.stop();
					executions.remove(key);
					
					//if completed remove file as well
					if(ex.getStatus() == BatchStatus.COMPLETED){
						FileManager.INSTANCE.deleteFile(key);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the number of executions currently running
	 * @return
	 */
	private int getActiveExecutionsCount(){
		int count = 0;
		for(String key : executions.keySet()){
			JobExecution ex = executions.get(key);
			if(ex.isRunning()){
				count ++;
			}
		}
		return count;
	}
} 
