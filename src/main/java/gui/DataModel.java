package gui;

import gameLogic.GameField;
import gameLogic.RobotEnemy;
import gameLogic.RobotType;
import gameLogic.UserRobot;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DataModel implements Observer {
    public static String BUNDLE_CHANGED = "DataModel.bundle";
    public static String RESTORING_STATE = "Restoring state";
    public static String SAVING_STATE = "Saving state";
    public static String UPDATE_COORDINATES_ENEMY_ROBOT = "Update coordinates enemy robot";
    public static String UPDATE_COORDINATES_USER_ROBOT = "Update coordinates user robot";

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    private ResourceBundle bundle;
    private final GameField gameField;

    public DataModel(ResourceBundle resourceBundle, GameField gameField) {
        bundle = resourceBundle;
        this.gameField = gameField;
        this.gameField.robotEnemy.addObserver(this);
        this.gameField.userRobot.addObserver(this);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void updateBundle(String resourceName, String nameLocal) {
        ResourceBundle new_bundle = ResourceBundle.getBundle(resourceName, new Locale(nameLocal));
        propChangeDispatcher.firePropertyChange(BUNDLE_CHANGED, bundle, new_bundle);
        bundle = new_bundle;
    }


    public void addBundleChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(BUNDLE_CHANGED, listener);
        propChangeDispatcher.addPropertyChangeListener(RESTORING_STATE, listener);
        propChangeDispatcher.addPropertyChangeListener(SAVING_STATE, listener);
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
    public void update(Observable o, Object arg) {
        Point coordinatesRobot;
        if (arg == RobotEnemy.CHANGE_COORDINATES) {
            coordinatesRobot = new Point(gameField.getRobotEnemyX(), gameField.getRobotEnemyY());
            propChangeDispatcher.firePropertyChange(UPDATE_COORDINATES_ENEMY_ROBOT, RobotType.ENEMY, coordinatesRobot);
        }
        if (arg == UserRobot.CHANGE_COORDINATES) {
            coordinatesRobot = new Point(gameField.getUserRobotX(), gameField.getUserRobotY());
            propChangeDispatcher.firePropertyChange(UPDATE_COORDINATES_USER_ROBOT, RobotType.USER, coordinatesRobot);
        }
    }
}
