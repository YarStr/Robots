package gui;

public class Robot {
    private double x;
    private double y;
    private double direction;

    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.0015;

    public Robot(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updatePosition(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
