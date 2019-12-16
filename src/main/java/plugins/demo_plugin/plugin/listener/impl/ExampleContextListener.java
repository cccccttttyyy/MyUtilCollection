package plugins.demo_plugin.plugin.listener.impl;

import plugins.api_module.plugin.listener.PluginContextListener;
import plugins.api_module.plugin.listener.event.PluginContextEvent;

public class ExampleContextListener implements PluginContextListener {

    @Override
    public void contextInitialized(PluginContextEvent event) {
        System.out.println("event:  context Initialized");
    }

    @Override
    public void contextDestroyed(PluginContextEvent event) {
        System.out.println("event:  context Destroyed");
    }
}
