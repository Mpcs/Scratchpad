package com.mpcs.scratchpad;

import com.mpcs.scratchpad.config.Config;

import java.awt.*;

public class Grid {

    public static void draw(Graphics g) {
        g.setColor(Color.BLUE);
        for  (int x = Config.grid_offset_x% Config.grid_spacing; x <= Main.win_w; x+= Config.grid_spacing) {
            g.drawLine(x, 0,x, Main.win_h);
        }
        for (int y = Config.grid_offset_y%Config.grid_spacing; y<= Main.win_h; y+= Config.grid_spacing) {
            g.drawLine(0, y,Main.win_w, y);
        }
    }

    //private static void draw_line(Graphics g)
}
