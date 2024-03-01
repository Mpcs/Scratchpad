package com.mpcs.scratchpad.editor;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.*;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.Engine;
import com.mpcs.scratchpad.core.EngineBuilder;
import com.mpcs.scratchpad.core.EngineCreationException;
import com.mpcs.scratchpad.core.rendering.Renderer;
import javafx.geometry.Bounds;

import java.io.IOException;

public class EngineCanvas extends NewtCanvasJFX {
    /**
     * Instantiates an Engine and prepares to render its GLwindow.
     */
    public final Engine engine;

    public EngineCanvas() {
        super(null);
        try {
            engine = new EngineBuilder().create("./testProject/");
        } catch (IOException | EngineCreationException e) {
            throw new RuntimeException(e);
        }

        GLWindow glWindow = engine.getContext().getInstanceOf(Renderer.class).getGlWindow();

        this.setNEWTChild(glWindow);

        //fix the NewtCanvasJFX being unable to give up focus
        EngineCanvas self = this;
        glWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                self.requestFocus();
            }
        });
        this.focusedProperty().addListener((sth, oldValue, newValue) -> {
            if (!newValue) {
                glWindow.setVisible(false);
                glWindow.setVisible(true);
            }
        });
    }


    @Override
    public void destroy() {
        this.engine.stop();
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
