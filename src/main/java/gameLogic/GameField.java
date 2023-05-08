package gameLogic;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ThreadLocalRandom;

public class GameField {
    private int width;
    private int height;
    private final Target target;
    private final RobotEnemy robotEnemy;
    public final UserRobot userRobot;
    public static String WIN_USER = "Winner user";
    public static String WIN_ENEMY = "Winner enemy";
    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);
    private boolean start = false;


    public GameField(int width, int height) {
        updateFieldSize(width, height);
        target = new Target(50, 50);
        robotEnemy = new RobotEnemy(0, 0, 45);
        userRobot = new UserRobot(0, 0, 45);
        userRobot.correctSizeField(width, height);
    }

    public void addBundleChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(WIN_ENEMY, listener);
        propChangeDispatcher.addPropertyChangeListener(WIN_USER, listener);
    }

    public void startGame(){
        start = true;
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
//        target.correctPosition(width, height);
        robotEnemy.correctPosition(width, height);
        userRobot.correctSizeField(width, height);
    }

    public void onModelUpdateEvent() {
        if(start){
            if (robotEnemy.getDistanceToTarget(target) < 0.5){
                propChangeDispatcher.firePropertyChange(WIN_ENEMY, null, null);
                generateNewTarget();
            } else if (userRobot.getDistanceToTarget(target) < 3){
                propChangeDispatcher.firePropertyChange(WIN_USER, null, null);
                generateNewTarget();
            }
            robotEnemy.turnToTarget(target);
            robotEnemy.move(width, height);
        }
    }

    public void generateNewTarget(){
        int newX = ThreadLocalRandom.current().nextInt(1, width);
        int newY = ThreadLocalRandom.current().nextInt(1, height);
        Point point = new Point();
        point.setLocation(newX, newY);
        changeTargetPosition(point);
    }

    public int getRobotEnemyX() {
        return robotEnemy.getRoundedX();
    }

    public int getRobotEnemyY() {
        return robotEnemy.getRoundedY();
    }

    public double getRobotEnemyDirection() {
        return robotEnemy.direction;
    }

    public int getTargetX() {
        return target.x;
    }

    public int getTargetY() {
        return target.y;
    }

    public int getUserRobotX() {
        return userRobot.getRoundedX();
    }

    public int getUserRobotY() {
        return userRobot.getRoundedY();
    }

    public double getUserRobotDirection() {
        return userRobot.direction;
    }
}
