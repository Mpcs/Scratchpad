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
import java.util.UUID;

public class Engine {
    private final EngineThread simulationThread;
    private final Context context;

    private final UUID uuid;

    public Engine(String projectPath, boolean withRendering) throws IOException{
        this(new ResourceManager(projectPath), withRendering);
    }

    public Engine(ResourceManager resourceManager, boolean withRendering) {
        uuid = UUID.randomUUID();

        context = Context.createContext(this);

        context.put(ResourceManager.class, resourceManager);
        context.put(InputManager.class, new InputManager());

        if (withRendering) {
            context.put(Renderer.class, new Renderer(context));
        }
        context.put(Simulation.class, new Simulation());

        simulationThread = new EngineThread(context.getInstanceOf(Simulation.class), "EngineUpdateThread");
        simulationThread.setUuid(uuid);
        simulationThread.start();
    }

    public void stop() {
        context.getInstanceOf(Simulation.class).running.set(false);
        context.getInstanceOf(Renderer.class).stop();
    }

    public boolean isRunning() {
        return simulationThread.isAlive();
    }

    public Context getContext() {
        return context;
    }

    public UUID getUuid() {
        return uuid;
    }
}
