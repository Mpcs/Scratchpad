package com.mpcs.scratchpad.core.resources;

import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SceneFileParser {

    private Path path;

    public SceneFileParser(Path filepath) {
        this.path = filepath;
    }

    public Scene toScene() {
        Map<String, Node> namedNodes= new HashMap<>();
        Node parentNode;
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("[")) { // new node
                    String[] elements = line.replace("[", "").replace("]", "").split(" ");
                    String nodeClassName = elements[0];
                    String name = elements[1].split("=")[1].replace("\"", "");
                    String parentName = elements[2].split("=")[1].replace("\"", "");

                    Class<?> nodeClass = Class.forName("com.mpcs.scratchpad.engine.scene.nodes" + elements[0]);

                    Constructor<?> constructor = nodeClass.getConstructor();
                    Node object = (Node) constructor.newInstance();
                    //object.setName(name);
                    if (parentName == null) {
                        parentNode = object;
                    } else {
                        namedNodes.get(parentName).addChild(object);
                    }

                }

                if (line.startsWith("|")) { // param

                }
                System.out.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
