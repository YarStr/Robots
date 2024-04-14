package gui.adapters;

import javax.swing.*;
import java.util.ResourceBundle;

public interface ConfirmationWindow {
    default int getOptionForWindow(ResourceBundle bundle) {
        return getOptionForWindow(bundle, null);
    }

    default int getOptionForWindow(ResourceBundle bundle, String additionalInfo) {
        String message = getMessage(bundle, additionalInfo);
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

    String getMessage(ResourceBundle bundle, String additionalInfo);

    String getTitle(ResourceBundle bundle);
}
