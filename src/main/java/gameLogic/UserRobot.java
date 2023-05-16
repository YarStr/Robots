package gameLogic;

import gui.internalWindows.UserRobotDirection;

import java.util.HashMap;

public class UserRobot{
    private final double rotationSpeed = 0.1;
    private final double velocity = 1;

    public volatile double fieldWidth;
    public volatile double fieldHeight;

    public volatile double x;
    public volatile double y;

    public UserRobot(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public void move(HashMap<UserRobotDirection, Boolean> directionMove) {
        if (directionMove.get(UserRobotDirection.MOVE_UP)) {
            goForward();
        }
        if (directionMove.get(UserRobotDirection.MOVE_DOWN)) {
            goDown();
        }
        if (directionMove.get(UserRobotDirection.MOVE_LEFT)) {
            goLeft();
        }
        if (directionMove.get(UserRobotDirection.MOVE_RIGHT)) {
            goRight();
        }
        correctPosition();
    }

    public void goRight() {
        double newX = x + velocity;
        x = newX;
//        double newDirection = directionMove + rotationSpeed;
//        updateDirection(newDirection);
    }

    public void goLeft() {
        double newX = x - velocity;
        x = newX;
//        double newDirection = directionMove + rotationSpeed;
//        updateDirection(newDirection);
    }
//
//    public void turnLeft() {
//        double newDirection = directionMove - rotationSpeed;
//        updateDirection(newDirection);
//    }
//
//    private void updateDirection(double newDirection) {
//        directionMove = asNormalizedRadians(newDirection);
//    }

    public void goForward() {
        double newY = y - velocity;
        y = newY;
//        double newX = x + velocity * Math.cos(directionMove);
//        double newY = y + velocity * Math.sin(directionMove);
//        x = newX;
//        y = newY;
    }

    public void goDown() {
        double newY = y + velocity;
        y = newY;
//        double newX = x - velocity * Math.cos(directionMove);
//        double newY = y - velocity * Math.sin(directionMove);
//        x = newX;
//        y = newY;
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
    public double applyLimits(double value, double max) {
        double zero_value = 0;
        return Math.min(Math.max(value, zero_value), max);
    }

    public void correctFieldSize(int width, int height) {
        this.fieldWidth = width;
        this.fieldHeight = height;
    }

    public double getDistanceToTarget(Target target) {
        double diffX = x - target.x;
        double diffY = y - target.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public int getRoundedX() {
        return round(x);
    }

    public int getRoundedY() {
        return round(y);
    }

    public static int round(double value) {
        return (int) (value + 0.5);
    }
}
