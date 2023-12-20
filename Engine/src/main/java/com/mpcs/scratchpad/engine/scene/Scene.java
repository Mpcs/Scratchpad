package com.mpcs.scratchpad.engine.scene;

import com.mpcs.scratchpad.engine.rendering.Camera;
import com.mpcs.scratchpad.engine.scene.nodes.Node;

import java.util.List;

public class Scene {
    private Node rootNode;
    public Camera camera;

    public Scene(Node rootNode) {
        this.camera = new Camera();
        this.rootNode = rootNode;
    }

    public Node getRootNode() {
        return rootNode;
    }
    public void addNode(Node node) throws NodeException {
        rootNode.addChild(node);
    }

    public List<Node> getAllNodes() {
        return rootNode.getAllChildrenAndSubChildren();
    }

}
