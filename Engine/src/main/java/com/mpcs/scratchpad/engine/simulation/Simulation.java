package com.mpcs.scratchpad.engine.simulation;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.logging.Logger;
import com.mpcs.math.Vector3;
import com.mpcs.scratchpad.engine.Context;
import com.mpcs.scratchpad.engine.rendering.ArrayMesh3D;
import com.mpcs.scratchpad.engine.scene.nodes.Node;
import com.mpcs.scratchpad.engine.scene.Scene;
import com.mpcs.scratchpad.engine.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.engine.simulation.loops.SimulationLoop;
import com.mpcs.scratchpad.engine.simulation.loops.MinecraftLoop;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable{

    private final static boolean DISPLAY_TPS = false;

    public AtomicBoolean running = new AtomicBoolean(false);
    final Context context;
    private Scene scene;

    float[] vertices = {
            0.5f,  0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,  // top right
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,// bottom right
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,    // bottom left
            -0.5f,  0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,   // top left
    };

    float[] verticesNoIndices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };
    int[] indices = {  // note that we start from 0!
            0, 1, 3,   // first triangle
            1, 2, 3,    // second triangle
    };

    Vector3[] cubePositions = {
            new Vector3( 0.0f,  0.0f,  0.0f),
    new Vector3( 2.0f,  5.0f, -15.0f),
    new Vector3(-1.5f, -2.2f, -2.5f),
    new Vector3(-3.8f, -2.0f, -12.3f),
    new Vector3( 2.4f, -0.4f, -3.5f),
    new Vector3(-1.7f,  3.0f, -7.5f),
    new Vector3( 1.3f, -2.0f, -2.5f),
    new Vector3( 1.5f,  2.0f, -2.5f),
    new Vector3( 1.5f,  0.2f, -1.5f),
    new Vector3(-1.3f,  1.0f, -1.5f)
};


    public Simulation(Context context) {
        Node rootNode = new Node();
        ArrayMesh3D mesh3D = new ArrayMesh3D(verticesNoIndices);

        for (Vector3 cubePosition : cubePositions) {
            Model3DNode newBoxNode = new Model3DNode(mesh3D);
            newBoxNode.setRelativePosition(cubePosition);
            rootNode.addChild(newBoxNode);
        }

        this.scene = new Scene(rootNode);
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
            if (node instanceof Model3DNode model3DNode) {
                model3DNode.rotation += 1;
            }
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
