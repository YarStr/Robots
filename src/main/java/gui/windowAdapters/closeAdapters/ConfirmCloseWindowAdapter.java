package gui.windowAdapters.closeAdapters;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

public class ConfirmCloseWindowAdapter extends WindowAdapter implements ConfirmCloseWindow {

    public ResourceBundle bundle;

    public static String PROPERTY_NAME = "CloseWindow";

    private int CONFIRM_VALUE = 0;

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    public ConfirmCloseWindowAdapter(ResourceBundle bundle) {
        updateBundle(bundle);
    }

    public void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int option = getOptionForWindowAndBundle(window.getName(), bundle);
        if (option == CONFIRM_VALUE) {
            propChangeDispatcher.firePropertyChange(PROPERTY_NAME, null, null);
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        }
    }

    public void addCloseWindowListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(PROPERTY_NAME, listener);
    }
}
