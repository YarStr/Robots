package logic.game;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameField {
    private static final int WIN_SCORE_POINTS = 5;
    private int width;
    private int height;

    private final Target target;
    private final RobotEnemy robotEnemy;
    public final UserRobot userRobot;

    public static String SCORE_CHANGED = "Score changed";
    public static String GAME_OVER = "Game over";

    private final PropertyChangeSupport scoreChangeDispatcher = new PropertyChangeSupport(this);

    private boolean isGameOn = false;
    private final HashMap<RobotType, Integer> score = new HashMap<>();

    private final HashMap<UserRobotDirection, Boolean> directionMove = new HashMap<>();

    public GameField(int width, int height) {
        updateFieldSize(width, height);
        target = new Target(50, 50);
        robotEnemy = new RobotEnemy(0, 0, 45);
        userRobot = new UserRobot(0, 0);
        userRobot.correctFieldSize(width, height);
        setDirectionMove();
    }

    private void setScore() {
        for (RobotType robot : RobotType.values()) {
            setRobotScore(robot, 0);
        }
    }

    private void setDirectionMove() {
        for (UserRobotDirection state : UserRobotDirection.values()) {
            directionMove.put(state, false);
        }
    }

    public void addScoreChangeListener(PropertyChangeListener listener) {
        scoreChangeDispatcher.addPropertyChangeListener(SCORE_CHANGED, listener);
    }

    public void addGameOverListener(PropertyChangeListener listener) {
        scoreChangeDispatcher.addPropertyChangeListener(GAME_OVER, listener);
    }

    public void startGame() {
        isGameOn = true;
        setScore();
    }

    public void stopGame(RobotType winner) {
        isGameOn = false;
        setDirectionMove();
        scoreChangeDispatcher.firePropertyChange(GAME_OVER, null, winner);
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
        robotEnemy.correctPosition(width, height);
        userRobot.correctFieldSize(width, height);
    }

    public void updateDirection(UserRobotDirection direction, Boolean state) {
        directionMove.put(direction, state);
    }

    public void onModelUpdateEvent() {
        if (isGameOn) {
            robotEnemy.turnToTarget(target);
            robotEnemy.move(width, height);
            userRobot.move(directionMove);

            double enemyDistance = robotEnemy.getDistanceToTarget(target);
            double userDistance = userRobot.getDistanceToTarget(target);

            RobotType robotThatReachedTheTarget = getRobotThatReachedTheTarget(enemyDistance, userDistance);

            if (robotThatReachedTheTarget != null) {
                int scorePoints = score.get(robotThatReachedTheTarget) + 1;
                setRobotScore(robotThatReachedTheTarget, scorePoints);
                generateNewTarget();

                if (scorePoints >= WIN_SCORE_POINTS)
                    stopGame(robotThatReachedTheTarget);
            }
        }
    }

    private RobotType getRobotThatReachedTheTarget(double enemyDistance, double userDistance) {
        if (enemyDistance < 0.5) {
            return RobotType.ENEMY;
        }
        if (userDistance < 10) {
            return RobotType.USER;
        }
        return null;
    }

    private void setRobotScore(RobotType robot, int scorePoints) {
        score.put(robot, scorePoints);
        scoreChangeDispatcher.firePropertyChange(SCORE_CHANGED, robot, scorePoints);
    }

    private void generateNewTarget() {
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


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
