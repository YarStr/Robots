package gui.windowAdapters.closeAdapters;

import gui.DataModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfirmCloseWindowAdapter extends WindowAdapter implements ConfirmCloseWindow {

    private DataModel dataModel;
    private int CONFIRM_VALUE = 0;

    public ConfirmCloseWindowAdapter(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int option = getOptionForWindowAndBundle(window.getName(), dataModel.getBundle());
        if (option == CONFIRM_VALUE) {
            dataModel.saveState();
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        }
    }
}
