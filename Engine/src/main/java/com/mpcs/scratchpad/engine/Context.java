package com.mpcs.scratchpad.engine;

import com.mpcs.scratchpad.engine.rendering.Renderer;
import com.mpcs.scratchpad.engine.simulation.Simulation;

public class Context {
    private final Engine engine;
    private Renderer renderer;
    private Simulation simulation;
    private InputManager inputManager;
    private ResourceManager resourceManager;

    public Context(Engine engine) {
        this.engine = engine;
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
