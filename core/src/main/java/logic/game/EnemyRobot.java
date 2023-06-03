package logic.game;


import ru.robot.interfaces.Robot;
import ru.robot.interfaces.RobotType;

import java.beans.PropertyChangeListener;

public class EnemyRobot extends Robot {

    public volatile double direction;

    public final double maxVelocity = 0.05;
    public final double maxAngularVelocity = 0.015;
    public final double duration = 10;
    public double angle = 0;

    public double velocity = maxVelocity;
    public double angularVelocity = 0;






    public EnemyRobot(double x, double y, double direction, int robotWidth, int robotHeight) {
        super(x, y, robotWidth, robotHeight);
        this.direction = direction;
    }

    public void turnToTarget(Target target) {
        double angleToTarget = getAngleToTarget(x + robotHeight/2, y + robotWidth/2, target.x, target.y);
        direction = angleToTarget;
    }

    public double getAngleToTarget(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void move(int width, int height) {
        updateCoordinates();
        correctPosition(width, height);
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
        lastX = x;
        x = newX;
        lastY = y;
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

}
