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
    private final Thread simulationThread;
    private final Context context;

    public Engine(String projectPath, boolean withRendering) throws IOException{
        this(new ResourceManager(projectPath), withRendering);
    }

    public Engine(ResourceManager resourceManager, boolean withRendering) {
        context = Context.createContext(this);

        context.setResourceManager(resourceManager);
        context.setInputManager(new InputManager());
        if (withRendering) {
            context.setRenderer(new Renderer());
        }
        context.setSimulation(new Simulation());

        simulationThread = new Thread(context.getSimulation(), "EngineUpdateThread");
        simulationThread.start();
    }

    public void stop() {
        context.getSimulation().running.set(false);
        context.getRenderer().stop();
    }

    public boolean isRunning() {
        return simulationThread.isAlive();
    }

    public Context getContext() {
        return context;
    }
}
