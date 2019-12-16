package plugins.api_module.plugin.listener;

import com.di.dmas.tag.plugin.listener.event.PluginContextAttributeEvent;

public interface PluginContextAttributeListener extends EventListener {
    void attributeAdded(PluginContextAttributeEvent var1);

    void attributeRemoved(PluginContextAttributeEvent var1);

    void attributeReplaced(PluginContextAttributeEvent var1);
}
