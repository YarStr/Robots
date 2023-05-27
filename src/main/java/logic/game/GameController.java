package logic.game;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameController {
    private static final int WIN_SCORE_POINTS = 5;
    private int width;
    private int height;

    public final Target target;

    public final ArrayList<EnemyRobot> enemyRobots = new ArrayList<>();

    public final UserRobot userRobot;

    private Rectangle modelUserRobot;
    private Rectangle modelEnemyRobot;

    private RobotType zoomTarget = RobotType.USER;

    public static String SCORE_CHANGED = "Score changed";
    public static String GAME_OVER = "Game over";

    private final PropertyChangeSupport scoreChangeDispatcher = new PropertyChangeSupport(this);

    private boolean isGameOn = false;
    private final HashMap<RobotType, Integer> score = new HashMap<>();

    private final HashMap<UserRobotDirection, Boolean> directionMove = new HashMap<>();

    public GameController(int width, int height) {
        updateFieldSize(width, height);
        // TODO спавнить сущности изначально НЕ вне экрана
        target = new Target(-100, -100);
        setEnemyRobots();
        userRobot = new UserRobot(-100, -100, 20, 20);
        userRobot.correctFieldSize(width, height);
//        setModelsOfRobots();
        setDirectionMove();
    }

    public void setEnemyRobots() {
        for (int i = 0; i < 5; i++) {
            enemyRobots.add(new EnemyRobot(-100, -100, 0, 10, 10));
        }
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
        changeTargetPosition(new Point(width / 2, height / 2));
        setRandomEnemiesPosition();
        setRandomUserPosition();
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

    public void setRandomEnemiesPosition() {
        for (EnemyRobot robot : enemyRobots) {
            robot.x = getRandomCoordinateWithLimit(width);
            robot.y = getRandomCoordinateWithLimit(height);
        }
    }

    public void setRandomUserPosition() {
        userRobot.x = getRandomCoordinateWithLimit(width);
        userRobot.y = getRandomCoordinateWithLimit(height);
    }

    public void applyLimits(int updatedWidth, int updatedHeight) {
        updateFieldSize(updatedWidth, updatedHeight);
        target.correctPosition(width, height);
        for (EnemyRobot enemyRobot : enemyRobots)
            enemyRobot.correctPosition(width, height);
        userRobot.correctFieldSize(width, height);
    }

    public void updateDirection(UserRobotDirection direction, Boolean state) {
        directionMove.put(direction, state);
    }

    public void onModelUpdateEvent() {
        if (isGameOn) {
            for (EnemyRobot enemyRobot : enemyRobots) {
                enemyRobot.turnToTarget(target);
                enemyRobot.move(width, height);
            }
            userRobot.move(directionMove);

//            setModelsOfRobots();
//            getIntersectsOfRobots();

            if (userRobot.XP == 0) {
                stopGame(RobotType.ENEMY);
                userRobot.XP = 100;
            }

            double enemyDistance = getMinEnemyDistanceToTarget();
            double userDistance = userRobot.getDistanceToTarget(target);

            RobotType robotThatReachedTheTarget = getRobotThatReachedTheTarget(enemyDistance, userDistance);

            if (robotThatReachedTheTarget != null) {
                int scorePoints = score.get(robotThatReachedTheTarget) + 1;
                setRobotScore(robotThatReachedTheTarget, scorePoints);

                if (scorePoints >= WIN_SCORE_POINTS)
                    stopGame(robotThatReachedTheTarget);
                else
                    generateNewTarget();
            }
        }
    }

    public double getMinEnemyDistanceToTarget() {
        double distance = Double.MAX_VALUE;
        for (EnemyRobot robot : enemyRobots) {
            double robotDistance = robot.getDistanceToTarget(target);
            if (robotDistance < distance) {
                distance = robotDistance;
            }
        }
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
        int newX = getRandomCoordinateWithLimit(width);
        int newY = getRandomCoordinateWithLimit(height);
        changeTargetPosition(new Point(newX, newY));
    }

    private int getRandomCoordinateWithLimit(int limit) {
        return ThreadLocalRandom.current().nextInt(1, limit);
    }

    // TODO сделать отображение координат у всех Роботов
    public int getRobotEnemyX() {
        return enemyRobots.get(0).getRoundedX();
    }

    public int getRobotEnemyY() {
        return enemyRobots.get(0).getRoundedY();
    }

    // TODO устранить старые методы получения данных о Роботе
//    public int getRobotEnemyWidth() {
//        return enemyRobot.robotWidth;
//    }
//
//    public int getRobotEnemyHeight() {
//        return enemyRobot.robotHeight;
//    }
//
//    public double getRobotEnemyDirection() {
//        return enemyRobot.direction;
//    }

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

    // TODO убрать устаревшие методы получения характеристик Робота
//    public int getUserRobotWidth() {
//        return userRobot.robotWidth;
//    }
//
//    public int getUserRobotHeight() {
//        return userRobot.robotHeight;
//    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

//    public void getIntersectsOfRobots() {
//        if (modelUserRobot.intersects(modelEnemyRobot)) {
//            updateUserRobotXP();
//            pushBackUserRobot();
////            System.out.println(userRobot.XP);
//        }
//    }

    private void pushBackUserRobot() {
        int MIN_DISTANCE_BETWEEN_ROBOTS = 1;

        if (getUserRobotX() < getRobotEnemyX()) {
            int newX = getUserRobotX() - MIN_DISTANCE_BETWEEN_ROBOTS;
            userRobot.x = newX;
        } else {
            int newX = getUserRobotX() + MIN_DISTANCE_BETWEEN_ROBOTS;
            userRobot.x = newX;
        }
        if (getUserRobotY() < getRobotEnemyY()) {
            int newY = getUserRobotY() - MIN_DISTANCE_BETWEEN_ROBOTS;
            userRobot.y = newY;
        } else {
            int newY = getUserRobotY() + MIN_DISTANCE_BETWEEN_ROBOTS;
            userRobot.y = newY;
        }
    }

    private void updateUserRobotXP() {
        int newXP = userRobot.XP - 5;
        System.out.println(newXP);
        userRobot.XP = newXP;
    }

//    private void setModelsOfRobots() {
//        modelUserRobot = new Rectangle(getUserRobotX(), getUserRobotY(),
//                userRobot.robotWidth, userRobot.robotHeight);
//        modelEnemyRobot = new Rectangle(getRobotEnemyX(), getRobotEnemyY(),
//                enemyRobots.get(0).robotWidth, enemyRobots.get.robotHeight);
//    }
}
