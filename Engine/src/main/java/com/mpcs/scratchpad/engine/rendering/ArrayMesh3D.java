package com.mpcs.scratchpad.engine.rendering;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ArrayMesh3D implements Mesh3D{
    private final float[] vertices;
    private boolean wasSetUp = false;
    private int VAO;

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

        VAO = vaoBuffer.get();
        int VBO = vboBuffer.get();

        gl.glBindVertexArray(VAO);

        FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO);
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

    public void render(GL3 gl, ShaderProgram shaderProgram) {
        if (!wasSetUp) {
            setupMesh(gl);
            wasSetUp = true;
        }
        gl.glUseProgram(shaderProgram.getID());
        gl.glBindVertexArray(VAO);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);
        gl.glBindVertexArray(0);
    }
}
