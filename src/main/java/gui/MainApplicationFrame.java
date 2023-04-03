package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import gui.menuItems.LocalizationMenuItems;
import gui.menuItems.LookAndFeelMenuItems;
import gui.menuItems.TestMenuItems;
import log.Logger;

import java.io.File;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    ResourceBundle bundle = ResourceBundle.getBundle("message", Locale.ENGLISH);
    JMenuBar menuBar = new JMenuBar();
    GameWindow gameWindow = new GameWindow(bundle, 400, 400);

    LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), bundle);

    public MainApplicationFrame() {
        setContentPane(desktopPane);
        addWindow(createLogWindow());
        addWindow(createGameWindow());

        setJMenuBar(generateMenuBar());

        setTitle(bundle.getString("main.title"));
        String mainSystemLookAndFeel = LookAndFeelMenuItems.NIMBUS.getClassName();
        setLookAndFeel(mainSystemLookAndFeel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setFrameSizeAndPaddings() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
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

    private JMenu getOptionsMenu() {
        JMenu fileMenu = getMenuWithNameAndDescription(
                bundle.getString("jMenu.name"),
                bundle.getString("fileMenu.description"));
        fileMenu.add(getExitButton());
        return fileMenu;
    }

    private JMenu getSystemLookAndFeelMenu() {
        JMenu lookAndFeelMenu = getMenuWithNameAndDescription(
                bundle.getString("lookAndFeelMenu.name"),
                bundle.getString("lookAndFeelMenu.description"));
        for (LookAndFeelMenuItems item : LookAndFeelMenuItems.values())
            lookAndFeelMenu.add(getSystemLookAndFeelMenuItem(item));
        return lookAndFeelMenu;
    }

    private JMenu getTestMenu() {
        JMenu testMenu = getMenuWithNameAndDescription(
                bundle.getString("testMenu.name"),
                bundle.getString("testMenu.description"));
        testMenu.add(getTestMenuItem(TestMenuItems.NEW_MESSAGE));
        return testMenu;
    }

    private JMenu getLocalization() {
        JMenu localization = getMenuWithNameAndDescription(
                bundle.getString("local.name"),
                bundle.getString("local.description"));
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
        exitButton.addActionListener((event) -> {
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        });
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

    private JMenuItem getTestMenuItem(TestMenuItems menuName) {
        JMenuItem addLogMessageItem = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug(menuName.getCommand()));
        return addLogMessageItem;
    }

    private JMenuItem getLocalizationMenuItem(LocalizationMenuItems menuName) {
        JMenuItem localization = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
        localization.addActionListener((event) -> {
            setLocalization("message");
            getLocales();
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

    private void setLocalization(String newLocale) {
        bundle = ResourceBundle.getBundle(newLocale);
    }

    private void getLocales() {
        File directory = new File("src\\main\\resources");
        String[] files = directory.list((dir, name) -> name.endsWith(".properties"));

        assert files != null;
        for (String fileName : files) {
            String localeName = fileName.replace(".properties", "");
            JMenuItem locale = new JMenuItem(bundle.getString(localeName), KeyEvent.VK_S);
            locale.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                menuBar.removeAll();
                setLocalization(localeName);
                resetUI();
            }));
            this.add(locale);
        }
    }

    private void resetUI() {
        setJMenuBar(generateMenuBar());
        gameWindow.setTitle(bundle.getString("gameWindow.title"));
        logWindow.setTitle(bundle.getString("logWindow.title"));
//        gameWindow.changeLocale(bundle);
//        logWindow.changeLocale(bundle);
        revalidate();
        repaint();
    }
}
