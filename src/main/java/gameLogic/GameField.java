package gameLogic;

import java.awt.*;

public class GameField {
    private int width;
    private int height;
    private final Target target;
    private final Robot robot;


    public GameField(int width, int height) {
        updateFieldSize(width, height);
        target = new Target(0, 0);
        robot = new Robot(0, 0, 45);
    }

    public void updateFieldSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void changeTargetPosition(Point point) {
        target.x = point.x;
        target.y = point.y;
    }

    public void applyLimits(int updatedWidth, int updatedHeight) {
        updateFieldSize(updatedWidth, updatedHeight);
        target.correctPosition(width, height);
        robot.correctPosition(width, height);
    }

    public void onModelUpdateEvent() {
        if (robot.getDistanceToTarget(target) < 0.5)
            return;
        robot.turnToTarget(target);
        robot.move();
    }

    public int getRobotX() {
        return robot.getRoundedX();
    }

    public int getRobotY() {
        return robot.getRoundedY();
    }

    public double getRobotDirection() {
        return robot.direction;
    }

    public int getTargetX() {
        return target.x;
    }

    public int getTargetY() {
        return target.y;
    }
}
