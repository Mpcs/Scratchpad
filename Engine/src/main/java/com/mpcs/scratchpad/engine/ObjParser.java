package com.mpcs.scratchpad.engine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.List;
import com.mpcs.logging.Logger;
import java.lang.Float;
import java.util.ArrayList;
import org.joml.Vector3f;
import java.util.Arrays;
import java.io.IOException;

public class ObjParser {

	//Map<String
	List<Vector3f> vertices = new ArrayList();
	List<Vector3f> vertexNormals = new ArrayList();


	public void loadFromFile(String filepath) throws IOException{
		Path path = Paths.get(filepath);
		BufferedReader bufferedReader = Files.newBufferedReader(path);
		List<String> lines = bufferedReader.lines().toList();
		for (String line : lines) {
			if (line.startsWith("#")) {
				continue;
			}
			String[] commandAndArgs = line.split(" ");
			String command = commandAndArgs[0];
			String[] arguments = Arrays.copyOfRange(commandAndArgs, 1, commandAndArgs.length);
			switch(command) {
			case "v":
				processVertexCommand(arguments);
				break;
			case "vn":
				processVertexNormalsCommand(arguments);
				break;
			case "f":
				processFaceCommand(arguments);
				break;
			default:
				Logger.warn("OBJ command not implemented: " + command);
			}
		}
	}

	private void processVertexCommand(String[] arguments) {
		float x = Float.valueOf(arguments[0]);
		float y = Float.valueOf(arguments[1]);
		float z = Float.valueOf(arguments[2]);
		vertices.add(new Vector3f(x, y, z));
	}

	private void processVertexNormalsCommand(String[] arguments) {
		float x = Float.valueOf(arguments[0]);
		float y = Float.valueOf(arguments[1]);
		float z = Float.valueOf(arguments[2]);
		vertexNormals.add(new Vector3f(x, y, z));
	}
}