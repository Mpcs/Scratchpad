package com.mpcs.scratchpad.config;

import com.mpcs.config.annotations.AConfig;

import java.util.Arrays;

public final class Config {

    /**
     * CONFIG VALUES
     *
     * Initial values are the default, these are changed at runtime after loading the config file.
     */
    //@AConfig
    public static int test = 2;
    public static String aaa = "aaa";
    public static int grid_offset_y = 1;
    public static int grid_offset_x = 2;
    // Distance between tw
    public static int grid_spacing = 25;

    /**
     * Internal variables, should probably not be modified
     */

    int pedal = 2;

    public static void testik() {
        System.out.println("Declared: " + Arrays.toString(Config.class.getDeclaredFields()));
        System.out.println("Normal:   " + Arrays.toString(Config.class.getFields()));

        test = 3;

    }

}
