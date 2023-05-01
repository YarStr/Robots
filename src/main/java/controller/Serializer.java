package controller;

import log.Logger;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.prefs.Preferences;

public class Serializer {

    public void serialize(JInternalFrame window) {
        String name = window.getClass().toString();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        prefs.putInt(name + "width", window.getWidth());
        prefs.putInt(name + "height", window.getHeight());
        prefs.putInt(name + "x", window.getX());
        prefs.putInt(name + "y", window.getY());
        prefs.putBoolean(name + "icon", window.isIcon());
    }

    public void deserialize(JInternalFrame window) {
        String name = window.getClass().toString();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        window.setSize(prefs.getInt(name + "width", 0), prefs.getInt(name + "height", 0));
        window.setLocation(prefs.getInt(name + "x", 0), prefs.getInt(name + "y", 0));
        try {
            window.setIcon(prefs.getBoolean(name + "icon", false));
        } catch (PropertyVetoException e) {
            Logger.error("Невозможно открыть окно класса " + name);
        }
    }
}
