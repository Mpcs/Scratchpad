package com.mpcs.scratchpad.engine;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.mpcs.config.ConfigManager;
import com.mpcs.config.annotations.Config;
import com.mpcs.logging.Logger;
import com.mpcs.util.PerformanceMonitor;
import com.mpcs.scratchpad.engine.ResourceManager;
import java.io.IOException;
import com.mpcs.scratchpad.engine.ObjParser;

public class Main {

    @Config
    public static int default_win_w = 800;
    @Config
    public static int default_win_h = 600;

    @Config
    public static String default_window_title = "Scratchpad Engine";
    private static PerformanceMonitor performanceMonitor;
    private static Engine engine;

    public static void main(String[] args) throws IOException {
        ResourceManager resourceManager = new ResourceManager();

        new ObjParser().loadFromFile("./testProject/box.obj");

        String projectPath = "./testProject/";

        Logger.setShowThread(true);
        Logger.log("Scratchpad is starting!");
        performanceMonitor = new PerformanceMonitor();

        performanceMonitor.start();
        ConfigManager.init();
        Logger.debug("Config init took " + performanceMonitor.getTime() + " ms");

        engine = new Engine(projectPath, true);
        GLWindow glWindow = engine.getGlWindow();
        glWindow.setSize(default_win_w, default_win_h);
        glWindow.setResizable(false);

        glWindow.setTitle(default_window_title);
        glWindow.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

        glWindow.setVisible(true);
    }


}
