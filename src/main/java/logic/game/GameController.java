package logic.game;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameController {
    public static final String HP_CHANGED = "HP changed";
    public static String CHANGE_ENEMY_COORDINATES = "coordinates  of enemy robot changed";
    private static final int WIN_SCORE_POINTS = 1;
    private int widthField;

    private int heightField;

    public final Target target;

    private int level = 1;

    public final ArrayList<EnemyRobot> enemyRobots = new ArrayList<>();
    public final UserRobot userRobot;

    private RobotType zoomTarget = RobotType.USER;

    public static String SCORE_CHANGED = "Score changed";
    public static String GAME_OVER = "Game over";
    public static String LEVEL_CHANGED = "Level changed";

    private final PropertyChangeSupport scoreChangeDispatcher = new PropertyChangeSupport(this);

    private boolean isGameOn = false;
    private final HashMap<RobotType, Integer> score = new HashMap<>();

    private final HashMap<UserRobotDirection, Boolean> directionMove = new HashMap<>();


    public GameController(int width, int heightField) {
        updateFieldSize(width, heightField);
        target = new Target(-100, -100);
        userRobot = new UserRobot(-100, -100, 20, 20);
        userRobot.correctFieldSize(width, heightField);
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

    public void addScoreAndLevelChangeListener(PropertyChangeListener listener) {
        scoreChangeDispatcher.addPropertyChangeListener(SCORE_CHANGED, listener);
        scoreChangeDispatcher.addPropertyChangeListener(LEVEL_CHANGED, listener);
    }

    public void addGameOverListener(PropertyChangeListener listener) {
        scoreChangeDispatcher.addPropertyChangeListener(GAME_OVER, listener);
        scoreChangeDispatcher.addPropertyChangeListener(HP_CHANGED, listener);
    }

    public void addEnemyPositionChangedListener(PropertyChangeListener listener) {
        scoreChangeDispatcher.addPropertyChangeListener(CHANGE_ENEMY_COORDINATES, listener);
    }

    public void startGame() {
        isGameOn = true;
        updateEnemyRobotsAmount();
        changeTargetPosition(new Point(widthField / 2, heightField / 2));
        setRandomEnemiesPosition();
        setRandomUserPosition();
        setScore();
    }

    private void updateEnemyRobotsAmount() {
        int count = level - enemyRobots.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                enemyRobots.add(new EnemyRobot(-100, -100, 0, 10, 10));
            }

        } else {
            for (int i = 0; i < -count; i++) {
                enemyRobots.remove(0);
            }
        }
    }

    public void stopGame(RobotType winner) {
        isGameOn = false;
        setDirectionMove();
        scoreChangeDispatcher.firePropertyChange(GAME_OVER, null, winner);
    }

    public void updateFieldSize(int width, int height) {
        this.widthField = width;
        this.heightField = height;
    }

    public void changeTargetPosition(Point point) {
        target.x = point.x;
        target.y = point.y;
    }

    public void setRandomEnemiesPosition() {
        for (EnemyRobot robot : enemyRobots) {
            robot.x = getRandomCoordinateWithLimit(widthField);
            robot.y = getRandomCoordinateWithLimit(heightField);
        }
    }

    public void setRandomUserPosition() {
        userRobot.x = getRandomCoordinateWithLimit(widthField);
        userRobot.y = getRandomCoordinateWithLimit(heightField);
    }

    public void applyLimits(int updatedWidth, int updatedHeight) {
        updateFieldSize(updatedWidth, updatedHeight);
        target.correctPosition(widthField, heightField);
        for (EnemyRobot enemyRobot : enemyRobots)
            enemyRobot.correctPosition(widthField, heightField);
        userRobot.correctFieldSize(widthField, heightField);
    }

    public void updateDirection(UserRobotDirection direction, Boolean state) {
        directionMove.put(direction, state);
    }

    public void onModelUpdateEvent() {
        if (isGameOn) {
            userRobot.move(directionMove);
            getIntersectsOfRobots(userRobot);
            for (EnemyRobot enemyRobot : enemyRobots) {
                enemyRobot.turnToTarget(target);
                enemyRobot.move(widthField, heightField);
                getIntersectsOfRobots(enemyRobot);
            }

            if (userRobot.HP <= 0) {
                stopGame(RobotType.ENEMY);
                userRobot.HP = 100;
            }

            double enemyDistance = getMinEnemyDistanceToTarget();
            double userDistance = userRobot.getDistanceToTarget(target);

            RobotType robotThatReachedTheTarget = getRobotThatReachedTheTarget(enemyDistance, userDistance);

            if (robotThatReachedTheTarget != null) {
                int scorePoints = score.get(robotThatReachedTheTarget) + 1;
                setRobotScore(robotThatReachedTheTarget, scorePoints);

                if (scorePoints >= WIN_SCORE_POINTS) {
                    updateLevel(robotThatReachedTheTarget);
                    stopGame(robotThatReachedTheTarget);
                } else {
                    generateNewTarget();
                }
            }
        }
    }

    private void updateLevel(RobotType robotThatReachedTheTarget) {
        switch (robotThatReachedTheTarget) {
            case USER -> {
                if (level < 5) {
                    level += 1;
                }
            }
            case ENEMY -> level = 1;
        }
        scoreChangeDispatcher.firePropertyChange(LEVEL_CHANGED, null, level);
    }

    public double getMinEnemyDistanceToTarget() {
        double distance = Double.MAX_VALUE;
        int minDistanceRobotIndex = 0;

        for (int i = 0; i < enemyRobots.size(); i++) {
            EnemyRobot robot = enemyRobots.get(i);
            double robotDistance = robot.getDistanceToTarget(target);
            if (robotDistance < distance) {
                distance = robotDistance;
                minDistanceRobotIndex = i;
            }
        }

        scoreChangeDispatcher.firePropertyChange(
                CHANGE_ENEMY_COORDINATES,  enemyRobots.get(minDistanceRobotIndex), RobotType.ENEMY
        );
        return distance;
    }

    private RobotType getRobotThatReachedTheTarget(double enemyDistance, double userDistance) {
        if (enemyDistance < 0.5) {
            return RobotType.ENEMY;
        }
        if (userDistance < 0.5) {
            return RobotType.USER;
        }
        return null;
    }

    private void setRobotScore(RobotType robot, int scorePoints) {
        score.put(robot, scorePoints);
        scoreChangeDispatcher.firePropertyChange(SCORE_CHANGED, robot, scorePoints);
    }

    private void generateNewTarget() {
        int newX = getRandomCoordinateWithLimit(widthField);
        int newY = getRandomCoordinateWithLimit(heightField);
        changeTargetPosition(new Point(newX, newY));
    }

    private int getRandomCoordinateWithLimit(int limit) {
        return ThreadLocalRandom.current().nextInt(1, limit);
    }


    public int getRobotEnemyX() {
        return enemyRobots.get(0).getRoundedX();
    }

    public int getRobotEnemyY() {
        return enemyRobots.get(0).getRoundedY();
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
        return widthField;
    }

    public int getHeightField() {
        return heightField;
    }

    public RobotType getZoomTarget() {
        return zoomTarget;
    }

    public void changeZoomTarget() {
        switch (zoomTarget) {
            case USER -> zoomTarget = RobotType.ENEMY;
            case ENEMY -> zoomTarget = RobotType.USER;
        }
    }

    private void getIntersectsOfRobots(Robot robot1) {
        Rectangle modelOfRobot1 = new Rectangle(robot1.getRoundedX(), robot1.getRoundedY(),
                robot1.robotWidth, robot1.robotHeight);
        ArrayList<Robot> dynamicRobots = new ArrayList<>(enemyRobots);
        dynamicRobots.add(userRobot);
        for (Robot robot2 : dynamicRobots) {
            if (robot1 != robot2) {
                Rectangle modelOfRobot2 = new Rectangle(robot2.getRoundedX(), robot2.getRoundedY(),
                        robot2.robotWidth, robot2.robotHeight);
                if (modelOfRobot1.intersects(modelOfRobot2)) {
                    robot1.x = robot1.lastX;
                    robot1.y = robot1.lastY;
                    if (robot1 instanceof UserRobot) {
                        ((UserRobot) robot1).HP -= 1;
                        scoreChangeDispatcher.firePropertyChange(HP_CHANGED, null, ((UserRobot) robot1).HP);
                    }
                    if (robot2 instanceof UserRobot) {
                        ((UserRobot) robot2).HP -= 1;
                        scoreChangeDispatcher.firePropertyChange(HP_CHANGED, null, ((UserRobot) robot2).HP);
                    }
                }
            }
        }
    }
}
