package com.mpcs.scratchpad;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;

public class Engine {
    final GLJPanel glCanvas;
    final GLWindow window;
    public Engine() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCanvas = new GLJPanel(glCapabilities);
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);

        glCanvas.addGLEventListener(new MyGLEventListenerEx1());
        window = GLWindow.create(NewtFactory.createScreen(jfxNewtDisplay, 0), glCapabilities);
        window.addGLEventListener(new MyGLEventListenerEx1());
    }

    public GLJPanel getGlJPanel() {
        return glCanvas;
    }

    public GLWindow getGlWindow() {
        return window;
    }


}
