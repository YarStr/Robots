package gui.windows.game;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.windows.InternalWindow;
import gui.windows.WindowType;
import logic.Dispatcher;
import logic.game.GameController;
import logic.game.RobotType;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ScoreBoardWindow extends InternalWindow {
    private final HashMap<RobotType, Integer> score = new HashMap<>();
    private Integer level = 1;
    private ResourceBundle bundle;
    private final JTextArea scoreText = new JTextArea();
    private final JTextArea levelText = new JTextArea();

    public ScoreBoardWindow(Dispatcher dispatcher, ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter) {
        super(WindowType.SCORE_BOARD, dispatcher, confirmCloseInternalFrameAdapter);
        bundle = dispatcher.getBundle();
        setScore();
        initializeContent();
        addContent();
    }

    private void initializeContent() {
        levelText.setEditable(false);
        updateLevelText();
        scoreText.setEditable(false);
        updateScoreText();
    }

    private void addContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(levelText, BorderLayout.NORTH);
        contentPanel.add(scoreText, BorderLayout.CENTER);
        getContentPane().add(contentPanel);
    }

    private void setScore() {
        for (RobotType robot : RobotType.values())
            score.put(robot, 0);
    }
    private void updateScoreText() {
        String newContent = bundle.getString("scoreBoardWindow.userScore") + ": " + score.get(RobotType.USER) + "\n" +
                bundle.getString("scoreBoardWindow.enemyScore") + ": " + score.get(RobotType.ENEMY) + "\n";
        scoreText.setText(newContent);
        scoreText.invalidate();
    }

    private void updateLevelText() {
        String newContent = bundle.getString("scoreBoardWindow.level") + ": " + level.toString();
        levelText.setText(newContent);
        levelText.invalidate();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameController.SCORE_CHANGED)) {
            updateScore((RobotType) evt.getOldValue(), (Integer) evt.getNewValue());
            updateScoreText();
        } else if (evt.getPropertyName().equals(GameController.LEVEL_CHANGED)) {
            updateLevel((Integer) evt.getNewValue());
            updateLevelText();
        } else {
            super.propertyChange(evt);
        }
    }

    private void updateLevel(Integer newValue) {
        level = newValue;
    }

    protected void updateBundleResources(ResourceBundle bundle) {
        this.bundle = bundle;
        updateScoreText();
        updateLevelText();
        super.updateBundleResources(bundle);
    }

    private void updateScore(RobotType robotType, Integer newScore) {
        score.put(robotType, newScore);
    }
}
