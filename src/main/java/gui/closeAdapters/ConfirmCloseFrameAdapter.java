package gui.closeAdapters;

import gui.DataModel;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmCloseFrameAdapter extends InternalFrameAdapter implements ConfirmClosable, PropertyChangeListener {

    private ResourceBundle bundle;
    private int CONFIRM_VALUE = 0;

    public ConfirmCloseFrameAdapter(DataModel dataModel) {
        dataModel.addBundleChangeListener(this);
        updateBundle(dataModel.getBundle());
    }

    private void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();
        int option = getOptionForWindowAndBundle(frame.getTitle(), bundle);
        if (option == CONFIRM_VALUE) {
            frame.setVisible(false);
            frame.dispose();
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
