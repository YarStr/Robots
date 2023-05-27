package logic.game;

import java.beans.PropertyChangeSupport;

public class Robot {
    public volatile double x;
    public volatile double y;

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

    public int getDistanceToTarget(Target target) {
        double diffX = getCenterX() - target.x;
        double diffY = getCenterY() - target.y;
        int distance = round(Math.sqrt(diffX * diffX + diffY * diffY)) - 10;
        return Math.max(distance, 0);
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
        return getRoundedX() + robotWidth / 2;
    }

    public int getCenterY() {
        return getRoundedY() + robotHeight / 2;
    }
}
