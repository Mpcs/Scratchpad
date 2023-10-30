package com.mpcs.scratchpad;

import com.mpcs.config.ConfigManager;
import com.mpcs.config.annotations.Config;

import java.awt.*;

public class Grid {

    @Config
    public static int grid_offset_y = 1;
    @Config
    public static int grid_offset_x = 2;
    // Distance between two grid lines
    @Config
    public static int grid_spacing = 25;

    public static void draw(Graphics g) {
        g.setColor(Color.BLUE);
        for  (int x = grid_offset_x% grid_spacing; x <= Main.win_w; x+= grid_spacing) {
            g.drawLine(x, 0,x, Main.win_h);
        }
        for (int y = grid_offset_y% grid_spacing; y<= Main.win_h; y+= grid_spacing) {
            g.drawLine(0, y,Main.win_w, y);
        }
    }

    //private static void draw_line(Graphics g)
}
