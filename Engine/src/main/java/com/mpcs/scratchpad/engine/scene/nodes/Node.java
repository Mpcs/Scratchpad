package com.mpcs.scratchpad.engine.scene.nodes;

import com.mpcs.math.Vector3;
import com.mpcs.scratchpad.engine.scene.NodeException;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Node {
    private Vector3 relativePosition;
    private Set<Node> children;
    private Node parent;

    public Node() {
        this(new Vector3(0, 0, 0));
    }
    public Node(Vector3 relativePosition) {
        this.relativePosition = relativePosition;
        this.children = new LinkedHashSet<>();
    }

    public Set<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) throws NodeException {
        if(node.getParent() != null) {
            throw new NodeException("Cannot add a node already being in the scene tree");
        }
        children.add(node);
        node.setParent(this);
    }

    private void setParent(Node node) {
        this.parent = node;
    }

    public void removeChild(Node node) throws NodeException {
        if (!children.contains(node)) {
            throw new NodeException("Child object not found");
        }
        children.remove(node);
    }

    private Node getParent() {
        return parent;
    }

    public Vector3 getRelativePosition() {
        return relativePosition;
    }

    public Vector3 getAbsolutePosition() {
        if (parent == null) {
            return relativePosition;
        }
        Vector3 parentAbsolutePosition = parent.getAbsolutePosition();
        return parentAbsolutePosition.add(this.getRelativePosition());
    }

    public void setRelativePosition(Vector3 newPosition) {
        this.relativePosition = newPosition;
    }

    public List<Node> getAllChildrenAndSubChildren() {
        List<Node> subChildren = new LinkedList<>();
        for (Node child : children) {
            subChildren.addAll(child.getAllChildrenAndSubChildren());
        }
        subChildren.addAll(children);
        return subChildren;
    }
}
