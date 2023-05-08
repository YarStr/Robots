package gameLogic;

public class RobotEnemy extends Robot{

    public final double maxVelocity = 0.05;
    public final double maxAngularVelocity = 0.015;
    public final double duration = 10;
    public double angle = 0;

    public double velocity = maxVelocity;
    public double angularVelocity = 0;

    public RobotEnemy(double x, double y, double direction) {
        super(x, y, direction);
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
}
