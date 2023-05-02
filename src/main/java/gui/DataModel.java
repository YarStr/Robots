package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DataModel {
    public static String BUNDLE_CHANGED = "DataModel.bundle";
    public static String RESTORING_STATE = "Restoring state";
    public static String SAVING_STATE = "Saving state";

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    private ResourceBundle bundle;

    public DataModel(ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void updateBundle(String resourceName, String nameLocal) {
        ResourceBundle new_bundle = ResourceBundle.getBundle(resourceName, new Locale(nameLocal));
        propChangeDispatcher.firePropertyChange(BUNDLE_CHANGED, bundle, new_bundle);
        bundle = new_bundle;
    }


    public void addBundleChangeListener(PropertyChangeListener listener) {
        propChangeDispatcher.addPropertyChangeListener(BUNDLE_CHANGED, listener);
        propChangeDispatcher.addPropertyChangeListener(RESTORING_STATE, listener);
        propChangeDispatcher.addPropertyChangeListener(SAVING_STATE , listener);
    }

    public void restoreState() {
        propChangeDispatcher.firePropertyChange(RESTORING_STATE, null, null);
    }

    public void saveState() {
        Preferences preferences = Preferences.userNodeForPackage(ResourceBundle.class);
        preferences.put("baseName", bundle.getBaseBundleName());
        preferences.put("language", bundle.getLocale().toString());
        propChangeDispatcher.firePropertyChange(SAVING_STATE, null, null);
    }
}
