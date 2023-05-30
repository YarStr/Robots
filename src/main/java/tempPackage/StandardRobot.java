package tempPackage;

import logic.game.Robot;
import logic.game.RobotType;
import logic.game.UserRobotDirection;

import java.beans.PropertyChangeListener;
import java.util.HashMap;

public class StandardRobot extends Robot {
    private final double velocity = 0.8;


    public static String CHANGE_COORDINATES = "coordinates  of user robot changed";


    public StandardRobot(double x, double y, int robotWidth, int robotHeight) {
        super(x, y, robotWidth, robotHeight);
    }


    public void move(HashMap<UserRobotDirection, Boolean> directionMove) {
        if (directionMove.get(UserRobotDirection.MOVE_UP)) {
            goForward();
            goLeft();
        }
        if (directionMove.get(UserRobotDirection.MOVE_DOWN)) {
            goDown();
            goRight();
        }
        if (directionMove.get(UserRobotDirection.MOVE_LEFT)) {
            goLeft();
            goDown();
        }
        if (directionMove.get(UserRobotDirection.MOVE_RIGHT)) {
            goRight();
            goForward();
        }
        correctPosition();
        propChangeDispatcher.firePropertyChange(CHANGE_COORDINATES, null, RobotType.USER);
    }


    public void goRight() {
        double newX = x + velocity;
        lastX = x;
        x = newX;
    }

    public void goLeft() {
        double newX = x - velocity;
        lastX = x;
        x = newX;
    }

    public void goForward() {
        double newY = y - velocity;
        lastY = y;
        y = newY;
    }

    public void goDown() {
        double newY = y + velocity;
        lastY = y;
        y = newY;
    }

    private void correctPosition() {
        if (fieldWidth != 0) {
            double newX = applyLimits(x, fieldWidth);
            x = newX;
        }

        if (fieldHeight != 0) {
            double newY = applyLimits(y, fieldHeight);
            y = newY;
        }
    }


    public void addDataChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(CHANGE_COORDINATES, listener);
    }
}
