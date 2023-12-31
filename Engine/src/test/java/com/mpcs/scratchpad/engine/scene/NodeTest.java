package com.mpcs.scratchpad.engine.scene;

import com.mpcs.scratchpad.engine.scene.nodes.Node;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

    @Test
    void getRelativePositionShouldReturnCopy() {
        Node node = new Node();
        Vector3f nodePosition = node.getRelativePosition();
        nodePosition.add(1, 0, 0);
        assertEquals(0, node.getRelativePosition().x());
    }

    @Test
    void setRelativePositionShouldCopyValue() {
        Node node = new Node();
        Vector3f position = new Vector3f(1, 2, 3);
        node.setRelativePosition(position);
        position.add(1,0,0);
        assertEquals(1, node.getRelativePosition().x());
    }
}