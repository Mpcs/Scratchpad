package com.mpcs.scratchpad.core.scene.ogscene;

import com.mpcs.scratchpad.core.*;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.Image;
import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import org.joml.Vector3f;

import java.io.IOException;

@Deprecated
public class OGScene extends Scene {

    Vector3f[] cubePositions = {
            new Vector3f( 0.0f,  -3.0f,  -5.0f),
   //new Vector3f( 2.0f,  5.0f, -15.0f),
   //new Vector3f(-1.5f, -2.2f, -2.5f),
   //new Vector3f(-3.8f, -2.0f, -12.3f),
   //new Vector3f( 2.4f, -0.4f, -3.5f),
   //new Vector3f(-1.7f,  3.0f, -7.5f),
   //new Vector3f( 1.3f, -2.0f, -2.5f),
   //new Vector3f( 1.5f,  2.0f, -2.5f),
   //new Vector3f( 1.5f,  0.2f, -1.5f),
   //new Vector3f(-1.3f,  1.0f, -1.5f)
};

	public OGScene() {
		super(new Node());
        ResourceManager rm = Context.get().getResourceManager();

        Node rootNode = this.getRootNode();
        Image texture;

        Mesh3D mesh3D;
        try {
            texture = rm.getResourceImage("textures/Vehicle_Taxi.png");
            mesh3D = rm.getResourceMesh("models/taxi_mesh.obj");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Vector3f cubePosition : cubePositions) {
            BoxNode newBoxNode = new BoxNode(mesh3D, texture);
            newBoxNode.scale = new Vector3f(0.5F);
            newBoxNode.setRelativePosition(cubePosition);
            rootNode.addChild(newBoxNode);
        }
		
	}
}