package plugins.api_module.plugin.listener.event;

import com.di.dmas.tag.plugin.context.PluginContext;

public class PluginContextAttributeEvent extends PluginContextEvent {
    private String name;
    private Object value;

    public PluginContextAttributeEvent(PluginContext source, String name, Object value) {

        super(source);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
