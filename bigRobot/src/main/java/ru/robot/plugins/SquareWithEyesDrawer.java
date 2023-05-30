package ru.robot.plugins;

import ru.robot.interfaces.Drawer;

import java.awt.*;


public class SquareWithEyesDrawer implements Drawer {

    @Override
    public void draw(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(Color.ORANGE);
        graphics2D.fillRect(x, y, width, height);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawOval(x + width / 4, y + height / 4, width / 2, height / 2);
        graphics2D.fillOval(x + width / 2, y + height / 2, width / 4, height / 4);
    }
}
