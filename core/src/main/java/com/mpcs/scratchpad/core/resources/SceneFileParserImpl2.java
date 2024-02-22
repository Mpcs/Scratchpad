package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.resources.parsing.LineParser;
import com.mpcs.scratchpad.core.resources.parsing.type.NodeTypeParser;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParseException;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParsers;
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

public class SceneFileParserImpl2 implements SceneFileParser{
    private Path path;

    LineParser nodeLineParser;
    LineParser attributeLineParser;

    public SceneFileParserImpl2(Path filepath) {
        this.path = filepath;

        nodeLineParser = new LineParser().startingWith("[")
                .parameter("type").parsedWith(TypeParsers.NODE_TYPE_PARSER).until(" ")
                .parameter("name").withPrefix("name=").until("")
                .parameter("parent").withPrefix("parent=").until("]");

        attributeLineParser = new LineParser().startingWith("| ")
                .keyValueParameter("=").until("\n");

    }

    public Scene toScene() {
        Map<String, Node> namedNodes= new HashMap<>();
        Node rootNode = null;
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            String line;
            Node currentNode = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (nodeLineParser.matches(line)) {
                    Map<String, Object> parameters = nodeLineParser.parse(line);

                    Class<? extends Node> nodeClass = (Class<? extends Node>) parameters.get("type");
                    Constructor<?> constructor = nodeClass.getConstructor();
                    currentNode = (Node) constructor.newInstance();

                    String parentName = (String) parameters.get("parent");
                    if (parentName.isEmpty()) {
                        rootNode = currentNode;
                    } else {
                        //namedNodes.get(parentName).addChild(currentNode);
                    }
                    namedNodes.put((String) parameters.get("name"), currentNode);
                } else if (attributeLineParser.matches(line)) {
                    Map<String, Object> parameters = attributeLineParser.parse(line);
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        String attrName = entry.getKey();
                        String attrValue = (String) entry.getValue();

                        for (Method method : currentNode.getClass().getMethods()) {
                            //System.out.println("set"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                            if (method.getName().equals("set"+ attrName.substring(0, 1).toUpperCase() + attrName.substring(1))) {
                                System.out.println(method.getName());
                                Parameter parameter = method.getParameters()[0];
                                if(Arrays.stream(parameter.getType().getInterfaces()).toList().contains(Stringable.class)) {
                                    Method generatorMethod = parameter.getType().getMethod("fromString", String.class);
                                    Object object = generatorMethod.invoke(null, attrValue.replace("\"", ""));
                                    method.invoke(currentNode, object);
                                } else if (parameter.getType().equals(Vector3f.class)) {
                                    String[] cleanParams = attrValue.replace("(", "").replace(")", "").split(",");
                                    Vector3f vector3f = new Vector3f(Float.valueOf(cleanParams[0]), Float.valueOf(cleanParams[1]), Float.valueOf(cleanParams[2]));
                                    method.invoke(currentNode, vector3f);
                                }

                            }
                        }
                    }
                }
                System.out.println(line);
            }

        } catch (IOException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (TypeParseException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(rootNode);

        return scene;
    }
}
