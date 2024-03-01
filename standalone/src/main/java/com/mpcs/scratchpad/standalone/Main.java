package com.mpcs.scratchpad.standalone;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.Engine;
import com.mpcs.scratchpad.core.rendering.Renderer;

import java.io.IOException;

public class Main {

    public static final int DEFAULT_WINDOW_WIDTH = 800;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;
    public static final String DEFAULT_WINDOW_TITLE = "Scratchpad Engine";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Provide project path as an argument.");
            return;
        }
        String projectPath = args[0];

        Engine engine = new Engine(projectPath, true); // TODO: Builder?
        GLWindow glWindow = engine.getContext().getInstanceOf(Renderer.class).getGlWindow();
        glWindow.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        glWindow.setResizable(false);

        glWindow.setTitle(DEFAULT_WINDOW_TITLE);
        glWindow.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

        glWindow.setVisible(true);
    }
}