package com.mpcs.scratchpad.engine;

import com.jogamp.newt.event.*;
import com.mpcs.logging.Logger;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InputManager implements KeyListener, MouseListener {

    private Context context;
    private List<Short> pressedKeys = new ArrayList<>();

    public InputManager(Context context) {
        this.context = context;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!e.isAutoRepeat())
            pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!e.isAutoRepeat())
            pressedKeys.remove(Short.valueOf(e.getKeyCode()));
    }

    public boolean isKeyPressed(short keyCode) {
        return pressedKeys.contains(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }
}







//private final Simulation simulation;
//
//        KbListener(Simulation simulation) {
//            this.simulation = simulation;
//        }
//        @Override
//        public void keyPressed(KeyEvent e) {
//            if (e.getKeyChar() == 'a') {
//                Logger.log("a pressed"); //placeholder for testing the functionality
//            }
//        }
//    }
//
//    public static class MouseListener extends MouseAdapter {
//        private final Simulation simulation;
//
//        MouseListener(Simulation simulation) {
//            this.simulation = simulation;
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            Logger.log("Mouse clicked"); //placeholder for testing the functionality
//        }
//    }