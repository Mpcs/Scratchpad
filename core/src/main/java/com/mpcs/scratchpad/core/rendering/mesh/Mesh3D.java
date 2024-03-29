package com.mpcs.scratchpad.core.rendering.mesh;

import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.resources.Image;

public interface Mesh3D {
    void render(GL3 gl, ShaderProgram shaderProgram, Image texture);
}
