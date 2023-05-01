package gui.windowAdapters.stateRecoveryAdapter;

import gui.windowAdapters.ConfirmWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfirmStateRecovery extends WindowAdapter implements ConfirmWindow {
    public static String PROPERTY_NAME = "OpenWindow";
    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));

    private int CONFIRM_VALUE = 0;

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    @Override
    public void windowOpened(WindowEvent e) {
        int option = getOptionForWindowAndBundle("", bundle);
        if (option == CONFIRM_VALUE) {
            propChangeDispatcher.firePropertyChange(PROPERTY_NAME, null, null);
        }
    }

    public void addOpenWindowListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(PROPERTY_NAME, listener);
    }

    @Override
    public Object[] getOptions(ResourceBundle bundle) {
        return new Object[]{
                bundle.getString("stateRecoveryOptions.name1"),
                bundle.getString("stateRecoveryOptions.name2")
        };
    }

    @Override
    public String getMessage(ResourceBundle bundle) {
        return bundle.getString("stateRecovery.message");
    }

    @Override
    public String getTitle(ResourceBundle bundle) {
        return bundle.getString("stateRecovery.title");
    }
}
