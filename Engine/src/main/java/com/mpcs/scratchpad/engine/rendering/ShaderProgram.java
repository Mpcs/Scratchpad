package com.mpcs.scratchpad.engine.rendering;

import com.jogamp.opengl.GL3;

import java.nio.ByteBuffer;
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
}
