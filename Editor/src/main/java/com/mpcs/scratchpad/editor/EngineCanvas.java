package com.mpcs.scratchpad.editor;

import com.jogamp.nativewindow.NativeWindow;
import com.jogamp.nativewindow.javafx.JFXAccessor;
import com.jogamp.newt.Display;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.*;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.util.EDTUtil;
import com.jogamp.opengl.util.Animator;
import com.mpcs.logging.Logger;
import com.mpcs.scratchpad.Engine;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.Scene;

public class EngineCanvas extends NewtCanvasJFX {
    /**
     * Instantiates an Engine and prepares to render its GLwindow.
     */
    public final Engine engine;
    public final Animator animator;
    public EngineCanvas() {
        super(null);
        engine = new Engine();
        this.setNEWTChild(engine.getGlWindow());
        animator = new Animator(engine.getGlWindow());
        animator.start();

        //Broken focus dirty fix
        EngineCanvas self = this;
        engine.getGlWindow().addWindowListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                self.requestFocus();
            }
        });
        this.focusedProperty().addListener((sth, oldValue, newValue) -> {
            if (!newValue) {
                engine.getGlWindow().setVisible(false);
                engine.getGlWindow().setVisible(true);
            }
        });
    }

    @Override
    public void destroy() {
        animator.stop();
        super.destroy();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        final Bounds b = localToScene(getBoundsInLocal());
        int x = (int)b.getMinX();
        int y = (int)b.getMinY();
        int w = (int)b.getWidth();
        int h = (int)b.getHeight();

        Window newtChild = this.getNEWTChild();
        newtChild.setSize(w, h);
        newtChild.setPosition(x, y);
    }

}
