package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.rendering.mesh.ArrayMesh3D;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.obj.ObjFile;
import com.mpcs.scratchpad.core.resources.obj.VertexDataFormat;
import com.mpcs.scratchpad.core.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {

	public static final String PROJECT_FILE_NAME = "project.scratchpad";

	private Path projectDirectory;
	private Project currentProject;
	public ResourceManager(String projectPath) throws IOException {
		loadProject(projectPath);
	}

	public Project loadProject(String projectPath) throws IOException {
		projectDirectory = Paths.get(projectPath);
		Path projectDescriptorFile = projectDirectory.resolve(PROJECT_FILE_NAME);
		Map<String, String> projectProperties = separateKeyValue(Files.readAllLines(projectDescriptorFile, StandardCharsets.UTF_8), ":");
		String projectName = projectProperties.get("name");
		String initialScene = projectProperties.get("initialScene");
		Project project = new Project(projectDirectory, projectName, initialScene);
		this.currentProject = project;
		return project;
	}

	private Map<String, String> separateKeyValue(List<String> lines, String separator) {
		Map<String, String> pairs = new HashMap<>();
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
		return loadScene(currentProject.getInitialSceneName() + ".scene");
	}

	public Scene loadScene(String sceneName) {
		SceneFileParser sceneFileParser = new SceneFileParserImpl2(projectDirectory.resolve(sceneName));
		//return new OGScene();
		return sceneFileParser.toScene();// new OGScene();
	}

	public Image getResourceImage(String resourceName) throws IOException {
		Path resourcePath = projectDirectory.resolve(resourceName);

		BufferedImage image;
		image = ImageIO.read(resourcePath.toFile());
		image = flipImageHorizontally(image);
		return new Image(image);
	}

	public Mesh3D getResourceMesh(String resourceName) throws IOException {
		ObjFile objModel = ObjFile.loadFromFile(projectDirectory.resolve(resourceName));
		return new ArrayMesh3D(objModel.getTriangulatedVerticesFormatted(VertexDataFormat.POSITION_TEXTURE));
	}

	private BufferedImage flipImageHorizontally(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}

}
