package ru.robot.plugins;

import ru.robot.interfaces.Drawer;
import ru.robot.interfaces.Plugin;
import ru.robot.interfaces.Robot;

public class YellowDotRobot implements Plugin {
    @Override
    public Robot getRobot() {
        return new DotRobot(-100, -100, 10, 10);
    }

    @Override
    public Drawer getDrawer() {
        return new YellowDrawer();
    }
}
