package gui.windowAdapters.closeAdapters;

import gui.DataModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmCloseWindowAdapter extends WindowAdapter implements ConfirmClosable, PropertyChangeListener {

    private ResourceBundle bundle;
    private int CONFIRM_VALUE = 0;

    public ConfirmCloseWindowAdapter(DataModel dataModel) {
        dataModel.addBundleChangeListener(this);
        updateBundle(dataModel.getBundle());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            ResourceBundle bundle = (ResourceBundle) evt.getNewValue();
            updateBundle(bundle);
        }
    }
}
