package gui.closeAdapters;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class ConfirmCloseWindowAdapter extends WindowAdapter implements ConfirmClosable {

    private ResourceBundle bundle;
    private int CONFIRM_VALUE = 0;

    public ConfirmCloseWindowAdapter(ResourceBundle bundle) {
        updateBundle(bundle);
    }

    public void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int option = getOptionForWindowAndBundle(window.getName(), bundle);
        if (option == CONFIRM_VALUE) {
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        }
    }
}
