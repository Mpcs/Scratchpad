package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SceneFileParserImpl implements SceneFileParser{

    private Path path;

    public SceneFileParserImpl(Path filepath) {
        this.path = filepath;
    }

    public Scene toScene() {
        Map<String, Node> namedNodes= new HashMap<>();
        Node parentNode = null;
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            String line;
            Node currentNode = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("[")) { // new node
                    String[] elements = line.replace("[", "").replace("]", "").split(" ");
                    String nodeClassName = elements[0];
                    String name = elements[1].split("=")[1].replace("\"", "");
                    String parentName = elements[2].split("=")[1].replace("\"", "");

                    Class<?> nodeClass = Class.forName("com.mpcs.scratchpad.core.scene.nodes." + nodeClassName);

                    Constructor<?> constructor = nodeClass.getConstructor();
                    currentNode = (Node) constructor.newInstance();
                    //object.setName(name);
                    if (parentName.isEmpty()) {
                        parentNode = currentNode;
                    } else {
                        namedNodes.get(parentName).addChild(currentNode);
                    }
                    namedNodes.put(name, currentNode);

                }

                if (line.startsWith("|")) { // param
                    String[] elements = line.replace("| ", "").split("=");
                    String fieldName = elements[0];
                    String param = elements[1];

                    for (Method method : currentNode.getClass().getMethods()) {
                        //System.out.println("set"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                        if (method.getName().equals("set"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1))) {
                            System.out.println(method.getName());
                            Parameter parameter = method.getParameters()[0];
                            if(Arrays.stream(parameter.getType().getInterfaces()).toList().contains(Stringable.class)) {
                                Method generatorMethod = parameter.getType().getMethod("fromString", String.class);
                                Object object = generatorMethod.invoke(null, param.replace("\"", ""));
                                method.invoke(currentNode, object);
                            } else if (parameter.getType().equals(Vector3f.class)) {
                                String[] cleanParams = param.replace("(", "").replace(")", "").split(",");
                                Vector3f vector3f = new Vector3f(Float.valueOf(cleanParams[0]), Float.valueOf(cleanParams[1]), Float.valueOf(cleanParams[2]));
                                method.invoke(currentNode, vector3f);
                            }

                        }
                    }

                }
                System.out.println(line);
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(parentNode);

        return scene;
    }
}
