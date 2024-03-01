package com.mpcs.scratchpad.core;

import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.rendering.Renderer;
import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.service.EngineService;
import com.mpcs.scratchpad.core.simulation.Simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EngineBuilder {

    List<Class<? extends EngineService>> services = new ArrayList<>();

    public EngineBuilder() {
        services.add(Simulation.class);
        services.add(InputManager.class);
        services.add(Renderer.class);
    }

    public EngineBuilder noRendering() {
        services.remove(Renderer.class);
        return this;
    }

    public Engine create(String projectPath) throws EngineCreationException, IOException {
        return new Engine(services, projectPath);
    }

    public Engine create(ResourceManager resourceManager) throws EngineCreationException {
        return new Engine(services, resourceManager);
    }
}
