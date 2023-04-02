package gui;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import gui.menuItems.LookAndFeelMenuItems;
import gui.menuItems.TestMenuItems;
import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        setContentPane(desktopPane);
        addWindow(createLogWindow());
        addWindow(createGameWindow());
        setJMenuBar(generateMenuBar());

        setTitle("Приложение Робот");
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
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setLocation(310, 10);
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getOptionsMenu());
        menuBar.add(getSystemLookAndFeelMenu());
        menuBar.add(getTestMenu());
        return menuBar;
    }

    private JMenu getOptionsMenu() {
        JMenu fileMenu = getMenuWithNameAndDescription("Опции", "Настройка опций приложения");
        fileMenu.add(getExitButton());
        return fileMenu;
    }

    private JMenu getSystemLookAndFeelMenu() {
        JMenu lookAndFeelMenu = getMenuWithNameAndDescription(
                "Режим отображения",
                "Управление режимом отображения приложения"
        );
        for (LookAndFeelMenuItems item : LookAndFeelMenuItems.values())
            lookAndFeelMenu.add(getSystemLookAndFeelMenuItem(item));
        return lookAndFeelMenu;
    }

    private JMenu getTestMenu() {
        JMenu testMenu = getMenuWithNameAndDescription("Тесты", "Тестовые команды");
        testMenu.add(getTestMenuItem(TestMenuItems.NEW_MESSAGE));
        return testMenu;
    }

    private JMenu getMenuWithNameAndDescription(String name, String description) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem getExitButton() {
        JMenuItem exitButton = new JMenuItem("Выход", KeyEvent.VK_S);
        exitButton.addActionListener((event) -> {
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        });
        return exitButton;
    }

    private JMenuItem getSystemLookAndFeelMenuItem(LookAndFeelMenuItems menuName) {
        JMenuItem systemLookAndFeel = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
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

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            Logger.debug(e.getMessage());
        }
    }
}
