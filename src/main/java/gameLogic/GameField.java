package gameLogic;

import java.awt.*;

public class GameField {
    private int width;
    private int height;
    public Target target;
    public Robot robot;


    public GameField(int width, int height) {
        updateFieldSize(width, height);
        target = new Target(width / 2, height / 2);
        robot = new Robot(0, 0, 45);
    }

    public void updateFieldSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void changeTargetPosition(Point point){
        target.x = point.x;
        target.y = point.y;
    }

    public void onModelUpdateEvent() {
        if (robot.getDistanceToTarget(target) < 0.5)
            return;
        robot.turnToTarget(target);
        robot.move(width, height);
    }
}
