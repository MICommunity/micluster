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

import org.springframework.stereotype.Component;

/**
 * Task that runs the execution cleanance
 */
@Component
public class RunMeTask{

	private MergerManager manager;

	public RunMeTask(MergerManager manager) {
		this.manager = manager;
	}

	public void printMe() {
		this.manager.checkExecutions();
	}
}