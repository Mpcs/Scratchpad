package com.mpcs.scratchpad;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.mpcs.scratchpad.rendering.MyGLEventListenerEx1;

public class Engine {
    final GLWindow window;
    private final Simulation simulation;
    private final Thread simulationThread;
    public Engine(boolean withRendering) {
        simulation = new Simulation(this);
        simulationThread = new Thread(simulation, "EngineUpdateThread");
        simulationThread.start();

        if (withRendering) {
            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);

            window = GLWindow.create(NewtFactory.createScreen(jfxNewtDisplay, 0), glCapabilities);
            window.addGLEventListener(new MyGLEventListenerEx1());
            window.addKeyListener(simulation.kbListener);
            window.addMouseListener(simulation.mouseListener);
        } else {
            window = null;
        }
    }

    public void stop() {
        simulation.running.set(false);
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
