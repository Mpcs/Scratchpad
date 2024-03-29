package com.mpcs.scratchpad.core.simulation;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.service.EngineService;
import com.mpcs.scratchpad.core.service.EngineThread;
import com.mpcs.scratchpad.core.service.ServicePriority;
import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import com.mpcs.scratchpad.core.simulation.loops.MinecraftLoop;
import com.mpcs.scratchpad.core.simulation.loops.SimulationLoop;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements EngineService, Runnable {

    private static final boolean DISPLAY_TPS = false;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private EngineThread thread;

    private Scene scene;

    private long framesSinceStart = 0;

    @Override
    public void run() {
        this.scene = Context.get(ResourceManager.class).loadInitialScene();

        SimulationLoop simulationLoop = new MinecraftLoop(this::update, 60);

        running.set(true);
        while(running.get()) {
            simulationLoop.tickAndWait();
            framesSinceStart++;
        }
    }

    private void update(double delta) {
        for (Node node : scene.getAllNodes()) {
            node.update(0.0f);
        }

        InputManager inputManager = Context.get(InputManager.class);

        if (inputManager.isKeyPressed(KeyEvent.VK_UP)) {
            scene.camera.position.add(0.0f, 0.1f, 0.0f);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_DOWN)) {
            scene.camera.position.sub(0.0f, 0.1f, 0.0f);
        }
    }
    
    public Scene getScene() {
        return scene;
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void init(Context context) {
        thread = new EngineThread(this, "EngineUpdateThread");
        thread.setUuid(context.getEngineInstance().getUuid());
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void stop() {
        this.running.set(false);
    }

    @Override
    public ServicePriority getPriority() {
        return ServicePriority.MAIN;
    }
}
