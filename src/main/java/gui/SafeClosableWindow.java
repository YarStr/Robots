package gui;

import javax.swing.*;
import java.util.ResourceBundle;

public class SafeClosableWindow extends JInternalFrame {

    public SafeClosableWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

//    @Override
    public void doDefaultCloseAction(ResourceBundle bundle) {
        Object[] options = {bundle.getString("options.name1"), bundle.getString("options.name2")};
        String message = bundle.getString("close.message") + "\"" + title + "\"?";
        if (JOptionPane.showOptionDialog(
                this,
                message,
                bundle.getString("close.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]) == 0)
            super.doDefaultCloseAction();
    }
}
