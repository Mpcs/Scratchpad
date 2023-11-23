package com.mpcs.scratchpad;

import com.mpcs.config.ConfigManager;
import com.mpcs.config.annotations.Config;
import com.mpcs.logging.Logger;
import com.mpcs.util.PerformanceMonitor;

import javax.swing.*;
import java.awt.*;

public class Main {

    @Config
    public static int win_w = 800;
    @Config
    public static int win_h = 600;

    //TODO: Consider replacing AWT with Jogl's NEWT and/or improve the engine window
    public static void main(String[] args) {

        if (true)
        return;
        System.out.println("SciPad is starting!");
        PerformanceMonitor monitor = new PerformanceMonitor();
        monitor.start();
        ConfigManager.init();
        Logger.log("Config init took " + monitor.getTime() + " ms");


        JFrame frame = new JFrame("Scratchpad");
        frame.setSize(win_w,win_h);

        frame.setLayout(new BorderLayout());
        Engine engine = new Engine();

        frame.add(engine.getGlJPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        String s = 50 + 30 + "test" + 40 + 40;
        Logger.log(s);

        engine.getGlWindow().setVisible(true);
    }
}
