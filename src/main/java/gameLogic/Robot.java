package gameLogic;

public class Robot {
    public volatile double x;
    public volatile double y;
    public volatile double direction;
    public Robot(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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

    public static int round(double value) {
        return (int) (value + 0.5);
    }
    public double getDistanceToTarget(Target target) {
        double diffX = x - target.x;
        double diffY = y - target.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

}
