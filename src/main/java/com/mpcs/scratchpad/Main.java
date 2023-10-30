package com.mpcs.scratchpad;

import com.mpcs.config.ConfigManager;
import com.mpcs.config.ConfigVars;
import com.mpcs.config.annotations.Config;

import java.lang.reflect.Field;

public class Main {

    @Config
    public static int win_w = 100;
    @Config
    public static int win_h = 200;
    public static void main(String[] args) {
        System.out.println(win_w);
        ConfigManager.init();

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
