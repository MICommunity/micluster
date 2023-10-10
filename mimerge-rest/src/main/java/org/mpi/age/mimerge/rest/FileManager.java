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
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.xml.converter.ConverterException;

/**
 * Manages the files produced by the application.
 */
public enum FileManager {
	INSTANCE;
	
	private static final String FILE_DIR = "webapps/mimerge/data/", TSV = ".tsv";
	private static final long MAX_SIZE = 5000000;//5 MB
	
	/**
	 * Saves a set of binary interactions into a MITAB file.
	 * @param taskId
	 * @param interactions
	 * @throws IOException
	 * @throws ConverterException
	 */
	public void saveToFile(String taskId, ArrayList<BinaryInteraction> interactions) throws IOException, ConverterException{
		File file = new File(FILE_DIR+taskId+TSV);
		if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();
		file.createNewFile();
		FileOutputStream fop = new FileOutputStream(file);
	   
	    /* Print PSI MITAB clustered binary interactions */
        PsimiTabWriter writer = new PsimiTabWriter();
        writer.write(interactions, fop);
        
	}
	
	/**
	 * Deletes a file
	 * @param taskId
	 * @return
	 */
	public boolean deleteFile(String taskId){
		File file = new File(FILE_DIR+taskId+TSV);
		return file.delete();
	}
	
	/**
	 * Creates a temporal file from a MultipartFile.
	 * @param mFile
	 * @return
	 * @throws IOException 
	 */
	public String createTempFile(MultipartFile mFile) throws IOException{
		
		if(mFile.getSize() == 0){
			return "No file to cluster";
		}
		
		if(mFile.getSize() > MAX_SIZE){
			return "File too big";
		}
		
		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + mFile.getOriginalFilename());
		file.createNewFile();
		try {
			mFile.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file.getAbsolutePath();
	}
	
	/**
	 * Returns a file asociated to a particular job
	 * @param taskId
	 * @return
	 */
	public File getTaskFile(String taskId){
		return getFile(FILE_DIR+taskId+TSV);
	}
	
	/**
	 * Returns the file stored in the given path
	 * @param path
	 * @return
	 */
	public File getFile(String path){
		File f = new File(path);
		if(f.exists())
			return f;
		return null;
	}
}
