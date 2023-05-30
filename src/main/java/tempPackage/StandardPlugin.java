package tempPackage;

import logic.game.Robot;

public class StandardPlugin implements MyPlugin {

    @Override
    public Robot getRobot() {
        return new StandardRobot(-100, -100, 20, 20);
    }

    @Override
    public Drawer getDrawer() {
        return new StandardDrawer();
    }
}
