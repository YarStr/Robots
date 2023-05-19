package gui.windows.game;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.windows.InternalWindow;
import gui.windows.WindowType;
import logic.Dispatcher;
import logic.game.GameField;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;

public class GameWindow extends InternalWindow {
    private final Dispatcher dispatcher;
    private final GameField gameField;

    private final JButton startButton;
    private final JButton zoomFocusButton;

    public GameWindow(GameField gameField, Dispatcher dispatcher, ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter) {
        super(WindowType.GAME, dispatcher, confirmCloseInternalFrameAdapter);

        this.dispatcher = dispatcher;
        this.gameField = gameField;
        this.gameField.addGameOverListener(this);
        startButton = getStartButton();
        zoomFocusButton = getZoomFocusButton();

        setSize(gameField.getWidth(), gameField.getHeight());
        addContentPanel(gameField);
        pack();
    }

    public void startGame() {
        gameField.startGame();
        this.startButton.setEnabled(false);
    }

    private void addContentPanel(GameField gameField) {
        JPanel contentPanel = getContentPanel(gameField);
        getContentPane().add(contentPanel);
    }

    private JPanel getContentPanel(GameField gameField) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(getGameVisualiser(gameField), BorderLayout.CENTER);
        contentPanel.add(getButtonsPanel(), BorderLayout.SOUTH);

        return contentPanel;
    }

    private GameVisualizer getGameVisualiser(GameField gameField) {
        GameVisualizer gameVisualizer = new GameVisualizer(gameField);
        gameVisualizer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        return gameVisualizer;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(startButton, BorderLayout.WEST);
        panel.add(zoomFocusButton, BorderLayout.EAST);
        return panel;
    }

    private JButton getStartButton() {
        JButton button = new JButton(
                new AbstractAction("Start logic.game") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameWindow.this.startGame();
                    }
                }
        );
        button.setText(dispatcher.getBundle().getString("startGame.text"));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }

    private JButton getZoomFocusButton() {
        JButton button = new JButton(
                new AbstractAction("Change zoom target") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameField.changeZoomTarget();
                    }
                }
        );
        button.setFocusable(false);
        button.setText(dispatcher.getBundle().getString("changeZoomTarget.text"));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }

    @Override
    protected void updateBundleResources(ResourceBundle bundle) {
        startButton.setText(bundle.getString("startGame.text"));
        zoomFocusButton.setText(bundle.getString("changeZoomTarget.text"));
        super.updateBundleResources(bundle);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameField.GAME_OVER)) {
            this.startButton.setEnabled(true);
        } else {
            super.propertyChange(evt);
        }
    }
}
