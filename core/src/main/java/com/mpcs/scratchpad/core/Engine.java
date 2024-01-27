package com.mpcs.scratchpad.core;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.rendering.Renderer;
import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.simulation.Simulation;

import java.io.IOException;

public class Engine {

    private static final int TARGET_FPS = 60;

    private final GLWindow window;
    public final FPSAnimator animator;
    private final Thread simulationThread;
    private final Context context;

    public boolean stopped = false;

    public Engine(String projectPath, boolean withRendering) throws IOException{
        this(new ResourceManager(projectPath), withRendering);
    }

    public Engine(ResourceManager resourceManager, boolean withRendering) {
        context = Context.createContext(this);

        context.setResourceManager(resourceManager);
        context.setInputManager(new InputManager());
        context.setRenderer(new Renderer(context));
        context.setSimulation(new Simulation(context));

        simulationThread = new Thread(context.getSimulation(), "EngineUpdateThread");
        simulationThread.start();

        if (withRendering) {
            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);

            window = GLWindow.create(NewtFactory.createScreen(jfxNewtDisplay, 0), glCapabilities);
            window.addGLEventListener(context.getRenderer());
            window.addKeyListener(context.getInputManager());
            window.addMouseListener(context.getInputManager());
            window.addWindowListener(new CloseWindowListener(this));

            animator = new FPSAnimator(window, TARGET_FPS);
            animator.start();
            animator.setUpdateFPSFrames(1, null);

        } else {
            window = null;
            animator = null;
        }
    }

    public void stop() {
        context.getSimulation().running.set(false);
        if(animator != null)
            animator.stop();
        this.stopped = true;
    }

    public GLWindow getGlWindow() {
        return window;
    }

    public boolean isRunning() {
        return simulationThread.isAlive();
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
