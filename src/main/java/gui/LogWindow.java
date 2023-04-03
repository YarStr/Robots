package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends SafeClosableWindow implements LogChangeListener {
    private final LogWindowSource m_logSource;

    private final TextArea m_logContent;

    ResourceBundle bundle;

    public LogWindow(LogWindowSource logSource, ResourceBundle bundle) {
        super(bundle.getString("logWindow.title"), true, true, true, true);
        this.bundle = bundle;
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
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
        super.doDefaultCloseAction(bundle);
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
