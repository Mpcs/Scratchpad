package com.mpcs.scratchpad.core.scene;

import com.mpcs.scratchpad.core.rendering.Camera;
import com.mpcs.scratchpad.core.scene.nodes.Node;

import java.util.List;

public class Scene {
    private final Node rootNode;
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
