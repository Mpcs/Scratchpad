package com.mpcs.scratchpad.core.simulation.loops;

import java.util.function.Consumer;

/**
 * Loop implementation inspired by minecraft.
 */

public class MinecraftLoop extends SimulationLoop {

    private int targetTPS;
    double nanosecondsPerTick;
    long lastime = System.nanoTime();
    double deltaTime = 0;
    int framesPassedInCurrentSecond = 0;
    double time = System.currentTimeMillis();

    int lastTPSValue = 0;

    public MinecraftLoop(Consumer<Double> updateFunction, int targetTPS) {
        super(updateFunction);
        this.targetTPS = targetTPS;
        nanosecondsPerTick = (double) 1000000000 / targetTPS;
    }

    @Override
    public void tickAndWait() {
        long now = System.nanoTime();
        deltaTime += (now - lastime) / nanosecondsPerTick;
        lastime = now;
        if(deltaTime >= 1) {
            super.update(nanosecondsPerTick/1000000);
            framesPassedInCurrentSecond++;
            deltaTime--;
            if(System.currentTimeMillis() - time >= 1000) { // every second
                //System.out.println("tps:" + framesPassedInCurrentSecond);
                time += 1000;
                lastTPSValue = framesPassedInCurrentSecond;
                framesPassedInCurrentSecond = 0;
            }
        }
    }

    @Override
    public double getTPS() {
        return lastTPSValue;
    }
    
    @Override
    public boolean controlsRendering() {
        return false;
    }
}
