package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.service.EngineService;
import com.mpcs.scratchpad.core.service.ServicePriority;
import com.mpcs.scratchpad.core.rendering.mesh.ArrayMesh3D;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.obj.ObjFile;
import com.mpcs.scratchpad.core.resources.obj.VertexDataFormat;
import com.mpcs.scratchpad.core.scene.Scene;

import javax.imageio.ImageIO;
import javax.tools.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ResourceManager implements EngineService {

	public static final String PROJECT_FILE_NAME = "project.scratchpad";

	private Path projectDirectory;
	private Project currentProject;
	private String currentProjectPath;
	public ResourceManager(String projectPath) {
		this.currentProjectPath = projectPath;
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
		SceneFileParser sceneFileParser = new SceneFileParser(projectDirectory.resolve(sceneName));
		return sceneFileParser.toScene();
	}

	public Image getResourceImage(String resourceName) throws IOException {
		Path resourcePath = projectDirectory.resolve(resourceName);

		BufferedImage image;
		image = ImageIO.read(resourcePath.toFile());
		image = flipImageHorizontally(image);
		return new Image(image);
	}

	public Mesh3D getResourceMesh(String resourceName) throws IOException {
		Path resolvedPath= projectDirectory.resolve(resourceName);
		
		ObjFile objModel = ObjFile.loadFromFile(resolvedPath);
		return new ArrayMesh3D(objModel.getTriangulatedVerticesFormatted(VertexDataFormat.POSITION_TEXTURE));
	}

	private BufferedImage flipImageHorizontally(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}

	public Class<?> loadClass(String filePath) {
		Path fileResolvedPath = projectDirectory.resolve(filePath);
		File file = fileResolvedPath.toFile();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnostics, null, null);

		List<String> optionList = new ArrayList<String>();
		optionList.add("-classpath");
		optionList.add(System.getProperty("java.class.path") + File.pathSeparator + "dist/InlineCompiler.jar");
		optionList.add("-d");
		optionList.add(projectDirectory.resolve("build/").toString());
		Iterable<? extends JavaFileObject> compilationUnit
				= fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));
		JavaCompiler.CompilationTask task = javaCompiler.getTask(
				null,
				fileManager,
				diagnostics,
				optionList,
				null,
				compilationUnit);

		if (task.call()) {
			/** Load and execute *************************************************************************************************/
			//System.out.println("Yipe");
			// Create a new custom class loader, pointing to the directory that contains the compiled
			// classes, this should point to the top of the package structure!
			try {
				URLClassLoader classLoader = null;

				classLoader = new URLClassLoader(new URL[]{projectDirectory.resolve("build/").toUri().toURL()});

				// Load the class from the classloader by name....
				Class<?> loadedClass = classLoader.loadClass(filePath.replace(".java", ""));
				return loadedClass;
			} catch (ClassNotFoundException | MalformedURLException e) {
				throw new RuntimeException(e);
			}
			// Create a new instance...
			/************************************************************************************************* Load and execute **/
		} else {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				System.out.format("Error on line %d in %s%n",
						diagnostic.getLineNumber(),
						diagnostic.getSource().toUri());
			}
		}
		try {
			fileManager.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void init(Context context) {
        try {
            loadProject(currentProjectPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public ServicePriority getPriority() {
		return ServicePriority.RESOURCES;
	}
}
