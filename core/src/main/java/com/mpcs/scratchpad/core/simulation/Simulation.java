package com.mpcs.scratchpad.core.simulation;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.scene.Scene;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import com.mpcs.scratchpad.core.simulation.loops.MinecraftLoop;
import com.mpcs.scratchpad.core.simulation.loops.SimulationLoop;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable {

    private static final boolean DISPLAY_TPS = false;

    public AtomicBoolean running = new AtomicBoolean(false);
    final Context context;
    private Scene scene;

    public Simulation() {
        this.context = Context.get();
        this.scene = context.getResourceManager().loadInitialScene();
    }

    private long framesSinceStart = 0;
    @Override
    public void run() {
        running.set(true);

        SimulationLoop simulationLoop = new MinecraftLoop(this::update, 60);
        while(running.get()) {
            simulationLoop.tickAndWait();
            framesSinceStart++;

        }
    }

    private void update(double delta) {
        for (Node node : scene.getAllNodes()) {
            node.update(0.0f);
        }

        if (context.getInputManager().isKeyPressed(KeyEvent.VK_UP)) {
            scene.camera.position.add(0.0f, 0.1f, 0.0f);
        }
        if(context.getInputManager().isKeyPressed(KeyEvent.VK_DOWN)) {
            scene.camera.position.sub(0.0f, 0.1f, 0.0f);
        }
    }
    
    public Scene getScene() {
        return scene;
    }
}
