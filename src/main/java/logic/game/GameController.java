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
    public final ArrayList<Robot> dynamicRobots = new ArrayList<>();



    private Rectangle modelUserRobot = new Rectangle();
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
        userRobot = new UserRobot(-100, -100, 20, 20);
        userRobot.correctFieldSize(width, height);
        setEnemyRobots();
        dynamicRobots.add(userRobot);
        dynamicRobots.addAll(enemyRobots);
        updateUserRobotModel();
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
            userRobot.move(directionMove);
            getIntersectsOfRobots(userRobot);
            for (EnemyRobot enemyRobot : enemyRobots) {
                enemyRobot.turnToTarget(target);
                enemyRobot.move(width, height);
                getIntersectsOfRobots(enemyRobot);
            }

            if (userRobot.HP == 0) {
                stopGame(RobotType.ENEMY);
                userRobot.HP = 100;
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

    private void updateUserRobotModel() {
        modelUserRobot.x = getUserRobotX();
        modelUserRobot.y = getUserRobotY();
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
        System.out.println(enemyDistance);
        System.out.println(userDistance);
        System.out.println(" ");
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

    public RobotType getZoomTarget() {
        return zoomTarget;
    }

    public void changeZoomTarget() {
        switch (zoomTarget) {
            case USER -> zoomTarget = RobotType.ENEMY;
            case ENEMY -> zoomTarget = RobotType.USER;
        }
    }

    private void getIntersectsOfRobots(Robot robot1){
        Rectangle modelOfRobot1 = new Rectangle(robot1.getRoundedX(), robot1.getRoundedY(),
                robot1.robotWidth, robot1.robotHeight);
        for (Robot robot2: dynamicRobots) {
            if (robot1 != robot2){
                Rectangle modelOfRobot2 = new Rectangle(robot2.getRoundedX(), robot2.getRoundedY(),
                        robot2.robotWidth, robot2.robotHeight);
                if (modelOfRobot1.intersects(modelOfRobot2)) {
                    robot1.x = robot1.lastX;
                    robot1.y = robot1.lastY;
                    if(robot1 instanceof UserRobot){
                        ((UserRobot) robot1).HP -= 5;
                        System.out.println(((UserRobot) robot1).HP);
                    }
//                    if(robot2 instanceof UserRobot){
//                        ((UserRobot) robot2).HP -= 5;
//                    }

                }
            }
        }
    }
}
