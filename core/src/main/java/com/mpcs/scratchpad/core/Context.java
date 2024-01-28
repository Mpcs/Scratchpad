package com.mpcs.scratchpad.core;

import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.rendering.Renderer;
import com.mpcs.scratchpad.core.simulation.Simulation;
import com.mpcs.scratchpad.core.resources.ResourceManager;

public class Context {

    private static Context instance;

    private final Engine engine;
    private Renderer renderer;
    private Simulation simulation;
    private InputManager inputManager;
    private ResourceManager resourceManager;

    private Context(Engine engine) {
        this.engine = engine;
    }

    static Context createContext(Engine engine) {
        if (instance != null) {
            throw new UnsupportedOperationException("Context already exists");
        }
        instance = new Context(engine);
        return instance;
    }

    public static Context get() {
        return instance;
    }

    void setResourceManager(ResourceManager resourceManager) {
        if (this.resourceManager != null)
            throw new UnsupportedOperationException("Trying to overwrite ResourceManager");
        this.resourceManager = resourceManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    void setRenderer(Renderer renderer) {
        if (this.renderer != null)
            throw new UnsupportedOperationException("Trying to overwrite Renderer");

        this.renderer = renderer;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    void setSimulation(Simulation simulation) {
        if (this.simulation != null)
            throw new UnsupportedOperationException("Trying to overwrite Simulation");

        this.simulation = simulation;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    void setInputManager(InputManager inputManager) {
        if (this.inputManager != null)
            throw new UnsupportedOperationException("Trying to overwrite InputManager");

        this.inputManager = inputManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Engine getEngine() {
        return engine;
    }
}
