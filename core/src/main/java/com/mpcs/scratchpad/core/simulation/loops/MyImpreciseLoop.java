package com.mpcs.scratchpad.core.simulation.loops;

import com.mpcs.scratchpad.util.LimitedQueue;

import java.util.Queue;
import java.util.function.Consumer;

public class MyImpreciseLoop extends SimulationLoop{

    private static final int FRAMES_PER_SEC = 60;
    private static final double MILIS_PER_FRAME = (1000.0/FRAMES_PER_SEC); // 1000 milliseconds per second

    private Queue<Double> lastFrameTimes = new LimitedQueue<>(FRAMES_PER_SEC);

    public MyImpreciseLoop(Consumer<Double> updateFunction) {
        super(updateFunction);
    }

    private double lastTickTime = 0;

    @Override
    public void tickAndWait() {
            double frameStartTime = System.currentTimeMillis();
            update(1);
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

    @Override
    public boolean controlsRendering() {
        return false;
    }
}
