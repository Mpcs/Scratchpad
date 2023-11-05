package com.mpcs.scratchpad;

import com.mpcs.config.ConfigManager;
import com.mpcs.config.annotations.Config;
import com.mpcs.logging.Logger;
import com.mpcs.util.PerformanceMonitor;

public class Main {

    @Config
    public static int win_w = 100;
    @Config
    public static int win_h = 300;
    @Config
    public static int maya = 32;
    public static void main(String[] args) {
        PerformanceMonitor monitor = new PerformanceMonitor();
        monitor.start();
        ConfigManager.init();
        Logger.log("Config init took " + monitor.getTime() + " ms");
        Logger.log(Integer.toString(win_w));

/* System.out.println("SciPad is starting!");
        System.out.println(com.mpcs.config.Config.test);
        com.mpcs.config.Config.testik();
        System.out.println(com.mpcs.config.Config.test);

        JFrame frame = new JFrame("SciPad");
        frame.add(new com.mpcs.scratchpad.MyCanvas(win_w,win_h));

        frame.setLayout(null);
        frame.setSize(win_w,win_h);
        frame.setVisible(true); */
    }
}
