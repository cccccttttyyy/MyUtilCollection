package plugins.plugin.util;

import com.di.dmas.tag.plugin.listener.EventListener;
import com.di.dmas.tag.plugin.listener.PluginContextAttributeListener;
import com.di.dmas.tag.plugin.listener.PluginContextListener;
import com.di.dmas.tag.plugin.listener.event.ContextEventType;
import com.di.dmas.tag.plugin.listener.event.PluginContextAttributeEvent;
import com.di.dmas.tag.plugin.listener.event.PluginContextEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class PluginContextEventMulticaster {

    private LinkedList<PluginContextListener> contextListeners = new LinkedList();
    private LinkedList<PluginContextAttributeListener> contextAttributeListeners = new LinkedList();

    public PluginContextEventMulticaster() {
    }

    public static PluginContextEventMulticaster getInstance() {
        return MulticasterHolder.INSTANCE;
    }

    public LinkedList<PluginContextListener> getContextListeners() {
        return contextListeners;
    }

    public void registerListener(EventListener listener) {
        if (listener instanceof PluginContextListener) {
            this.contextListeners.add((PluginContextListener) listener);
        } else if (listener instanceof PluginContextAttributeListener) {
            this.contextAttributeListeners.add((PluginContextAttributeListener) listener);
        }

    }

    public LinkedList<PluginContextAttributeListener> getContextAttributeListeners() {
        return contextAttributeListeners;
    }

    public void multicastEvent(final PluginContextEvent event, ContextEventType type) {
        Iterator<PluginContextListener> iter = this.getContextListeners().iterator();
        while (iter.hasNext()) {
            final PluginContextListener listener = iter.next();
            if (type.equals(ContextEventType.CONTEXT_INIT)) {
                listener.contextInitialized(event);
            } else if (type.equals(ContextEventType.CONTEXT_DESTORY)) {
                listener.contextDestroyed(event);
            }
        }
    }

    public void multicastEvent(final PluginContextAttributeEvent event, ContextEventType type) {
        Iterator<PluginContextAttributeListener> iter = this.getContextAttributeListeners().iterator();
        while (iter.hasNext()) {
            final PluginContextAttributeListener listener = iter.next();
            if (type.equals(ContextEventType.ATTRIBUTE_ADD)) {
                listener.attributeAdded(event);
            } else if (type.equals(ContextEventType.ATTRIBUTE_REMOVE)) {
                listener.attributeRemoved(event);
            } else if (type.equals(ContextEventType.ATTRIBUTE_REPLACE)) {
                listener.attributeReplaced(event);
            }
        }
    }

    private static class MulticasterHolder {
        public static final PluginContextEventMulticaster INSTANCE = new PluginContextEventMulticaster();
    }

}
