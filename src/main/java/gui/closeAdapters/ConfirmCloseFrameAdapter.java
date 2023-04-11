package gui.closeAdapters;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.ResourceBundle;

public class ConfirmCloseFrameAdapter extends InternalFrameAdapter implements ConfirmClosable {

    private ResourceBundle bundle;
    private int CONFIRM_VALUE = 0;

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
