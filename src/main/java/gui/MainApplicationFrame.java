package gui;

import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.menuItems.LookAndFeels;
import gui.menuItems.Test;
import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    // Разобраться, почему здесь не работает setBounds, setMinimumSize и прочее
    public MainApplicationFrame() {
//        int inset = 50;
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setBounds(inset, inset,
//                screenSize.width - inset * 2,
//                screenSize.height - inset * 2);
        setContentPane(desktopPane);

        addWindow(createLogWindow());
        addWindow(createGameWindow());

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getSystemLookAndFeelMenu());
        menuBar.add(getTestMenu());
        return menuBar;
    }

    private JMenu getSystemLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        for (LookAndFeels name : LookAndFeels.values())
            lookAndFeelMenu.add(getSystemLookAndFeelMenuItem(name));

        return lookAndFeelMenu;
    }

    private JMenu getTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        testMenu.add(getTestMenuItem(Test.NEW_MESSAGE));
        return testMenu;
    }

    private JMenuItem getSystemLookAndFeelMenuItem(LookAndFeels menuName) {
        JMenuItem systemLookAndFeel = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(menuName.getClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }

    private JMenuItem getTestMenuItem(Test menuName) {
        JMenuItem addLogMessageItem = new JMenuItem(menuName.getStringName(), KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(menuName.getCommand());
        });
        return addLogMessageItem;
    }

    // Придумать, как обрабатывать ошибки умнее
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            Logger.debug(e.getMessage());
        }
    }
}
