package com.mpcs.scratchpad.core.rendering.mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.resources.Image;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ArrayMesh3D implements Mesh3D {
    private final float[] vertices;
    private boolean wasSetUp = false;
    private int vao;

    public ArrayMesh3D(float[] vertices) {
        this.vertices = vertices;
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

        gl.glBindVertexArray(vao);

        FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, verticesBuffer, GL.GL_STATIC_DRAW);

        int stride = 5*Float.BYTES;
        // positions
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, stride, 0);
        gl.glEnableVertexAttribArray(0);
        // colors
        //gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, stride, 3*Float.BYTES);
        //gl.glEnableVertexAttribArray(1);
        // Texture UVs
        gl.glVertexAttribPointer(1, 2, GL.GL_FLOAT, false, stride, 3*Float.BYTES);
        gl.glEnableVertexAttribArray(1);

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
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);
        gl.glBindVertexArray(0);
    }
}
