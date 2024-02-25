package com.mpcs.scratchpad.core.scene.nodes;

import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.scene.NodeException;
import org.joml.Vector3f;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Registry(Node.class)
public class Node {
    private Vector3f relativePosition = new Vector3f();

    private Set<Node> children;
    private Node parent;

    public Node() {
        this(new Vector3f(0, 0, 0));
    }
    public Node(Vector3f relativePosition) {
        setRelativePosition(relativePosition);
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

    public Vector3f getRelativePosition() {
        return new Vector3f(relativePosition);
    }

    public Vector3f getAbsolutePosition() {
        if (parent == null) {
            return relativePosition;
        }
        Vector3f tempVector = this.getRelativePosition();
        tempVector.add(parent.getAbsolutePosition());
        return tempVector;
    }

    public void setRelativePosition(Vector3f newPosition) {
        this.relativePosition.set(newPosition);
    }

    public List<Node> getAllChildrenAndSubChildren() {
        List<Node> subChildren = new LinkedList<>();
        for (Node child : children) {
            subChildren.addAll(child.getAllChildrenAndSubChildren());
        }
        subChildren.addAll(children);
        return subChildren;
    }

    public void update(float deltaTime) {
        // method empty for override
    }
}
