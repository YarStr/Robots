package gameLogic;

public class UserRobot extends Robot {
    private final double rotationSpeed = 0.1;
    private final double velocity = 2;

    public volatile double fieldWidth;
    public volatile double fieldHeight;

    public UserRobot(double x, double y, double direction) {
        super(x, y, direction);
    }


    public void turnRight() {
        double newDirection = direction + rotationSpeed;
        updateDirection(newDirection);
    }

    public void turnLeft() {
        double newDirection = direction - rotationSpeed;
        updateDirection(newDirection);
    }

    private void updateDirection(double newDirection) {
        direction = asNormalizedRadians(newDirection);
    }

    public void goForward() {
        double newX = x + velocity * Math.cos(direction);
        double newY = y + velocity * Math.sin(direction);
        x = newX;
        y = newY;
        correctPosition();
    }

    public void goBack() {
        double newX = x - velocity * Math.cos(direction);
        double newY = y - velocity * Math.sin(direction);
        x = newX;
        y = newY;
        correctPosition();
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

    public void correctFieldSize(int width, int height) {
        this.fieldWidth = width;
        this.fieldHeight = height;
    }
}
