package gui.internalWindows;

import gui.DataModel;
import gui.windowAdapters.closeAdapters.ConfirmCloseFrameAdapter;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends InternalWindow {

    public GameWindow(DataModel dataModel, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(WindowType.GAME, dataModel, confirmCloseFrameAdapter);
        addGameVisualiser();
        pack();
    }

    public void addGameVisualiser() {
        GameVisualizer gameVisualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }
}
