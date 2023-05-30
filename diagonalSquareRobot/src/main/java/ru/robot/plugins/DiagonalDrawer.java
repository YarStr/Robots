package ru.robot.plugins;

import ru.robot.interfaces.Drawer;

import java.awt.*;


public class DiagonalDrawer implements Drawer {

    @Override
    public void draw(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(Color.GREEN);
        graphics2D.fillRect(x, y, width, height);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, width, height);
    }
}
