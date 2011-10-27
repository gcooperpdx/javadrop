/*******************************************************************************
 * Copyright 2011 gregorycooper
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.javadrop.runner.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



/**
 * This implementation of the runner strategy is designed to support stand-alone
 * java client programs that have their own 'main(..)'.
 * 
 * @author gregorycooper
 *
 */
public class JavaAppStrategy extends BaseRunnerStrategy {
	
	@Override
	public Map<File, File> getConversionFiles(File outputDirectory) {
		Map<File,File> conversionFiles = new HashMap<File, File>(); //super.getConversionFiles(outputDirectory, serviceName);

		conversionFiles.put(new File(getPrefix() + File.separator + "bin" + File.separator + "java_app_sh.vm"),
				new File(outputDirectory + File.separator + "runners" + File.separator + "bin" + File.separator + getAppName() + ".sh"));
		
		return conversionFiles;
	}

	@Override
	public Map<File, Collection<File>> getInstallSet(File workingDirectory) {
		Map<File, Collection<File>> installSet = super.getInstallSet(workingDirectory);
		Collection<File> installFiles = new ArrayList<File>();
		installFiles.add(new File(getAppName() + ".sh"));
		installSet.put(new File("runners" + File.separator + "bin"), installFiles);

		// Lib files
		Collection<File> libFiles = getDirFiles(new File(workingDirectory.getAbsolutePath() + File.separator + "lib"));
		if (libFiles.size() > 0) installSet.put(new File("lib"), libFiles);
			
		return installSet;
	}

	protected String getAppName() {
		return runnerVariables.get("APP_NAME");
	}

	private Collection<File> getDirFiles(File dir) {
		
		ArrayList<File> fileList = new ArrayList<File>();

		File [] dirList = dir.listFiles();
		if (dirList == null) {
			get_log().warn("Directory is missing or empty: " + dir.getAbsolutePath());
			return fileList;
		}
		for (File file : dirList) {
			fileList.add(file);
		}
		return fileList;
	}
	
	
	@Override
	protected void applyDefaults() {
		super.applyDefaults();
		runnerVariables.put("APP_NAME", "java_app");
		requiredVariables.add("RUNNER_MAIN_CLASS");
	}
	
	private String getPrefix() {
		return "org" + File.separator + "javadrop" + File.separator + "runnerstrategy" + File.separator + "java_app";
	}

}
