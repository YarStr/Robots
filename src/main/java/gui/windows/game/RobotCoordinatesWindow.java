package gui.windows.game;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.windows.InternalWindow;
import gui.windows.WindowType;
import logic.DataModel;
import logic.game.RobotType;

public class RobotCoordinatesWindow extends InternalWindow {
    private final HashMap<RobotType, Point> coordinates = new HashMap<>();
    private final JTextArea content = new JTextArea();
    private ResourceBundle bundle;

    public RobotCoordinatesWindow(DataModel dataModel, ConfirmCloseInternalFrameAdapter confirmCloseFrameAdapter) {
        super(WindowType.ROBOT_COORDINATE, dataModel, confirmCloseFrameAdapter);
        bundle = dataModel.getBundle();
        setCoordinates();
        addContent();
    }

    private void addContent() {
        content.setEditable(false);
        updateContent();
        getContentPane().add(content);
    }

    private void setCoordinates() {
        for (RobotType robot : RobotType.values())
            coordinates.put(robot, new Point(0, 0));
    }

    protected void updateBundleResources(ResourceBundle bundle) {
        this.bundle = bundle;
        updateContent();
        super.updateBundleResources(bundle);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.UPDATE_COORDINATES_ENEMY_ROBOT) ||
                evt.getPropertyName().equals(DataModel.UPDATE_COORDINATES_USER_ROBOT)) {
            updateCoordinates((RobotType) evt.getOldValue(), (Point) evt.getNewValue());
            updateContent();
        } else {
            super.propertyChange(evt);
        }
    }

    private void updateCoordinates(RobotType oldValue, Point newValue) {
        coordinates.put(oldValue, newValue);
    }

    private void updateContent() {
        String newContent = bundle.getString("userRobotCoordinates") +
                ": x: " + coordinates.get(RobotType.USER).x +
                ", y: " + coordinates.get(RobotType.USER).y + "\n" +
                bundle.getString("enemyRobotCoordinates") +
                ": x: " + coordinates.get(RobotType.ENEMY).x +
                ", y: " + coordinates.get(RobotType.ENEMY).y + "\n";
        content.setText(newContent);
        content.invalidate();
    }
}
