package com.mpcs.scratchpad.engine;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.mpcs.scratchpad.engine.rendering.Renderer;

public class Engine {
    private final GLWindow window;
    private final Thread simulationThread;
    private final Context context;
    public Engine(boolean withRendering) {
        context = new Context();

        context.setInputManager(new InputManager());
        context.setRenderer(new Renderer());
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
        } else {
            window = null;
        }
    }

    public void stop() {
        context.getSimulation().running.set(false);
        if(window != null)
            window.destroy();
    }

    public GLWindow getGlWindow() {
        return window;
    }

    public boolean isRunning() {
        return simulationThread.isAlive();
    }
}
