package ru.robot.plugins;

import ru.robot.interfaces.Drawer;
import ru.robot.interfaces.Plugin;
import ru.robot.interfaces.Robot;

public class BigRobot implements Plugin {
    @Override
    public Robot getRobot() {
        return new SquareRobot(-100, -100, 40, 40);
    }

    @Override
    public Drawer getDrawer() {
        return new SquareWithEyesDrawer();
    }
}
