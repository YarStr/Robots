package gui.closeAdapters;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.ResourceBundle;

public class ConfirmCloseFrameAdapter extends InternalFrameAdapter {

    private ResourceBundle bundle;

    public ConfirmCloseFrameAdapter(ResourceBundle bundle) {
        updateBundle(bundle);
    }

    public void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();
        int option = getOption(getOptions(), getMessage(frame.getTitle()));
        if (option == 0) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    private int getOption(Object[] options, String message) {
        return JOptionPane.showOptionDialog(
                null,
                message,
                bundle.getString("close.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    private Object[] getOptions() {
        return new Object[]{
                bundle.getString("options.name1"),
                bundle.getString("options.name2")
        };
    }

    private String getMessage(String title) {
        return bundle.getString("close.message")
                + " "
                + title
                + "?";
    }
}
