package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

public class DataModel {
    public static String BUNDLE_CHANGED = "DataModel.bundle";

    private final PropertyChangeSupport propChangeDispatcher = new PropertyChangeSupport(this);

    private ResourceBundle bundle;

    public DataModel(String resourceName, String nameLocal) {
        bundle = ResourceBundle.getBundle(resourceName, new Locale(nameLocal));
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
    }
}
