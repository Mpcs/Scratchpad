package com.mpcs.scratchpad;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.mpcs.config.ConfigManager;
import com.mpcs.config.annotations.Config;
import com.mpcs.logging.Logger;
import com.mpcs.util.PerformanceMonitor;

public class Main {

    @Config
    public static int default_win_w = 800;
    @Config
    public static int default_win_h = 600;

    @Config
    public static String default_window_title = "Scratchpad Engine";
    private static PerformanceMonitor performanceMonitor;
    private static Engine engine;

    public static void main(String[] args) {
        Logger.log("Scratchpad is starting!");
        performanceMonitor = new PerformanceMonitor();

        performanceMonitor.start();
        ConfigManager.init();
        Logger.debug("Config init took " + performanceMonitor.getTime() + " ms");

        engine = new Engine(true);
        GLWindow glWindow = engine.getGlWindow();
        glWindow.setSize(default_win_w, default_win_h);

        glWindow.setTitle(default_window_title);
        glWindow.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
        glWindow.addWindowListener(new CloseWindowListener());

        glWindow.setVisible(true);
    }

    static class CloseWindowListener extends WindowAdapter {
        @Override
        public void windowDestroyed(WindowEvent e) {
            engine.stop();
        }
    }
}
