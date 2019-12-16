package plugins.api_module.plugin.listener.event;

import plugins.api_module.plugin.context.PluginContext;

public class PluginContextEvent {
    private PluginContext source;

    public PluginContextEvent(PluginContext source) {
        this.source = source;
    }

    PluginContext getPluginContext() {
        return source;
    }
}
