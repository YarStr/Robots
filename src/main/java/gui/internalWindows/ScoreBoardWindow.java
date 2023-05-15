package gui.internalWindows;

import gameLogic.GameField;
import gameLogic.RobotType;
import gui.DataModel;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ScoreBoardWindow extends InternalWindow {
    private final HashMap<RobotType, Integer> score = new HashMap<>();
    private ResourceBundle bundle;
    private final JTextArea content = new JTextArea();

    public ScoreBoardWindow(DataModel dataModel, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(WindowType.SCORE_BOARD, dataModel, confirmCloseFrameAdapter);
        bundle = dataModel.getBundle();
        setScore();
        addContent();
    }

    private void setScore() {
        for (RobotType robot : RobotType.values())
            score.put(robot, 0);
    }

    private void addContent() {
        content.setEditable(false);
        updateContent();
        getContentPane().add(content);
    }

    private void updateContent() {
        String newContent = bundle.getString("userScore") + ": " + score.get(RobotType.USER) + "\n" +
                bundle.getString("enemyScore") + ": " + score.get(RobotType.ENEMY) + "\n";
        content.setText(newContent);
        content.invalidate();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameField.SCORE_CHANGED)) {
            updateScore((RobotType) evt.getOldValue(), (Integer) evt.getNewValue());
            updateContent();
        } else {
            super.propertyChange(evt);
        }
    }

    protected void updateBundleResources(ResourceBundle bundle) {
        this.bundle = bundle;
        updateContent();
        super.updateBundleResources(bundle);
    }

    private void updateScore(RobotType robotType, Integer newScore) {
        score.put(robotType, newScore);
    }
}
