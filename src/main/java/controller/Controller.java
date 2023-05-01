package controller;

import gui.windowAdapters.closeAdapters.ConfirmCloseWindowAdapter;
import gui.internalWindows.GameWindow;
import gui.internalWindows.LogWindow;
import gui.windowAdapters.stateRecoveryAdapter.ConfirmStateRecovery;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class Controller implements PropertyChangeListener {

    private final Serializer serializer = new Serializer();
    private final GameWindow gameWindow;
    private final LogWindow logWindow;

    private final ConfirmCloseWindowAdapter confirmCloseWindowAdapter;
    private final ConfirmStateRecovery confirmStateRecovery;

    private ResourceBundle bundle;

    public Controller(GameWindow gameWindow, LogWindow logWindow,
                      ConfirmCloseWindowAdapter confirmCloseWindowAdapter,
                      ConfirmStateRecovery confirmStateRecovery) {
        this.gameWindow = gameWindow;
        this.logWindow = logWindow;
        this.confirmCloseWindowAdapter = confirmCloseWindowAdapter;
        this.confirmStateRecovery = confirmStateRecovery;
        addListener();
    }

    private void addListener() {
        confirmStateRecovery.addOpenWindowListener(this);
        confirmCloseWindowAdapter.addCloseWindowListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ConfirmCloseWindowAdapter.PROPERTY_NAME.equals(evt.getPropertyName())) {
            serializer.serialize(gameWindow);
            serializer.serialize(logWindow);
        }

        if (ConfirmStateRecovery.PROPERTY_NAME.equals(evt.getPropertyName())) {
            serializer.deserialize(gameWindow);
            serializer.deserialize(logWindow);
        }
    }

}
