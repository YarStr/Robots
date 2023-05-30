package logic.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class Robot {
    public volatile double x;
    public volatile double y;

    public int HP = 100;

    public double lastX;
    public double lastY;

    public volatile int robotWidth;
    public volatile int robotHeight;
    public volatile double fieldWidth;
    public volatile double fieldHeight;
    public final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    public Robot(double x, double y, int robotWidth, int robotHeight) {
        this.x = x;
        this.y = y;
        this.robotWidth = robotWidth;
        this.robotHeight = robotHeight;
    }

    public double applyLimits(double value, double max) {
        double zero_value = 0;
        return Math.min(Math.max(value, zero_value), max);
    }

    public void correctFieldSize(int width, int height) {
        fieldWidth = width;
        fieldHeight = height;
    }

    public int getDistanceToTarget(int targetX, int targetY) {
        double diffX = (getCenterX() + robotWidth/2) - targetX;
        double diffY = (getCenterY() + robotHeight/2) - targetY;
        int distance = round(Math.sqrt(diffX * diffX + diffY * diffY));
        return Math.max(distance, 0) - robotWidth/2;
    }

    public int round(double value) {
        return (int) (value + 0.5);
    }

    public int getRoundedX() {
        return round(x);
    }

    public int getRoundedY() {
        return round(y);
    }

    public int getCenterX() {
        return getRoundedX();
    }

    public int getCenterY() {
        return getRoundedY();
    }

    public void move(HashMap<UserRobotDirection, Boolean> directionMove) {
    }

    public void addDataChangeListener(PropertyChangeListener propertyChangeListener) {
    }
}
