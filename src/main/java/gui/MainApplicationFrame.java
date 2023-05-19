package gui;

import gui.adapters.ConfirmStateRecoveryAdapter;
import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.adapters.close.ConfirmCloseWindowAdapter;
import gui.menu.MenuBar;
import gui.windows.LogWindow;
import gui.windows.game.*;
import logic.Dispatcher;
import logic.game.GameField;
import logic.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainApplicationFrame extends JFrame implements PropertyChangeListener {
    private final JDesktopPane desktopPane = new JDesktopPane();

    private ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));

    private final GameField gameField = new GameField(400, 400);
    private final Dispatcher dispatcher = new Dispatcher(bundle, gameField);

    private final ConfirmStateRecoveryAdapter confirmStateRecoveryAdapter = new ConfirmStateRecoveryAdapter(dispatcher);
    private final ConfirmCloseWindowAdapter confirmCloseWindowAdapter = new ConfirmCloseWindowAdapter(dispatcher);
    private final ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter = new ConfirmCloseInternalFrameAdapter(dispatcher);

    private final MenuBar menuBar = new MenuBar(dispatcher, this);
    private final GameWindow gameWindow = new GameWindow(gameField, dispatcher, confirmCloseInternalFrameAdapter);
    private final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), dispatcher, confirmCloseInternalFrameAdapter);
    private final ScoreBoardWindow scoreBoardWindow = new ScoreBoardWindow(dispatcher, confirmCloseInternalFrameAdapter);
    private final RobotCoordinatesWindow robotCoordinatesWindow = new RobotCoordinatesWindow(dispatcher, confirmCloseInternalFrameAdapter);
    private final DistanceToTargetWindow distanceToTargetWindow = new DistanceToTargetWindow(dispatcher, confirmCloseInternalFrameAdapter);
    private final ConfirmGameRestartWindow confirmGameRestartWindow = new ConfirmGameRestartWindow(dispatcher, gameWindow, gameField);

    public MainApplicationFrame() {
        dispatcher.addInternalWindowPropertyChangeListener(this);
        initializeContent();
        setLook();
        setCloseAndStateRecoveryOperations();
    }

    private void initializeContent() {
        setContentPane(desktopPane);
        addWorkingWindows();
        setJMenuBar(menuBar);
    }

    private void setLook() {
        setNameAndTitle();
        setDefaultTheme();
    }

    private void setCloseAndStateRecoveryOperations() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(confirmCloseWindowAdapter);
        addWindowListener(confirmStateRecoveryAdapter);
    }

    private void setNameAndTitle() {
        String title = bundle.getString("main.title");
        setName(title);
        setTitle(title);
    }

    public void setFrameSizeAndPaddings() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
    }

    private void addWorkingWindows() {
        addWindow(createLogWindow());
        addWindow(createGameWindow());
        addWindow(createScoreBoardWindow());
        addWindow(createRobotCoordinatesWindow());
        addWindow(createDistanceToTargetWindow());
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected LogWindow createLogWindow() {
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("logger.message"));
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        gameWindow.setLocation(310, 10);
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    protected ScoreBoardWindow createScoreBoardWindow() {
        scoreBoardWindow.setLocation(800, 10);
        scoreBoardWindow.setSize(240, 100);
        gameField.addScoreChangeListener(scoreBoardWindow);
        return scoreBoardWindow;
    }

    protected RobotCoordinatesWindow createRobotCoordinatesWindow() {
        robotCoordinatesWindow.setLocation(800, 130);
        robotCoordinatesWindow.setSize(240, 100);
        return robotCoordinatesWindow;
    }

    protected DistanceToTargetWindow createDistanceToTargetWindow(){
        distanceToTargetWindow.setLocation(800, 250);
        distanceToTargetWindow.setSize(240, 100);
        return distanceToTargetWindow;
    }

    private void setDefaultTheme() {
        menuBar.setDefaultTheme();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Dispatcher.BUNDLE_CHANGED)) {
            bundle = (ResourceBundle) evt.getNewValue();
            resetUI();
        }
    }

    public ActionListener getListener() {
        return ((event) -> dispatchEvent(new WindowEvent(MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING)));
    }

    private void resetUI() {
        setNameAndTitle();
        revalidate();
        repaint();
    }
}
