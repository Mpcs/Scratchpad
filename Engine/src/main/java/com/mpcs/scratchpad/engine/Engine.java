package com.mpcs.scratchpad.engine;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.mpcs.scratchpad.engine.rendering.Renderer;
import com.mpcs.scratchpad.simulation.Simulation;

public class Engine {

    private static final int TARGET_FPS = 60;

    private final GLWindow window;
    public final FPSAnimator animator;
    private final Thread simulationThread;
    private final Context context;
    public boolean stopped = false;
    public Engine(boolean withRendering) {
        context = new Context(this);

        context.setInputManager(new InputManager(context));
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

    public Context getContext() {
        return context;
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
