package gui.internalWindows;

import gui.closeAdapters.ConfirmCloseFrameAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GameWindow extends JInternalFrame {

    public GameWindow(ResourceBundle bundle, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(bundle.getString("gameWindow.title"), true, true, false, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(confirmCloseFrameAdapter);
        pack();
    }
}
