package gui.adapters;

import logic.DataModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ConfirmStateRecoveryAdapter extends WindowAdapter implements ConfirmationWindow {
    private int CONFIRM_VALUE = 0;

    private DataModel dataModel;

    public ConfirmStateRecoveryAdapter(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Preferences preferences = Preferences.userNodeForPackage(ResourceBundle.class);
        try {
            preferences.sync();
            String baseName = preferences.get("baseName", "messages");
            String language = preferences.get("language", "ru");
            dataModel.updateBundle(baseName, language);

            int option = getOptionForWindow(dataModel.getBundle());
            if (option == CONFIRM_VALUE) {
                dataModel.restoreState();
            }
        } catch (BackingStoreException ex) {
            // файл с настройками отсутствует или недоступен
        }
    }

    @Override
    public Object[] getOptions(ResourceBundle bundle) {
        return new Object[]{
                bundle.getString("stateRecoveryOptions.name1"),
                bundle.getString("stateRecoveryOptions.name2")
        };
    }

    @Override
    public String getMessage(ResourceBundle bundle, String additionalInfo) {
        return bundle.getString("stateRecovery.message") + "?";
    }

    @Override
    public String getTitle(ResourceBundle bundle) {
        return bundle.getString("stateRecovery.title");
    }
}
