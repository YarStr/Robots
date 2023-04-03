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
        double angleDifference = asNormalizedRadians(newAngle - angle);

        updateAngularVelocity(angleToTarget, angleDifference);
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

    private void updateAngularVelocity(double angleToTarget, double angleDifference) {
//        int angleToTargetInDegrees = (int) Math.round(Math.toDegrees(angleToTarget));
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

    public void move(int height, int width) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        double newDirection = asNormalizedRadians(direction + angularVelocity * duration);
        double newX = x + velocity * duration * Math.cos(newDirection);
        if (width != 0)
            newX = applyLimits(newX, 0, width);
        double newY = y + velocity * duration * Math.sin(newDirection);
        if (height != 0)
            newY = applyLimits(newY, 0, height);
        x=newX;
        y=newY;
        direction = newDirection;
    }
    private static double applyLimits(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
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




    //    public void move() {
//        applyVelocityLimits();
////        x = getNewX();
////        y = getNewY();
//        direction = getNewDirection();
//    }

//    private void applyVelocityLimits() {
//        velocity = applyLimits(velocity, 0, maxVelocity);
//        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
//    }
//

//
//    private double getNewX() {
//        double value = x + velocity / angularVelocity * (Math.sin(direction + Math.min(angle, angularVelocity)
//                * duration) - Math.sin(direction));
//        return Double.isFinite(value) ? value : x + velocity * duration * Math.cos(direction);
//    }
//
//    private double getNewY() {
//        double value = y - velocity / angularVelocity * (Math.cos(direction + Math.min(angle, angularVelocity)
//                * duration) - Math.cos(direction));
//        return Double.isFinite(value) ? value : y + velocity * duration * Math.sin(direction);
//    }
//
//    private double getNewDirection() {
//        return asNormalizedRadians(direction + Math.min(angle, angularVelocity) * duration);
//    }
}
