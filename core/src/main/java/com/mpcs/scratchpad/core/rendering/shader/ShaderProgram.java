package com.mpcs.scratchpad.core.rendering.shader;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShaderProgram {
    private static final int MAX_ERROR_LENGTH = 512;
    private final Set<Shader> shaders = new HashSet<>();
    private int id;

    public void addShader(Shader shader) {
        shaders.add(shader);
    }

    public void addShaders(List<Shader> shaders) {
        this.shaders.addAll(shaders);
    }

    public void link(GL3 gl) throws ShaderProgramLinkException {
        id = gl.glCreateProgram();
        for (Shader shader : shaders) {
            gl.glAttachShader(id, shader.getId());
        }
        gl.glLinkProgram(id);
        IntBuffer returnCodeBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(id, GL3.GL_LINK_STATUS, returnCodeBuffer);
        if (returnCodeBuffer.get() != 1) {
            IntBuffer textLengthBuffer = IntBuffer.allocate(1);
            ByteBuffer messageBuffer = ByteBuffer.allocate(MAX_ERROR_LENGTH);
            gl.glGetProgramInfoLog(id, MAX_ERROR_LENGTH, textLengthBuffer, messageBuffer);
            String exceptionMessage = new String(messageBuffer.array(), 0, textLengthBuffer.get());

            throw new ShaderProgramLinkException(exceptionMessage);
        }
    }

    public int getID() {
        return id;
    }

    public void setUniform4f(GL3 gl, String uniformName, float x, float y, float z, float w) {
        int uniformLocation = gl.glGetUniformLocation(this.id, uniformName);
        gl.glUseProgram(this.id);
        gl.glUniform4f(uniformLocation, x, y, z, w);
        gl.glUseProgram(0);
    }

    public void setUniform3f(GL3 gl, String uniformName, float x, float y, float z) {
        int uniformLocation = gl.glGetUniformLocation(this.id, uniformName);
        gl.glUseProgram(this.id);
        gl.glUniform3f(uniformLocation, x, y, z);
        gl.glUseProgram(0);
    }

    public void setUniform1i(GL3 gl, String uniformName, int i) {
        int uniformLocation = gl.glGetUniformLocation(this.id, uniformName);
        gl.glUseProgram(this.id);
        gl.glUniform1i(uniformLocation, i);
        gl.glUseProgram(0);
    }

    public void setUniform1f(GL3 gl, String uniformName, float val) {
        int uniformLocation = gl.glGetUniformLocation(this.id, uniformName);
        gl.glUseProgram(this.id);
        gl.glUniform1f(uniformLocation, val);
        gl.glUseProgram(0);
    }

    public void setUniformMatrix4fv(GL3 gl, String uniformName, Matrix4f matrix4f) {
        int uniformLocation = gl.glGetUniformLocation(this.id, uniformName);
        FloatBuffer matrixBuffer = Buffers.newDirectFloatBuffer(16);
        matrix4f.getTransposed(matrixBuffer);
        gl.glUseProgram(this.id);
        //matrix4f.get

        gl.glUniformMatrix4fv(uniformLocation, 1, true, matrixBuffer);
        gl.glUseProgram(0);
    }
}
