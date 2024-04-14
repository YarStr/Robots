package ru.robot.plugins;

import ru.robot.interfaces.Drawer;
import ru.robot.interfaces.Plugin;
import ru.robot.interfaces.Robot;

public class DiagonalSquareRobot implements Plugin {
    @Override
    public Robot getRobot() {
        return new SquareRobot(-100, -100, 20, 20);
    }

    @Override
    public Drawer getDrawer() {
        return new DiagonalDrawer();
    }
}
