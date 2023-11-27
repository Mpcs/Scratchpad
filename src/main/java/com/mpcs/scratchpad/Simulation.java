package com.mpcs.scratchpad;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.mpcs.logging.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Runnable{

    AtomicBoolean running = new AtomicBoolean(false);
    final Engine hostEngine;
    final KbListener kbListener;
    final MouseListener mouseListener;

    public Simulation(Engine hostEngine) {
        this.hostEngine = hostEngine;
        this.mouseListener = new MouseListener(this);
        this.kbListener = new KbListener(this);
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
    public static class KbListener extends KeyAdapter {

        private final Simulation simulation;

        KbListener(Simulation simulation) {
            this.simulation = simulation;
        }
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                Logger.log("a pressed"); //placeholder for testing the functionality
            }
        }
    }

    public static class MouseListener extends MouseAdapter {
        private final Simulation simulation;

        MouseListener(Simulation simulation) {
            this.simulation = simulation;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Logger.log("Mouse clicked"); //placeholder for testing the functionality
        }
    }
}
