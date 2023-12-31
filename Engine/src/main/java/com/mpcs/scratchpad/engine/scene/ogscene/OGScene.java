package com.mpcs.scratchpad.engine.scene.ogscene;

import com.mpcs.scratchpad.engine.scene.ogscene.BoxNode;
import com.mpcs.scratchpad.engine.scene.Scene;
import com.mpcs.scratchpad.engine.scene.nodes.Node;
import com.mpcs.scratchpad.engine.rendering.ArrayMesh3D;
import org.joml.Vector3f;
import java.lang.Deprecated;

@Deprecated
public class OGScene extends Scene {

	    float[] vertices = {
            0.5f,  0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,  // top right
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,// bottom right
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,    // bottom left
            -0.5f,  0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,   // top left
    };

    float[] verticesNoIndices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };
    int[] indices = {  // note that we start from 0!
            0, 1, 3,   // first triangle
            1, 2, 3,    // second triangle
    };

    Vector3f[] cubePositions = {
            new Vector3f( 0.0f,  0.0f,  0.0f),
    new Vector3f( 2.0f,  5.0f, -15.0f),
    new Vector3f(-1.5f, -2.2f, -2.5f),
    new Vector3f(-3.8f, -2.0f, -12.3f),
    new Vector3f( 2.4f, -0.4f, -3.5f),
    new Vector3f(-1.7f,  3.0f, -7.5f),
    new Vector3f( 1.3f, -2.0f, -2.5f),
    new Vector3f( 1.5f,  2.0f, -2.5f),
    new Vector3f( 1.5f,  0.2f, -1.5f),
    new Vector3f(-1.3f,  1.0f, -1.5f)
};

	public OGScene() {
		super(new Node());
		Node rootNode = this.getRootNode();
        ArrayMesh3D mesh3D = new ArrayMesh3D(verticesNoIndices);

        for (Vector3f cubePosition : cubePositions) {
            BoxNode newBoxNode = new BoxNode(mesh3D);
            newBoxNode.setRelativePosition(cubePosition);
            rootNode.addChild(newBoxNode);
        }
		
	}
}