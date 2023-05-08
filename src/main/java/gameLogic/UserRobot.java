package gameLogic;

public class UserRobot extends Robot{
    private final double rotationSpeed = 0.3; // скорость вращения
    private final double velocity = 8; // скорость перемещения

    public volatile double widthField;
    public volatile double heightField;

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

    private void updateDirection(double newDirection){
        direction = asNormalizedRadians(newDirection);
    }

    public void goForward() {
        double newX = x + velocity * Math.cos(direction);
        double newY = y + velocity * Math.sin(direction);
        x = newX;
        y = newY;
        correctPositions();
    }

    public void goBack() {
        double newX = x - velocity;
        double newY = y - velocity;
        updateCoordinates(newX, newY);
    }

    private void updateCoordinates(double newX, double newY){
        x = newX * Math.cos(direction);
        y = newY * Math.sin(direction);
        correctPositions();
    }

    public void stop() {//вдруг понадобится
    }

    private void correctPositions() {
        if (widthField != 0) {
            double newX = applyLimits(x, widthField);
            x = newX;
        }

        if (heightField != 0) {
            double newY = applyLimits(y, heightField);
            y = newY;
        }
    }

    public void correctSizeField(int width, int height) {
        this.widthField = width;
        this.heightField = height;
    }
}
