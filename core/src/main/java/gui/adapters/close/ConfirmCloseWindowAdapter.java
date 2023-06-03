package gui.adapters.close;

import logic.Dispatcher;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfirmCloseWindowAdapter extends WindowAdapter implements ConfirmCloseWindow {

    private final Dispatcher dispatcher;
    private final int CONFIRM_VALUE = 0;

    public ConfirmCloseWindowAdapter(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int option = getOptionForWindow(dispatcher.getBundle(), window.getName());
        if (option == CONFIRM_VALUE) {
            dispatcher.saveState();
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        }
    }
}
