package gui;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.*;

public class GameWindow extends SafeClosableWindow {
    public GameWindow(ResourceBundle bundle, int wight, int height) {
        super(bundle.getString("gameWindow.title"), true, true, false, true);
        GameVisualizer m_visualizer = new GameVisualizer(wight, height);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
