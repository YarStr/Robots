package gui.internalWindows;

import gui.DataModel;
import gui.closeAdapters.ConfirmCloseFrameAdapter;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogWindow extends JInternalFrame implements LogChangeListener, PropertyChangeListener {
    private final LogWindowSource m_logSource;

    private final TextArea m_logContent;

    DataModel dataModel;

    public LogWindow(LogWindowSource logSource, DataModel dataModel, ConfirmCloseFrameAdapter confirmCloseFrameAdapter) {
        super(dataModel.getBundle().getString("logWindow.title"), true, true, true, true);
        this.dataModel = dataModel;
        this.dataModel.addBundleChangeListener(this);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(confirmCloseFrameAdapter);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void doDefaultCloseAction() {
        ConcurrentLinkedQueue<LogChangeListener> listeners = m_logSource.getListener();
        for (LogChangeListener listener : listeners) {
            m_logSource.unregisterListener(listener);
        }
        super.doDefaultCloseAction();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataModel.BUNDLE_CHANGED)) {
            ResourceBundle bundle = (ResourceBundle) evt.getNewValue();
            setTitle(bundle.getString("logWindow.title"));
        }
    }
}
