package gui;

import gameLogic.GameField;
import gui.internalWindows.*;
import gui.menu.MenuBar;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;
import gui.windowAdapters.closeAdapters.ConfirmCloseWindowAdapter;
import gui.windowAdapters.stateRecoveryAdapter.ConfirmStateRecovery;
import log.Logger;

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
    private final DataModel dataModel = new DataModel(bundle, gameField);

    private final ConfirmStateRecovery confirmStateRecovery = new ConfirmStateRecovery(dataModel);
    private final ConfirmCloseWindowAdapter confirmCloseWindowAdapter = new ConfirmCloseWindowAdapter(dataModel);
    private final ConfirmCloseFrameAdapter confirmCloseFrameAdapter = new ConfirmCloseFrameAdapter(dataModel);

    private final MenuBar menuBar = new MenuBar(dataModel, this);
    private final GameWindow gameWindow = new GameWindow(gameField, dataModel, confirmCloseFrameAdapter);
    private final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), dataModel, confirmCloseFrameAdapter);
    private final ScoreBoardWindow scoreBoardWindow = new ScoreBoardWindow(dataModel, confirmCloseFrameAdapter);
    private final RobotCoordinatesWindow robotCoordinatesWindow = new RobotCoordinatesWindow(dataModel, confirmCloseFrameAdapter);
    private final DistanceToTargetWindow distanceToTargetWindow = new DistanceToTargetWindow(dataModel, confirmCloseFrameAdapter);

    private final ConfirmGameRestartWindow confirmGameRestartWindow = new ConfirmGameRestartWindow(dataModel, gameWindow, gameField);

    public MainApplicationFrame() {
        dataModel.addBundleChangeListener(this);
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
        addWindowListener(confirmStateRecovery);
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
        scoreBoardWindow.setSize(400, 200);
        gameField.addScoreChangeListener(scoreBoardWindow);
        return scoreBoardWindow;
    }

    protected RobotCoordinatesWindow createRobotCoordinatesWindow() {
        robotCoordinatesWindow.setLocation(800, 220);
        robotCoordinatesWindow.setSize(400, 200);
        return robotCoordinatesWindow;
    }

    protected DistanceToTargetWindow createDistanceToTargetWindow(){
        distanceToTargetWindow.setLocation(800, 440);
        distanceToTargetWindow.setSize(400, 200);
        return distanceToTargetWindow;
    }

    private void setDefaultTheme() {
        menuBar.setDefaultTheme();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
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
