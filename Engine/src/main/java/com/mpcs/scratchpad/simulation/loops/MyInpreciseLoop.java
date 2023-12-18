package com.mpcs.scratchpad.simulation.loops;

import com.jogamp.newt.event.KeyEvent;
import com.mpcs.logging.Logger;
import com.mpcs.scratchpad.engine.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.engine.scene.nodes.Node;
import com.mpcs.util.LimitedQueue;

import java.util.Queue;
import java.util.function.Consumer;

public class MyInpreciseLoop extends SimulationLoop{


    private static final int FRAMES_PER_SEC = 60;
    private static final double MILIS_PER_FRAME = (1000.0/FRAMES_PER_SEC); // 1000 miliseconds per second

    private Queue<Double> lastFrameTimes = new LimitedQueue<>(FRAMES_PER_SEC);

    public MyInpreciseLoop(Consumer<Double> updateFunction) {
        super(updateFunction);
    }

    private double lastTickTime = 0;

    @Override
    public void tickAndWait() {
            double frameStartTime = System.currentTimeMillis();
            update(0);
            double frameEndTime = System.currentTimeMillis();
            double frameDuration = frameEndTime - frameStartTime;
            lastFrameTimes.add(frameDuration);
            try {
                long sleepTime = (long) (MILIS_PER_FRAME - frameDuration);
                if (sleepTime >= 0) {
                    Thread.sleep(sleepTime);
                }
                lastFrameTimes.add(System.currentTimeMillis() - frameStartTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }


    @Override
    public double getTPS() {
        double timeOfLastTicks = lastFrameTimes.stream().mapToDouble(Double::doubleValue).sum()/1000;
        return lastFrameTimes.size() / timeOfLastTicks;
    }
}
