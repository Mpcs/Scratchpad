package com.mpcs.scratchpad.core.simulation.loops;
import java.util.function.Consumer;

public abstract class SimulationLoop {

    protected Consumer<Double> updateFunction;

    public SimulationLoop(Consumer<Double> updateFunction) {
        this.updateFunction = updateFunction;
    }
    protected void update(double deltaTime) {
        updateFunction.accept(deltaTime);
    }

    public abstract boolean controlsRendering();
    public abstract void tickAndWait();
    public abstract double getTPS();

}
