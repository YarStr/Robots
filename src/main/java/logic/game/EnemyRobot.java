package logic.game;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EnemyRobot {
    public volatile double x;
    public volatile double y;

    public volatile double direction;

    public final double maxVelocity = 0.05;
    public final double maxAngularVelocity = 0.015;
    public final double duration = 10;
    public double angle = 0;

    public double velocity = maxVelocity;
    public double angularVelocity = 0;
    public volatile int robotWidth;
    public volatile int robotHeight;

    public static String CHANGE_COORDINATES = "coordinates  of enemy robot changed";
    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);


    public EnemyRobot(double x, double y, double direction, int robotWidth, int robotHeight) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.robotWidth = robotWidth;
        this.robotHeight = robotHeight;
    }

    public void turnToTarget(Target target) {
        double angleToTarget = getAngleToTarget(x, y, target.x, target.y);
        double newAngle = asNormalizedRadians(angleToTarget - direction);
        updateAngularVelocity(angleToTarget);
        angle = newAngle;
    }

    public double getAngleToTarget(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void updateAngularVelocity(double angleToTarget) {
        if (Math.abs(direction - angleToTarget) < 10e-7) {
            angularVelocity = 0;
        } else if (direction >= Math.PI) {
            if (direction - Math.PI < angleToTarget && angleToTarget < direction)
                angularVelocity = -maxAngularVelocity;
            else
                angularVelocity = maxAngularVelocity;
        } else {
            if (direction < angleToTarget && angleToTarget < direction + Math.PI)
                angularVelocity = maxAngularVelocity;
            else
                angularVelocity = -maxAngularVelocity;
        }
    }

    public void move(int width, int height) {
        updateVelocity();
        updateDirection();
        updateCoordinates();
        correctPosition(width, height);
        propChangeDispatcher.firePropertyChange(CHANGE_COORDINATES, null, RobotType.ENEMY);
    }

    private void updateVelocity() {
        velocity = applyLimits(velocity, maxVelocity);
    }

    private void updateDirection() {
        double newDirection = asNormalizedRadians(direction + Math.min(angle, angularVelocity) * duration);
        direction = newDirection;
    }

    private void updateCoordinates() {
        double newX = x + velocity * duration * Math.cos(direction);
        double newY = y + velocity * duration * Math.sin(direction);
        x = newX;
        y = newY;
    }

    public void correctPosition(int width, int height) {
        if (width != 0) {
            double newX = applyLimits(x, width);
            x = newX;
        }
        if (height != 0) {
            double newY = applyLimits(y, height);
            y = newY;
        }
    }

    public double asNormalizedRadians(double angle) {
        return (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
    }

    public double applyLimits(double value, double max) {
        double zero_value = 0;
        return Math.min(Math.max(value, zero_value), max);
    }

    public int getRoundedX() {
        return round(x);
    }

    public int getRoundedY() {
        return round(y);
    }

    public int round(double value) {
        return (int) (value + 0.5);
    }

    public int getDistanceToTarget(Target target) {
        double diffX = x - target.x;
        double diffY = y - target.y;
        int distance = round(Math.sqrt(diffX * diffX + diffY * diffY));
        return Math.max(distance, 0);
    }

    public void addDataChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(CHANGE_COORDINATES, listener);
    }
}
