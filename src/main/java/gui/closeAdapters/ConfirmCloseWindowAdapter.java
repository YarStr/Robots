package gui.closeAdapters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class ConfirmCloseWindowAdapter extends WindowAdapter {

    private ResourceBundle bundle;

    public ConfirmCloseWindowAdapter(ResourceBundle bundle) {
        updateBundle(bundle);
    }

    public void updateBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }


    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int option = getOption(getOptions(), getMessage());
        if (option == 0) {
            window.setVisible(false);
            window.dispose();
            System.exit(0);
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

    private String getMessage() {
        return bundle.getString("close.message")
                + " "
                + bundle.getString("close.main_message")
                + "?";
    }
}
