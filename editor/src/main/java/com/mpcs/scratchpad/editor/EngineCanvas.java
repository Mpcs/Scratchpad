package com.mpcs.scratchpad.editor;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.*;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.mpcs.scratchpad.core.Engine;
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
            engine = new Engine("./testProject/", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setNEWTChild(engine.getGlWindow());

        //fix the NewtCanvasJFX being unable to give up focus
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
