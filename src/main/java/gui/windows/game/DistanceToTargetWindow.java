package gui.windows.game;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.windows.InternalWindow;
import gui.windows.WindowType;
import logic.Dispatcher;
import logic.game.RobotType;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DistanceToTargetWindow extends InternalWindow {

    private final HashMap<RobotType, Integer> distance = new HashMap<>();
    private final JTextArea content = new JTextArea();
    private ResourceBundle bundle;

    public DistanceToTargetWindow(Dispatcher dispatcher, ConfirmCloseInternalFrameAdapter confirmCloseFrameAdapter) {
        super(WindowType.DISTANCE_TO_TARGET, dispatcher, confirmCloseFrameAdapter);
        bundle = dispatcher.getBundle();
        dispatcher.addDistanceChangeListener(this);
        setDistances();
        addContent();
    }

    private void addContent() {
        content.setEditable(false);
        updateContent();
        getContentPane().add(content);
    }

    private void setDistances() {
        for (RobotType robot : RobotType.values())
            distance.put(robot, 0);
    }

    protected void updateBundleResources(ResourceBundle bundle) {
        this.bundle = bundle;
        updateContent();
        super.updateBundleResources(bundle);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Dispatcher.UPDATE_DISTANCE_FROM_ENEMY_ROBOT_TO_TARGET) ||
                evt.getPropertyName().equals(Dispatcher.UPDATE_DISTANCE_FROM_USER_ROBOT_TO_TARGET)) {
            updateDistance((RobotType) evt.getOldValue(), (Integer) evt.getNewValue());
            updateContent();
        } else {
            super.propertyChange(evt);
        }
    }

    private void updateDistance(RobotType oldValue, Integer newValue) {
        distance.put(oldValue, newValue);
    }

    private void updateContent() {
        String newContent = bundle.getString("distanceToTargetWindow.user") +
                ": " + distance.get(RobotType.USER) + "\n" +
                bundle.getString("distanceToTargetWindow.enemy") +
                ": " + distance.get(RobotType.ENEMY) + "\n";
        content.setText(newContent);
        content.invalidate();
    }
}
