package com.mpcs.scratchpad.core.rendering;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.Engine;
import com.mpcs.scratchpad.core.rendering.shader.Shader;
import com.mpcs.scratchpad.core.rendering.shader.ShaderCompileException;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgramLinkException;
import com.mpcs.scratchpad.core.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class Renderer {

    private static final int TARGET_FPS = 60;

    private final GLWindow window;
    private final FPSAnimator animator;
    public Renderer() {
        Context context = Context.get();
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        GLListener glListener = new GLListener();

        window = GLWindow.create(NewtFactory.createScreen(jfxNewtDisplay, 0), glCapabilities);
        window.addGLEventListener(glListener);
        window.addKeyListener(context.getInputManager());
        window.addMouseListener(context.getInputManager());
        window.addWindowListener(new CloseWindowListener(context.getEngine()));

        animator = new FPSAnimator(window, TARGET_FPS);
        animator.start();
        animator.setUpdateFPSFrames(1, null);
    }

    public void stop() {
        animator.stop();
    }

    public GLWindow getGlWindow() {
        return window;
    }

    static class CloseWindowListener extends WindowAdapter {
        private final Engine engine;
        public CloseWindowListener(Engine engine) {
            this.engine = engine;
        }
        @Override
        public void windowDestroyed(WindowEvent e) {
            engine.stop();
        }
    }
}
