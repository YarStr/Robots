package gui.windows.game;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.windows.InternalWindow;
import gui.windows.WindowType;
import logic.Dispatcher;
import logic.game.GameController;
import ru.robot.interfaces.Drawer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;

public class GameWindow extends InternalWindow {
    private final Dispatcher dispatcher;
    private final GameController gameController;

    private final JButton startButton;
    private final JButton zoomFocusButton;

    private final JProgressBar userHPBar;

    private final Drawer drawer;

    public GameWindow(GameController gameController,
                      Dispatcher dispatcher,
                      ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter,
                      Drawer drawer) {
        super(WindowType.GAME, dispatcher, confirmCloseInternalFrameAdapter);

        this.dispatcher = dispatcher;
        this.gameController = gameController;
        this.gameController.addGameOverListener(this);
        startButton = getStartButton();
        zoomFocusButton = getZoomFocusButton();
        userHPBar = getUserHPBar();

        this.drawer = drawer;

        setSize(gameController.getWidth(), gameController.getHeightField());
        addContentPanel(gameController);
        pack();
    }

    private JProgressBar getUserHPBar() {
        JProgressBar bar = new JProgressBar(0, 100);

        bar.setValue(100);
        bar.setString(dispatcher.getBundle().getString("gameWindow.HP"));
        bar.setStringPainted(true);

        return bar;
    }

    public void startGame() {
        gameController.startGame();
        this.startButton.setEnabled(false);
    }

    private void addContentPanel(GameController gameController) {
        JPanel contentPanel = getContentPanel(gameController);
        getContentPane().add(contentPanel);
    }

    private JPanel getContentPanel(GameController gameController) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(userHPBar, BorderLayout.NORTH);
        contentPanel.add(getGameVisualiser(gameController), BorderLayout.CENTER);
        contentPanel.add(getButtonsPanel(), BorderLayout.SOUTH);

        return contentPanel;
    }

    private GameVisualizer getGameVisualiser(GameController gameController) {
        GameVisualizer gameVisualizer = new GameVisualizer(gameController, drawer);
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
        button.setText(dispatcher.getBundle().getString("gameWindow.startGameText"));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }

    private JButton getZoomFocusButton() {
        JButton button = new JButton(
                new AbstractAction("Change zoom target") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameController.changeZoomTarget();
                    }
                }
        );
        button.setFocusable(false);
        button.setText(dispatcher.getBundle().getString("gameWindow.changeZoomTargetText"));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }

    @Override
    protected void updateBundleResources(ResourceBundle bundle) {
        startButton.setText(bundle.getString("gameWindow.startGameText"));
        zoomFocusButton.setText(bundle.getString("gameWindow.changeZoomTargetText"));
        userHPBar.setString(bundle.getString("gameWindow.HP"));
        super.updateBundleResources(bundle);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameController.GAME_OVER)) {
            this.startButton.setEnabled(true);
        } else if (evt.getPropertyName().equals(GameController.HP_CHANGED)) {
            int newHP = (Integer) evt.getNewValue();
            userHPBar.setValue(newHP);
        } else {
            super.propertyChange(evt);
        }
    }
}
