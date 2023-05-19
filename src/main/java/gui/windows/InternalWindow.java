package gui.windows;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import logic.Dispatcher;
import serializer.Serializer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class InternalWindow extends JInternalFrame implements PropertyChangeListener {
    private final String windowTitleKey;

    public InternalWindow(WindowType windowType, Dispatcher dispatcher, ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter) {
        super(dispatcher.getBundle().getString(windowType.getTitleKey()),
                true, true, false, true);
        windowTitleKey = windowType.getTitleKey();
        dispatcher.addInternalWindowPropertyChangeListener(this);
        setConfirmOnCloseOperation(confirmCloseInternalFrameAdapter);
        pack();
    }

    private void setConfirmOnCloseOperation(ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(confirmCloseInternalFrameAdapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Dispatcher.BUNDLE_CHANGED)) {
            updateBundleResources((ResourceBundle) evt.getNewValue());

        } else if (evt.getPropertyName().equals(Dispatcher.SAVING_STATE)) {
            Serializer.serialize(this);

        } else if (evt.getPropertyName().equals(Dispatcher.RESTORING_STATE)) {
            Serializer.deserialize(this);
        }
    }

    protected void updateBundleResources(ResourceBundle bundle) {
        setTitle(bundle.getString(windowTitleKey));
    }
}
