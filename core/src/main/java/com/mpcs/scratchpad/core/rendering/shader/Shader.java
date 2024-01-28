package com.mpcs.scratchpad.core.rendering.shader;

import com.jogamp.opengl.GL3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Shader {
    private static final int MAX_ERROR_LENGTH = 512;
    private final String code;
    private final int type;
    private int id;

    private Shader(int type, String shaderCode) {
        this.code = shaderCode;
        this.type = type;
    }

    public void compile(GL3 gl) throws ShaderCompileException {
        id = gl.glCreateShader(type);
        gl.glShaderSource(id, 1, new String[]{code}, null);
        gl.glCompileShader(id);

        IntBuffer returnCodeBuffer = IntBuffer.allocate(1);
        gl.glGetShaderiv(id, GL3.GL_COMPILE_STATUS, returnCodeBuffer);
        if(returnCodeBuffer.get() != 1) {
            IntBuffer textLengthBuffer = IntBuffer.allocate(1);
            ByteBuffer messageBuffer = ByteBuffer.allocate(MAX_ERROR_LENGTH);
            gl.glGetShaderInfoLog(id, MAX_ERROR_LENGTH, textLengthBuffer, messageBuffer);
            String exceptionMessage = new String(messageBuffer.array(), 0, textLengthBuffer.get());

            throw new ShaderCompileException(exceptionMessage);
        }
    }

    public void delete(GL3 gl) {
        gl.glDeleteShader(id);
        id = 0;
    }

    public int getId() {
        return id;
    }

    public boolean isCompiled() {
        return id != 0;
    }

    public static Shader createFragmentShader(String code) {
        return new Shader(GL3.GL_FRAGMENT_SHADER, code);
    }

    public static Shader createVertexShader(String code) {
        return new Shader(GL3.GL_VERTEX_SHADER, code);
    }
}
