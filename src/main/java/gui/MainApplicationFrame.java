package gui;

import gui.adapters.ConfirmStateRecoveryAdapter;
import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import gui.adapters.close.ConfirmCloseWindowAdapter;
import gui.menu.MenuBar;
import gui.windows.LogWindow;
import gui.windows.game.*;
import logic.Dispatcher;
import logic.game.GameController;
import logic.game.UserRobot;
import logic.log.Logger;
import tempPackage.NewDrawer;
import tempPackage.Plugin;

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

    private Plugin plugin;

    private ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));

    private GameController gameController;
    private Dispatcher dispatcher;

    private ConfirmStateRecoveryAdapter confirmStateRecoveryAdapter;
    private ConfirmCloseWindowAdapter confirmCloseWindowAdapter;
    private ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter;


    private MenuBar menuBar;
    private GameWindow gameWindow;
    private LogWindow logWindow;
    private ScoreBoardWindow scoreBoardWindow;
    private RobotCoordinatesWindow robotCoordinatesWindow;
    private DistanceToTargetWindow distanceToTargetWindow;
    private ConfirmGameRestartWindow confirmGameRestartWindow;


    public MainApplicationFrame() {
        initializeEverything();
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

    // TODO декомпозировать
    private void initializeEverything() {
        // TODO читать аргументы из jar'а
        plugin = new Plugin(new UserRobot(-100, -100, 20, 20), new NewDrawer());

        gameController = new GameController(400, 400, plugin.robot());
        dispatcher = new Dispatcher(bundle, gameController);

        confirmStateRecoveryAdapter = new ConfirmStateRecoveryAdapter(dispatcher);
        confirmCloseWindowAdapter = new ConfirmCloseWindowAdapter(dispatcher);
        confirmCloseInternalFrameAdapter = new ConfirmCloseInternalFrameAdapter(dispatcher);

        menuBar = new MenuBar(dispatcher, this);

        gameWindow = new GameWindow(gameController, dispatcher, confirmCloseInternalFrameAdapter, plugin.drawer());

        logWindow = new LogWindow(Logger.getDefaultLogSource(), dispatcher, confirmCloseInternalFrameAdapter);
        scoreBoardWindow = new ScoreBoardWindow(dispatcher, confirmCloseInternalFrameAdapter);
        robotCoordinatesWindow = new RobotCoordinatesWindow(dispatcher, confirmCloseInternalFrameAdapter);
        distanceToTargetWindow = new DistanceToTargetWindow(dispatcher, confirmCloseInternalFrameAdapter);
        confirmGameRestartWindow = new ConfirmGameRestartWindow(dispatcher, gameWindow, gameController);
    }


    private void setLook() {
        setColorConstants();
        setNameAndTitle();
        setDefaultTheme();
    }

    private void setColorConstants() {
        UIManager.put("nimbusOrange", new Color(38, 139, 210));
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
        scoreBoardWindow.setSize(240, 120);
        gameController.addScoreAndLevelChangeListener(scoreBoardWindow);
        return scoreBoardWindow;
    }

    protected RobotCoordinatesWindow createRobotCoordinatesWindow() {
        robotCoordinatesWindow.setLocation(800, 150);
        robotCoordinatesWindow.setSize(240, 100);
        return robotCoordinatesWindow;
    }

    protected DistanceToTargetWindow createDistanceToTargetWindow(){
        distanceToTargetWindow.setLocation(800, 270);
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
