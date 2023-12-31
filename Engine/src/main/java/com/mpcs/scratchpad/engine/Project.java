package com.mpcs.scratchpad.engine;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.mpcs.logging.Logger;
import java.nio.file.Path;


public class Project {
	private Path projectPath;
	private String projectName;
	private String initialSceneName;

	public Project(Path projectPath, String projectName, String initialSceneName) {	
		this.projectPath = projectPath;
		this.projectName = projectName;
		this.initialSceneName = initialSceneName;
	}

	public String getInitialSceneName() {
		return initialSceneName;
	}
}