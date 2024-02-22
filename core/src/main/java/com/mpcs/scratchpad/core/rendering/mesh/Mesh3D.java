package com.mpcs.scratchpad.core.rendering.mesh;

import com.jogamp.opengl.GL3;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.resources.Image;
import com.mpcs.scratchpad.core.resources.Stringable;

import javax.management.monitor.StringMonitorMBean;
import java.io.IOException;

public interface Mesh3D extends Stringable {
    void render(GL3 gl, ShaderProgram shaderProgram, Image texture);

    static Object fromString(String string) {
        try {
            return Context.get().getResourceManager().getResourceMesh(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
