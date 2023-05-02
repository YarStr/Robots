package gui.internalWindows;

import controller.Serializable;
import gui.DataModel;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class GameWindow extends JInternalFrame implements PropertyChangeListener, Serializable {

    public GameWindow(DataModel dataModel, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(dataModel.getBundle().getString("gameWindow.title"), true, true, false, true);
        dataModel.addBundleChangeListener(this);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(confirmCloseFrameAdapter);
        pack();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            ResourceBundle bundle = (ResourceBundle) evt.getNewValue();
            setTitle(bundle.getString("gameWindow.title"));
        }
        else if (evt.getPropertyName().equals(DataModel.RESTORING_STATE)) {
            deserialize(this);
        }
        else if (evt.getPropertyName().equals(DataModel.SAVING_STATE)) {
            serialize(this);
        }
    }
}
