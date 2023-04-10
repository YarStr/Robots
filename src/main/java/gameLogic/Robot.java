package gameLogic;

public class Robot {

    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.015;
    public static final double duration = 10;

    public volatile double x;
    public volatile double y;
    public volatile double direction;
    public double angle = 0;

    public double velocity = maxVelocity;
    public double angularVelocity = 0;


    public Robot(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turnToTarget(Target target) {
        double angleToTarget = getAngleToTarget(x, y, target.x, target.y);
        double newAngle = asNormalizedRadians(angleToTarget - direction);
        updateAngularVelocity(angleToTarget);
        angle = newAngle;
    }

    private static double getAngleToTarget(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        return (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
    }

    private void updateAngularVelocity(double angleToTarget) {
        if (Math.abs(direction - angleToTarget) < 10e-7) {
            angularVelocity = direction;
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

    private static double applyLimits(double value, double max) {
        double zero_value = 0;
        return Math.min(Math.max(value, zero_value), max);
    }

    public int getRoundedX() {
        return round(x);
    }

    public int getRoundedY() {
        return round(y);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    public double getDistanceToTarget(Target target) {
        double diffX = x - target.x;
        double diffY = y - target.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
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
}
