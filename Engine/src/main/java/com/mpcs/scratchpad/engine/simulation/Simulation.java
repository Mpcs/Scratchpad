package com.mpcs.scratchpad.engine.simulation;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.logging.Logger;
import com.mpcs.scratchpad.engine.Context;
import com.mpcs.scratchpad.engine.rendering.ArrayMesh3D;
import com.mpcs.scratchpad.engine.scene.nodes.Node;
import com.mpcs.scratchpad.engine.scene.Scene;
import com.mpcs.scratchpad.engine.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.engine.simulation.loops.SimulationLoop;
import com.mpcs.scratchpad.engine.simulation.loops.MinecraftLoop;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.RuntimeException;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable {

    private final static boolean DISPLAY_TPS = false;

    public AtomicBoolean running = new AtomicBoolean(false);
    final Context context;
    private Scene scene;

    public Simulation(Context context) {
        this.scene = context.getResourceManager().loadInitialScene();
        this.context = context;
    }

    private long framesSinceStart = 0;
    @Override
    public void run() {
        Logger.log(Thread.currentThread().getName());
        running.set(true);

        SimulationLoop simulationLoop = new MinecraftLoop(this::update, 60);
        while(running.get()) {
            simulationLoop.tickAndWait();
            framesSinceStart++;
            if(DISPLAY_TPS && framesSinceStart % 60 == 0) {
                Logger.debug(String.valueOf(simulationLoop.getTPS()));
            }
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
