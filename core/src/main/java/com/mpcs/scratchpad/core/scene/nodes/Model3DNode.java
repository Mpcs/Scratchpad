package com.mpcs.scratchpad.core.scene.nodes;

import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.resources.Image;
import org.joml.Vector3f;

@Registry(Node.class)
public class Model3DNode extends Node {

    private Mesh3D mesh3D;
    private Image texture;

    public float rotation = 0;
    public Vector3f scale = new Vector3f(1.0f);

    public Model3DNode() {

    }

    public Model3DNode(Mesh3D mesh, Image texture) {
        this.mesh3D = mesh;
        this.texture = texture;
    }

    public Mesh3D getMesh3D() {
        return mesh3D;
    }

    public void setMesh3D(Mesh3D mesh3D) {
        this.mesh3D = mesh3D;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void render(GL3 gl, ShaderProgram shaderProgram) {
        if (mesh3D != null)
            mesh3D.render(gl, shaderProgram, texture);
    }

}
