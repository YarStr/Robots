package gui.windows;

import gui.adapters.close.ConfirmCloseInternalFrameAdapter;
import logic.DataModel;
import logic.log.LogChangeListener;
import logic.log.LogEntry;
import logic.log.LogWindowSource;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogWindow extends InternalWindow implements LogChangeListener {
    private final LogWindowSource logSource;

    private final TextArea logContent = new TextArea("");

    DataModel dataModel;

    public LogWindow(LogWindowSource logSource, DataModel dataModel, ConfirmCloseInternalFrameAdapter confirmCloseInternalFrameAdapter) {
        super(WindowType.LOG, dataModel, confirmCloseInternalFrameAdapter);

        this.dataModel = dataModel;
        this.logSource = logSource;
        this.logSource.registerListener(this);

        logContent.setSize(200, 500);
        addLogContent();

        pack();
        updateLogContent();
    }

    private void addLogContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void doDefaultCloseAction() {
        ConcurrentLinkedQueue<LogChangeListener> listeners = logSource.getListener();
        for (LogChangeListener listener : listeners) {
            logSource.unregisterListener(listener);
        }
        super.doDefaultCloseAction();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
