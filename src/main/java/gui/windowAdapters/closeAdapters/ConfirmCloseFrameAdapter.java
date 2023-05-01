package gui.windowAdapters.closeAdapters;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.ResourceBundle;

public class ConfirmCloseFrameAdapter extends InternalFrameAdapter implements ConfirmCloseWindow {

    private ResourceBundle bundle;
    private final int CONFIRM_VALUE = 0;

    public ConfirmCloseFrameAdapter(ResourceBundle bundle) {
        updateBundle(bundle);
    }

    public void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();
        int option = getOptionForWindowAndBundle(frame.getTitle(), bundle);
        if (option == CONFIRM_VALUE) {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
