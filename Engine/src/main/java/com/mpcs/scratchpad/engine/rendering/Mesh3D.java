package com.mpcs.scratchpad.engine.rendering;

import com.jogamp.opengl.GL3;

public interface Mesh3D {
    void render(GL3 gl, ShaderProgram shaderProgram);
}
