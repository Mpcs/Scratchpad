package com.mpcs.scratchpad;

import javax.swing.*;
import java.awt.*;

public class MyCanvas extends JPanel {
    public MyCanvas(int w, int h) {
        setBackground(Color.white);
        setSize(w,h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Grid.draw(g);
        g.setColor(Color.red);
        g.fillOval(75,75,150,75);
    }
}
