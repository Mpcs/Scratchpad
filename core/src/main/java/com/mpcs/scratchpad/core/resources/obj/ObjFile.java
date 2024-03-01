package com.mpcs.scratchpad.core.resources.obj;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjFile {
	private final List<Vector3f> vertexPositions = new ArrayList<>();
	private final List<Vector2f> vertexTextureNormals = new ArrayList<>();
	private final List<Vector3f> vertexNormals = new ArrayList<>();
	private final List<ObjFace> faces = new ArrayList<>();

	private ObjFile() {

	}

	public static ObjFile loadFromFile(Path filepath) throws IOException{
		BufferedReader bufferedReader = Files.newBufferedReader(filepath);
		List<String> lines = bufferedReader.lines().toList();
		ObjFile objFile = new ObjFile();

		int lineCount = 0;
		for (String line : lines) {
			lineCount++;
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			String[] commandAndArgs = line.split(" ");
			String command = commandAndArgs[0];
			String[] arguments = Arrays.copyOfRange(commandAndArgs, 1, commandAndArgs.length);
			switch(command) {
			case "v":
				objFile.processVertexCommand(arguments);
				break;
			case "vn":
				objFile.processVertexNormalsCommand(arguments);
				break;
			case "vt":
				objFile.processVertexTextureNormalsCommand(arguments);
				break;
			case "f":
				objFile.processFaceCommand(arguments, lineCount);
				break;
			default:
				//Logger.warn("OBJ command not implemented: " + command);
			}

		}

		//Logger.debug(String.valueOf(objFile.faces.size()));
		return objFile;
	}

	private void processVertexTextureNormalsCommand(String[] arguments) {
		float x = Float.parseFloat(arguments[0]);
		float y = Float.parseFloat(arguments[1]);
		vertexTextureNormals.add(new Vector2f(x,y));
	}

	private void processVertexCommand(String[] arguments) {
		float x = Float.parseFloat(arguments[0]);
		float y = Float.parseFloat(arguments[1]);
		float z = Float.parseFloat(arguments[2]);
		vertexPositions.add(new Vector3f(x, y, z));
	}

	private void processVertexNormalsCommand(String[] arguments) {
		float x = Float.parseFloat(arguments[0]);
		float y = Float.parseFloat(arguments[1]);
		float z = Float.parseFloat(arguments[2]);
		vertexNormals.add(new Vector3f(x, y, z));
	}

	private void processFaceCommand(String[] arguments, int lineCount) {
		ObjFace newFace = new ObjFace(lineCount);
		for (String point : arguments) {
			String[] indices = point.split("/");
			int positionIndex = Integer.parseInt(indices[0]) - 1;
			int textureIndex = -1;
			int normalIndex = -1;
			if (indices.length > 1) {
				if (!indices[1].isEmpty()) {
					textureIndex = Integer.parseInt(indices[1]) - 1;
				}
			}
			if (indices.length == 3) {
				normalIndex = Integer.parseInt(indices[2]);
			}
			newFace.putPoint(positionIndex, textureIndex, normalIndex);
		}
		faces.add(newFace);
	}

	public float[] getTriangulatedVerticesFormatted(VertexDataFormat dataFormat) {
		List<Integer> indices = new ArrayList<>();
		for (ObjFace face : faces) {
			indices.addAll(face.getTriangulatedIndicesFormatted(dataFormat));
		}
		List<Float> vertices = new ArrayList<>();
		for (int i = 0; i < indices.size()-1; i += 2) {
			int positionIndice = indices.get(i);
			int textureIndice = indices.get(i+1);
			Vector3f vertexPosition = vertexPositions.get(positionIndice);
			Vector2f vertexTexture = vertexTextureNormals.get(textureIndice);
			vertices.add(vertexPosition.x);
			vertices.add(vertexPosition.y);
			vertices.add(vertexPosition.z);
			vertices.add(vertexTexture.x);
			vertices.add(vertexTexture.y);
		}
		return listToArray(vertices);

        //return vertices.stream().mapToD(Integer::intValue).toArray();
	}

	private static float[] listToArray(List<Float> floatList) {
		float[] floatArray = new float[floatList.size()];
		int i = 0;

		for (Float f : floatList) {
			floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
		}
		return floatArray;
	}

}


/*

 */