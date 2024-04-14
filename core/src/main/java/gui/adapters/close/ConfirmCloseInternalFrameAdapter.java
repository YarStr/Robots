package gui.adapters.close;

import logic.Dispatcher;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmCloseInternalFrameAdapter extends InternalFrameAdapter implements ConfirmCloseWindow, PropertyChangeListener {

    private ResourceBundle bundle;
    private final int CONFIRM_VALUE = 0;

    public ConfirmCloseInternalFrameAdapter(Dispatcher dispatcher) {
        dispatcher.addInternalWindowPropertyChangeListener(this);
        updateBundle(dispatcher.getBundle());
    }

    private void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();
        int option = getOptionForWindow(bundle, frame.getTitle());
        if (option == CONFIRM_VALUE) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Dispatcher.BUNDLE_CHANGED)) {
            ResourceBundle bundle = (ResourceBundle) evt.getNewValue();
            updateBundle(bundle);
        }
    }
}
