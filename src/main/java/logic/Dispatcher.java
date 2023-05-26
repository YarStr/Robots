package logic;

import logic.game.EnemyRobot;
import logic.game.GameController;
import logic.game.RobotType;
import logic.game.UserRobot;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Dispatcher implements PropertyChangeListener {
    public static String BUNDLE_CHANGED = "DataModel.bundle";
    public static String RESTORING_STATE = "Restoring state";
    public static String SAVING_STATE = "Saving state";
    public static String UPDATE_COORDINATES_ENEMY_ROBOT = "Update coordinates enemy robot";
    public static String UPDATE_COORDINATES_USER_ROBOT = "Update coordinates user robot";
    public static String UPDATE_DISTANCE_FROM_USER_ROBOT_TO_TARGET = "Update distance from user robot to target";
    public static String UPDATE_DISTANCE_FROM_ENEMY_ROBOT_TO_TARGET = "Update distance from enemy robot to target";

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    private ResourceBundle bundle;
    private final GameController gameController;

    public Dispatcher(ResourceBundle resourceBundle, GameController gameController) {
        bundle = resourceBundle;
        this.gameController = gameController;
        this.gameController.userRobot.addDataChangeListener(this);
//        this.gameController.enemyRobot.addDataChangeListener(this);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void updateBundle(String resourceName, String nameLocal) {
        ResourceBundle new_bundle = ResourceBundle.getBundle(resourceName, new Locale(nameLocal));
        propChangeDispatcher.firePropertyChange(BUNDLE_CHANGED, bundle, new_bundle);
        bundle = new_bundle;
    }


    public void addInternalWindowPropertyChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(BUNDLE_CHANGED, listener);
        propChangeDispatcher.addPropertyChangeListener(RESTORING_STATE, listener);
        propChangeDispatcher.addPropertyChangeListener(SAVING_STATE, listener);
    }

    public void addDistanceChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(UPDATE_DISTANCE_FROM_USER_ROBOT_TO_TARGET, listener);
        propChangeDispatcher.addPropertyChangeListener(UPDATE_DISTANCE_FROM_ENEMY_ROBOT_TO_TARGET, listener);
    }

    public void addCoordinatesChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(UPDATE_COORDINATES_ENEMY_ROBOT, listener);
        propChangeDispatcher.addPropertyChangeListener(UPDATE_COORDINATES_USER_ROBOT, listener);
    }

    public void restoreState() {
        propChangeDispatcher.firePropertyChange(RESTORING_STATE, null, null);
    }

    public void saveState() {
        Preferences preferences = Preferences.userNodeForPackage(ResourceBundle.class);
        preferences.put("baseName", bundle.getBaseBundleName());
        preferences.put("language", bundle.getLocale().toString());
        propChangeDispatcher.firePropertyChange(SAVING_STATE, null, null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(RobotType.USER)) {
            changeLocationUserRobot(evt);
        }
        if (evt.getNewValue().equals(RobotType.ENEMY)) {
            changeLocationEnemyRobot(evt);
        }
    }


    private void changeLocationEnemyRobot(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(EnemyRobot.CHANGE_COORDINATES)) {
            Point coordinatesRobot = new Point(gameController.getRobotEnemyX(), gameController.getRobotEnemyY());
            propChangeDispatcher.firePropertyChange(UPDATE_COORDINATES_ENEMY_ROBOT, RobotType.ENEMY, coordinatesRobot);
            propChangeDispatcher.firePropertyChange(UPDATE_DISTANCE_FROM_ENEMY_ROBOT_TO_TARGET,
                    RobotType.ENEMY, gameController.enemyRobots.get(0).getDistanceToTarget(gameController.target));
        }
    }

    private void changeLocationUserRobot(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(UserRobot.CHANGE_COORDINATES)) {
            Point coordinatesRobot = new Point(gameController.getUserRobotX(), gameController.getUserRobotY());
            propChangeDispatcher.firePropertyChange(UPDATE_COORDINATES_USER_ROBOT, RobotType.USER, coordinatesRobot);
            propChangeDispatcher.firePropertyChange(UPDATE_DISTANCE_FROM_USER_ROBOT_TO_TARGET,
                    RobotType.USER, gameController.userRobot.getDistanceToTarget(gameController.target));
        }
    }
}

