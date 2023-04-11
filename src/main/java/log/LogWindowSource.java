package log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LogWindowSource {
    private final int m_iQueueLength;

    private final ConcurrentLinkedQueue<LogEntry> m_messages;
    private final ConcurrentLinkedQueue<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ConcurrentLinkedQueue<>();
        m_listeners = new ConcurrentLinkedQueue<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.offer(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = m_listeners.toArray(new LogChangeListener[0]);
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.offer(entry);
        synchronized (m_messages) {
            if (m_messages.size() > m_iQueueLength) {
                m_messages.poll();
            }
        }
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        if (activeListeners == null) {
            throw new IllegalStateException("activeListeners is null");
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public Iterable<LogEntry> getMessages(int count)
    {
        return m_messages;
    }

    public ConcurrentLinkedQueue<LogChangeListener> getListener(){
        return m_listeners;
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
