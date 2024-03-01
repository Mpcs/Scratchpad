package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.resources.parsing.LineParser;
import com.mpcs.scratchpad.core.resources.parsing.type.ScriptParserLoader;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParseException;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParsers;
import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SceneFileParser {
    private Path path;

    LineParser nodeLineParser;
    LineParser attributeLineParser;

    public SceneFileParser(Path filepath) {
        this.path = filepath;

        nodeLineParser = new LineParser().startingWith("[")
                .parameter("type").parsedWith(TypeParsers.NODE_TYPE_PARSER).until(" ")
                .parameter("script").setOptional().parsedWith(new ScriptParserLoader()).withPrefix("script=").until(" ")
                .parameter("name").withPrefix("name=").until(" ")
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
                    Class<?> scriptClass = (Class<?>) parameters.get("script");
                    if (scriptClass != null) {
                        //System.out.println(scriptClass.getSuperclass().getName());
                        //System.out.println(nodeClass.getName());
                        if (scriptClass.getSuperclass().equals(nodeClass)) {
                            nodeClass = (Class<? extends Node>) scriptClass;
                        } else {
                            throw new RuntimeException("Loaded class: " + scriptClass.getName() + " should extend " + nodeClass.getName());
                        }
                    }
                    Constructor<?> constructor = nodeClass.getConstructor();
                    currentNode = (Node) constructor.newInstance();

                    String parentName = (String) parameters.get("parent");
                    if (parentName.isEmpty()) {
                        rootNode = currentNode;
                    } else {
                        namedNodes.get(parentName).addChild(currentNode);
                    }
                    namedNodes.put((String) parameters.get("name"), currentNode);
                } else if (attributeLineParser.matches(line)) {
                    Map<String, Object> parameters = attributeLineParser.parse(line);
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        String attrName = entry.getKey();
                        Object attrValue = entry.getValue();


                        String setterName = "set" + attrName.substring(0,1).toUpperCase() + attrName.substring(1);
                        Method setterMethod = null;

                        for (Method method : currentNode.getClass().getMethods()) {
                            if(method.getName().equals(setterName)) {
                                setterMethod = method;
                                break;
                            }
                        }
                        Class<?> parameterType = setterMethod.getParameterTypes()[0];

                        if (attrValue instanceof String stringValue) {
                            attrValue = TypeParsers.parse(stringValue, parameterType);
                        }

                        setterMethod.invoke(currentNode, attrValue);
                    }
                }
                //System.out.println(line);
            }

        } catch (IOException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (TypeParseException e) {
            throw new RuntimeException(e);
        }

        return new Scene(rootNode);
    }
}
