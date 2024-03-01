package com.mpcs.scratchpad.core.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.service.EngineService;
import com.mpcs.scratchpad.core.service.ServicePriority;

import java.util.ArrayList;
import java.util.List;

public class InputManager implements EngineService, KeyListener, MouseListener {

    private List<Short> pressedKeys = new ArrayList<>();

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

    @Override
    public void init(Context context) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public ServicePriority getPriority() {
        return ServicePriority.RESOURCES;
    }
}