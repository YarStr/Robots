package gui.menu;

import gui.DataModel;
import gui.MainApplicationFrame;
import gui.menu.items.LocalizationMenuItems;
import gui.menu.items.LookAndFeelMenuItems;
import gui.menu.items.TestMenuItems;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class MenuBar extends JMenuBar implements PropertyChangeListener {

    private final DataModel dataModel;

    private ResourceBundle bundle;

    private final MainApplicationFrame mainWindow;

    public MenuBar(DataModel dataModel, MainApplicationFrame mainWindow) {
        super();
        this.dataModel = dataModel;
        this.bundle = dataModel.getBundle();
        this.mainWindow = mainWindow;
        dataModel.addBundleChangeListener(this);
        generateMenuBar();
    }

    private void generateMenuBar() {
        add(getOptionsMenu());
        add(getSystemLookAndFeelMenu());
        add(getLocalization());
        add(getTestMenu());
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

    public JMenuItem getExitButton() {
        JMenuItem exitButton = new JMenuItem(bundle.getString("exitButton.name"), KeyEvent.VK_S);
        exitButton.addActionListener(mainWindow.getListener());
        return exitButton;
    }

    private JMenuItem getSystemLookAndFeelMenuItem(LookAndFeelMenuItems menuName) {
        JMenuItem systemLookAndFeel = new JMenuItem(menuName.getStringName(bundle), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(menuName.getClassName());
            mainWindow.invalidate();
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
            dataModel.updateBundle(bundle.getBaseBundleName(), menuName.getResourceName());
            mainWindow.invalidate();
        });
        return localization;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainWindow);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            Logger.debug(e.getMessage());
        }
    }

    public void setDefaultTheme() {
        setLookAndFeel(LookAndFeelMenuItems.NIMBUS.getClassName());
        mainWindow.invalidate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            bundle = (ResourceBundle) evt.getNewValue();
            removeAll();
            generateMenuBar();
        }
    }
}
