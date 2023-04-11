package gui.closeAdapters;

import javax.swing.*;
import java.util.ResourceBundle;

public interface ConfirmClosable {
    default int getOptionForWindowAndBundle(String windowTitle, ResourceBundle bundle) {
        String closeMessage = getCloseMessage(bundle) + " " + windowTitle + "?";
        String closeTitle = getCloseTitle(bundle);
        Object[] options = getOptions(bundle);
        return JOptionPane.showOptionDialog(
                null,
                closeMessage,
                closeTitle,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    default Object[] getOptions(ResourceBundle bundle) {
        return new Object[]{
                bundle.getString("options.name1"),
                bundle.getString("options.name2")
        };
    }

    default String getCloseMessage(ResourceBundle bundle) {
        return bundle.getString("close.message");
    }

    default String getCloseTitle(ResourceBundle bundle) {
        return bundle.getString("close.title");
    }
}
