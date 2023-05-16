package gui.windowAdapters.closeAdapters;

import gui.windowAdapters.ConfirmWindow;

import java.util.ResourceBundle;

public interface ConfirmCloseWindow extends ConfirmWindow {
    @Override
    default Object[] getOptions(ResourceBundle bundle) {
        return new Object[]{
                bundle.getString("closeWindowOptions.name1"),
                bundle.getString("closeWindowOptions.name2")
        };
    }

    @Override
    default String getMessage(ResourceBundle bundle, String additionalInfo) {
        return bundle.getString("close.message") + " " + additionalInfo + "?";
    }

    @Override
    default String getTitle(ResourceBundle bundle) {
        return bundle.getString("close.title");
    }
}
