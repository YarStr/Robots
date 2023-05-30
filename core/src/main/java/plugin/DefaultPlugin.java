package plugin;

import ru.robot.interfaces.Drawer;
import ru.robot.interfaces.Plugin;
import ru.robot.interfaces.Robot;

public class DefaultPlugin implements Plugin {

    @Override
    public Robot getRobot() {
        return new DefaultRobot();
    }

    @Override
    public Drawer getDrawer() {
        return new DefaultDrawer();
    }
}
