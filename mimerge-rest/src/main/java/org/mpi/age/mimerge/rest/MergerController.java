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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MergerController {
	
	private static final Logger logger = LoggerFactory.getLogger(MergerController.class);
	
	
	/**
	 * Returns a clustered file to download
	 * @param taskId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/download/{taskId}", method = RequestMethod.GET)
	public String download(@PathVariable String taskId, Model model) {
		File file = FileManager.INSTANCE.getTaskFile(taskId);
		
		model.addAttribute("file", file);
		model.addAttribute("msg", "No file matching job id '"+taskId+"' was found. It may have been deleted.");
		return "FileRenderer";
	}
	
	/**
	 * Returns the status of a cluster job
	 * @param taskId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/status/{taskId}", method = RequestMethod.GET)
	public String status(@PathVariable String taskId, Model model) {
		model.addAttribute("msg", MergerManager.INSTANCE.getStatus(taskId));
		return "plaintext";
	}
	
	/**
	 * Starts a cluster job from a particular URL or a set of PSIQCUIC services. 
	 * It returns the job id
	 * @param query
	 * @param services
	 * @param serviceurls
	 * @param mappingIds
	 * @param types
	 * @param methods
	 * @param pubnumber
	 * @param score
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/cluster", method = RequestMethod.GET)
	public String cluster(@RequestParam(value="q", required=false) String query, 
			@RequestParam(value="service", required=false) String services, 
			@RequestParam(value="serviceurl", required=false) String serviceurls,
			@RequestParam(value="mapping", required=false) String mappingIds,
			@RequestParam(value="types", required=false) String types,
			@RequestParam(value="methods", required=false) String methods,
			@RequestParam(value="pubnumber", required=false) Double pubnumber,
			@RequestParam(value="score", required=false) boolean score, Model model) {
	
		JobParametersBuilder params = new JobParametersBuilder()
			.addString("q", query)
			.addString("service", services)
			.addString("serviceurl", serviceurls)
			.addString("mapping", mappingIds)
			.addString("types", types)
			.addString("methods", methods)
			.addDouble("pubnumber", pubnumber)
			.addString("score", score+"");
			
		String id = MergerManager.INSTANCE.submitJob(params);
		model.addAttribute("msg", id);
		return "plaintext";
	}
	
	/**
	 * Starts a cluster job form a MITAB file and returns a jobid
	 * @param file
	 * @param mappingIds
	 * @param types
	 * @param methods
	 * @param pubnumber
	 * @param score
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cluster", method = RequestMethod.POST)
	public String cluster(@RequestParam(value="file", required=false) MultipartFile file, 
			@RequestParam(value="mapping", required=false) String mappingIds,
			@RequestParam(value="types", required=false) String types,
			@RequestParam(value="methods", required=false) String methods,
			@RequestParam(value="pubnumber", required=false) Double pubnumber,
			@RequestParam(value="score", required=false) boolean score, Model model) {
		
		String msg = FileManager.INSTANCE.createTempFile(file);
		
		if(msg.contains("/")){
			JobParametersBuilder params = new JobParametersBuilder()
				.addString("mapping", mappingIds)
				.addString("types", types)
				.addString("methods", methods)
				.addDouble("pubnumber", pubnumber)
				.addString("score", score+"")
				.addString("file", msg);
			
			String id = MergerManager.INSTANCE.submitJob(params);
			model.addAttribute("msg", id);
		}else{
			model.addAttribute("msg", msg);
		}
		
		return "plaintext";
	}
}
