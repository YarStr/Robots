import gui.MainApplicationFrame;

import java.awt.*;

import javax.swing.SwingUtilities;

public class RobotsProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            int inset = 50;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setBounds(inset, inset,
                    screenSize.width - inset * 2,
                    screenSize.height - inset * 2);
        });
    }
}
