package gui;

import controller.Controller;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;
import gui.windowAdapters.closeAdapters.ConfirmCloseWindowAdapter;
import gui.internalWindows.GameWindow;
import gui.internalWindows.LogWindow;
import gui.menuItems.LocalizationMenuItems;
import gui.menuItems.LookAndFeelMenuItems;
import gui.menuItems.TestMenuItems;
import gui.windowAdapters.stateRecoveryAdapter.ConfirmStateRecovery;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainApplicationFrame extends JFrame implements PropertyChangeListener {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));
    private final JMenuBar menuBar = new JMenuBar();

    private final DataModel dataModel = new DataModel("messages", "ru");

    private final ConfirmCloseWindowAdapter confirmCloseWindowAdapter = new ConfirmCloseWindowAdapter(dataModel);
    private final ConfirmCloseFrameAdapter confirmCloseFrameAdapter = new ConfirmCloseFrameAdapter(dataModel);


    private final GameWindow gameWindow = new GameWindow(dataModel, confirmCloseFrameAdapter);

    private final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), dataModel, confirmCloseFrameAdapter);

    public MainApplicationFrame() {
        dataModel.addBundleChangeListener(this);
        setContentPane(desktopPane);
        addWorkingWindows();
        setJMenuBar(generateMenuBar());
        setNameAndTitle();
        setDefaultTheme();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(confirmCloseWindowAdapter);
        addWindowListener(confirmStateRecovery);
        createController();
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

    private JMenuBar generateMenuBar() {
        menuBar.add(getOptionsMenu());
        menuBar.add(getSystemLookAndFeelMenu());
        menuBar.add(getLocalization());
        menuBar.add(getTestMenu());
        return menuBar;
    }

    private void setDefaultTheme() {
        String mainSystemLookAndFeel = LookAndFeelMenuItems.NIMBUS.getClassName();
        setLookAndFeel(mainSystemLookAndFeel);
    }

    private JMenu getOptionsMenu() {
        JMenu fileMenu = getMenuWithNameAndDescription(
                bundle.getString("jMenu.name"),
                bundle.getString("fileMenu.description")
        );
        fileMenu.add(getExitButton());
        return fileMenu;
    }

    private JMenu getSystemLookAndFeelMenu() {
        JMenu lookAndFeelMenu = getMenuWithNameAndDescription(
                bundle.getString("lookAndFeelMenu.name"),
                bundle.getString("lookAndFeelMenu.description")
        );
        for (LookAndFeelMenuItems item : LookAndFeelMenuItems.values())
            lookAndFeelMenu.add(getSystemLookAndFeelMenuItem(item));
        return lookAndFeelMenu;
    }

    private JMenu getTestMenu() {
        JMenu testMenu = getMenuWithNameAndDescription(
                bundle.getString("testMenu.name"),
                bundle.getString("testMenu.description")
        );
        testMenu.add(getTestMenuItem());
        return testMenu;
    }

    private JMenu getLocalization() {
        JMenu localization = getMenuWithNameAndDescription(
                bundle.getString("local.name"),
                bundle.getString("local.description")
        );
        for (LocalizationMenuItems item : LocalizationMenuItems.values())
            localization.add(getLocalizationMenuItem(item));
        return localization;
    }

    private JMenu getMenuWithNameAndDescription(String name, String description) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem getExitButton() {
        JMenuItem exitButton = new JMenuItem(bundle.getString("exitButton.name"), KeyEvent.VK_S);
        exitButton.addActionListener((event) ->
            dispatchEvent(new WindowEvent(MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING))
        );
        return exitButton;
    }

    private JMenuItem getSystemLookAndFeelMenuItem(LookAndFeelMenuItems menuName) {
        JMenuItem systemLookAndFeel = new JMenuItem(menuName.getStringName(bundle), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(menuName.getClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }

    private JMenuItem getTestMenuItem() {
        JMenuItem addLogMessageItem = new JMenuItem(TestMenuItems.NEW_MESSAGE.getStringName(bundle), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug(TestMenuItems.NEW_MESSAGE.getCommand(bundle)));
        return addLogMessageItem;
    }

    private JMenuItem getLocalizationMenuItem(LocalizationMenuItems menuName) {
        JMenuItem localization = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
        localization.addActionListener((event) -> {
//            setLocalization(bundle.getBaseBundleName(), menuName.getResourceName());
            dataModel.updateBundle(bundle.getBaseBundleName(), menuName.getResourceName());
//            resetUI();
            this.invalidate();
        });
        return localization;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            Logger.debug(e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            bundle = (ResourceBundle) evt.getNewValue();
            resetUI();
        }
    }

    private void resetUI() {
        menuBar.removeAll();
        setJMenuBar(generateMenuBar());
        setNameAndTitle();
        revalidate();
        repaint();
    }
}
