package com.mpcs.scratchpad.engine;

import com.mpcs.logging.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable{

    AtomicBoolean running = new AtomicBoolean(false);
    final Context context;

    public Simulation(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Logger.log(Thread.currentThread().getName());
        running.set(true);
        while (running.get()) {
            try {
                Thread.sleep(100); // Placeholder
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // cleanup simulation and exit
    }

    //Listeners are being executed on the render thread (since the window captures the events. Not sure if that's going to be a problem in the future.

}
