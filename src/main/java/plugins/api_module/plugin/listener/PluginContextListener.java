package plugins.api_module.plugin.listener;

import plugins.api_module.plugin.listener.event.PluginContextEvent;

/**
 * 监听context
 */
public interface PluginContextListener extends EventListener {
    void contextInitialized(PluginContextEvent event);

    void contextDestroyed(PluginContextEvent event);
}
