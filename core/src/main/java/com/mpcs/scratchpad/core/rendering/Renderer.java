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
import com.mpcs.scratchpad.core.service.EngineService;
import com.mpcs.scratchpad.core.service.ServicePriority;
import com.mpcs.scratchpad.core.input.InputManager;

public class Renderer implements EngineService {

    private static final int TARGET_FPS = 60;

    private GLWindow window;
    private FPSAnimator animator;
    @Override
    public void init(Context context) {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        GLListener glListener = new GLListener(context);

        window = GLWindow.create(NewtFactory.createScreen(jfxNewtDisplay, 0), glCapabilities);
        window.addGLEventListener(glListener);
        window.addKeyListener(context.getInstanceOf(InputManager.class));
        window.addMouseListener(context.getInstanceOf(InputManager.class));
        window.addWindowListener(new CloseWindowListener(context.getEngineInstance()));

        animator = new FPSAnimator(window, TARGET_FPS);
        animator.start();
        animator.setUpdateFPSFrames(1, null);
    }

    @Override
    public void start() {

    }

    public void stop() {
        animator.stop();
    }

    @Override
    public ServicePriority getPriority() {
        return ServicePriority.MAIN;
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
