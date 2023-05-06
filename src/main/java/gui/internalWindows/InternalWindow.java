package gui.internalWindows;

import gui.DataModel;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;
import serializer.Serializer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class InternalWindow extends JInternalFrame implements PropertyChangeListener {
    private final String windowTitleKey;

    public InternalWindow(WindowType windowType, DataModel dataModel, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(dataModel.getBundle().getString(windowType.getTitleKey()),
                true, true, false, true);
        windowTitleKey = windowType.getTitleKey();
        dataModel.addBundleChangeListener(this);
        setConfirmOnCloseOperation(confirmCloseFrameAdapter);
        pack();
    }

    private void setConfirmOnCloseOperation(ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(confirmCloseFrameAdapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            ResourceBundle bundle = (ResourceBundle) evt.getNewValue();
            setTitle(bundle.getString(windowTitleKey));
        } else if (evt.getPropertyName().equals(DataModel.SAVING_STATE)) {
            Serializer.serialize(this);

        } else if (evt.getPropertyName().equals(DataModel.RESTORING_STATE)) {
            Serializer.deserialize(this);
        }
    }
}
