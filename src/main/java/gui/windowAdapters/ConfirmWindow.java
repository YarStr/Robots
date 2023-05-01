package gui.windowAdapters;

import javax.swing.*;
import java.util.ResourceBundle;

public interface ConfirmWindow {
    default int getOptionForWindowAndBundle(String windowTitle, ResourceBundle bundle) {
        String message = getMessage(bundle) + " " + windowTitle + "?";
        String title = getTitle(bundle);
        Object[] options = getOptions(bundle);
        return JOptionPane.showOptionDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    Object[] getOptions(ResourceBundle bundle);

    String getMessage(ResourceBundle bundle);

    String getTitle(ResourceBundle bundle);
}
