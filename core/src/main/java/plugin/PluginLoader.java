package plugin;

import ru.robot.interfaces.Plugin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private final JFrame parentFrame;
    private final ResourceBundle bundle;

    public PluginLoader(JFrame parentFrame, ResourceBundle bundle) {
        this.bundle = bundle;
        this.parentFrame = parentFrame;
        setLocalisation(bundle);
    }


    public Plugin getRobotPlugin() {
        try {
            return fgetRobotPlugin();
        } catch (Exception e) {
            return new DefaultPlugin();
        }
    }

    public Plugin fgetRobotPlugin() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        File pluginFile = getPluginFile();

        URL jarURL = pluginFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
        String mainClassName = getPluginProperties(pluginFile).getProperty("main.class");

        Class<?> pluginClass = classLoader.loadClass("ru.robot.plugins." + mainClassName);
        return (Plugin) pluginClass.newInstance();

    }

    private File getPluginFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("plugins"));
        fileChooser.setDialogTitle(bundle.getString("FileChooser.title"));
        fileChooser.showOpenDialog(parentFrame);
        return fileChooser.getSelectedFile();
    }

    private Properties getPluginProperties(File file) throws IOException {
        Properties result = null;

        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().equals("settings.properties")) {
                try (InputStream inputStream = jar.getInputStream(entry)) {
                    result = new Properties();
                    result.load(inputStream);
                }
            }
        }

        return result;
    }

    private void setLocalisation(ResourceBundle bundle) {
        UIManager.put("FileChooser.openButtonText", bundle.getString("FileChooser.openButtonText"));
        UIManager.put("FileChooser.saveButtonText", bundle.getString("FileChooser.saveButtonText"));
        UIManager.put("FileChooser.cancelButtonText", bundle.getString("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.fileNameLabelText", bundle.getString("FileChooser.fileNameLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText", bundle.getString("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.lookInLabelText", bundle.getString("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.saveInLabelText", bundle.getString("FileChooser.saveInLabelText"));
        UIManager.put("FileChooser.folderNameLabelText", bundle.getString("FileChooser.folderNameLabelText"));
    }
}
