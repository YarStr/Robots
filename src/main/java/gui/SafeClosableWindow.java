package gui;

import javax.swing.*;

public class SafeClosableWindow extends JInternalFrame {

    public SafeClosableWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public void doDefaultCloseAction() {
        Object[] options = {"Да", "Нет"};
        String message = "Закрыть \"" + title + "\"?";
        if (JOptionPane.showOptionDialog(
                this,
                message,
                "Подтверждение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]) == 0)
            super.doDefaultCloseAction();
    }
}
