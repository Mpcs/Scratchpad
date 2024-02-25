package com.mpcs.scratchpad.core.rendering.mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.resources.Image;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class IndicesMesh3D implements Mesh3D {

    private final float[] vertices;
    private final int[] indices;

    private boolean wasSetUp = false;
    private int vao;

    public IndicesMesh3D(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    private void setupMesh(GL3 gl) {
        IntBuffer vaoBuffer = IntBuffer.allocate(1);
        IntBuffer vboBuffer = IntBuffer.allocate(1);
        IntBuffer eboBuffer = IntBuffer.allocate(1);

        gl.glGenVertexArrays(1, vaoBuffer);
        gl.glGenBuffers(1, vboBuffer);
        gl.glGenBuffers(1, eboBuffer);

        vao = vaoBuffer.get();
        int vbo = vboBuffer.get();
        int ebo = eboBuffer.get();

        gl.glBindVertexArray(vao);

        FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, verticesBuffer, GL.GL_STATIC_DRAW);

        IntBuffer indicesBuffer = Buffers.newDirectIntBuffer(indices);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebo);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, (long) indices.length *Integer.BYTES, indicesBuffer, GL.GL_STATIC_DRAW);

        int stride = 8*Float.BYTES;
        // positions
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, stride, 0);
        gl.glEnableVertexAttribArray(0);
        // colors
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, stride, 3*Float.BYTES);
        gl.glEnableVertexAttribArray(1);

        gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, stride, 6*Float.BYTES);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(0);
    }

    public void render(GL3 gl, ShaderProgram shaderProgram, Image texture) {
        if (!wasSetUp) {
            setupMesh(gl);
            wasSetUp = true;
        }
        gl.glUseProgram(shaderProgram.getID());
        gl.glActiveTexture(0);
        texture.bind(gl);
        gl.glBindVertexArray(vao);
        gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }
}
