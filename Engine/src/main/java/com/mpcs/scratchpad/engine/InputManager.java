package com.mpcs.scratchpad.engine;

import com.jogamp.newt.event.*;

import java.util.ArrayList;
import java.util.List;

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