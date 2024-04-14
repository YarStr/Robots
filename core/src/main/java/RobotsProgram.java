import gui.MainApplicationFrame;

import javax.swing.*;

public class RobotsProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setFrameSizeAndPaddings();
            frame.setVisible(true);
        });
    }
}
