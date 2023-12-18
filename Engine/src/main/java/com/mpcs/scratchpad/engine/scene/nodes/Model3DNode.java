package com.mpcs.scratchpad.engine.scene.nodes;

import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.engine.rendering.Mesh3D;
import com.mpcs.scratchpad.engine.rendering.ShaderProgram;
import org.joml.Vector3f;

public class Model3DNode extends Node {

    private Mesh3D mesh3D;

    public float rotation = 0;
    public Vector3f scale = new Vector3f(1.0f);

    public Model3DNode(Mesh3D mesh) {
        this.mesh3D = mesh;
    }

    public void render(GL3 gl, ShaderProgram shaderProgram) {
        mesh3D.render(gl, shaderProgram);
    }

}
