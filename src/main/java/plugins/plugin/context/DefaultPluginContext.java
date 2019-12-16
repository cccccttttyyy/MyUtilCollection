package plugins.plugin.context;

import com.di.dmas.qc.plugin.util.PluginContextEventMulticaster;
import com.di.dmas.tag.plugin.context.PluginContext;
import com.di.dmas.tag.plugin.listener.event.ContextEventType;
import com.di.dmas.tag.plugin.listener.event.PluginContextAttributeEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PluginContext 维护Plugin的Context 多plugin共享 plugin通过COnfig获取
 */
public class DefaultPluginContext implements PluginContext {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(64);

    @Override
    public Object getAttribute(String attr) {
        return map.get(attr);
    }

    @Override
    public Set<String> getAttributeNames() {
        return map.keySet();
    }

    @Override
    public void setAttribute(String attr, Object value) {
        boolean replace = map.containsKey(attr);
        map.put(attr, value);
        if (replace) {
            PluginContextEventMulticaster.getInstance().multicastEvent(new PluginContextAttributeEvent(this, attr, value), ContextEventType.ATTRIBUTE_REPLACE);
        } else {
            PluginContextEventMulticaster.getInstance().multicastEvent(new PluginContextAttributeEvent(this, attr, value), ContextEventType.ATTRIBUTE_ADD);
        }
    }

    @Override
    public void removeAttribute(String attr) {
        map.remove(attr);
        PluginContextEventMulticaster.getInstance().multicastEvent(new PluginContextAttributeEvent(this, attr, null), ContextEventType.ATTRIBUTE_REMOVE);
    }
}
