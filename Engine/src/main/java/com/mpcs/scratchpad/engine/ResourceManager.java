package com.mpcs.scratchpad.engine;
import com.mpcs.scratchpad.engine.Project;
import java.io.File;
import com.mpcs.logging.Logger;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import com.mpcs.scratchpad.engine.scene.ogscene.OGScene;
import java.io.IOException;
import com.mpcs.scratchpad.engine.scene.Scene;

public class ResourceManager {

	public static final String PROJECT_FILE_NAME = "project.scratchpad";

	private Path projectDirectory;
	private Project currentProject;

	public ResourceManager() {

	}

	public ResourceManager(String projectPath) throws IOException {
		loadProject(projectPath);
	}

	public Project loadProject(String projectPath) throws IOException {
		projectDirectory = Paths.get(projectPath);
		Path projectDescriptorFile = projectDirectory.resolve(PROJECT_FILE_NAME);
		//Logger.log(projectDirectory.listFiles().toString());
		Map<String, String> projectProperties = separateKeyValue(Files.readAllLines(projectDescriptorFile, StandardCharsets.UTF_8), ":");
		String projectName = projectProperties.get("name");
		String initialScene = projectProperties.get("initialScene");
		Project project = new Project(projectDirectory, projectName, initialScene);
		this.currentProject = project;
		return project;
	}

	private Map<String, String> separateKeyValue(List<String> lines, String separator) {
		Map<String, String> pairs = new HashMap();
		for (String line : lines) {
			String[] elements = line.split(separator);
			if (elements.length != 2) {
				throw new RuntimeException("Error loading project descriptor.");
			}
			pairs.put(elements[0].strip(), elements[1].strip());
		}
		return pairs;
	}

	public Scene loadInitialScene() {
		Scene initialScene = loadScene(currentProject.getInitialSceneName());
		return initialScene;
	}

	public Scene loadScene(String sceneName) {
		return new OGScene();
	}
}
