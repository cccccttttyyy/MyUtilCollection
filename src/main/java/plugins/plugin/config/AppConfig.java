package plugins.plugin.config;

import com.di.dmas.qc.plugin.config.elem.*;

import java.util.List;

public class AppConfig {
    private String contextName; //上下文名称
    private List<PropertiesPlaceholder> placeholders;   //全局参数

    private List<Library> libs; //外部jar

    private List<Param> contextParams;//上下文参数

    private List<PluginDefinition> plugins; //插件

    private List<ListenerDefinition> listeners; //Listener

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public List<PropertiesPlaceholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<PropertiesPlaceholder> placeholders) {
        this.placeholders = placeholders;
    }

    public List<Library> getLibs() {
        return libs;
    }

    public void setLibs(List<Library> libs) {
        this.libs = libs;
    }

    public List<Param> getContextParams() {
        return contextParams;
    }

    public void setContextParams(List<Param> contextParams) {
        this.contextParams = contextParams;
    }

    public List<PluginDefinition> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<PluginDefinition> plugins) {
        this.plugins = plugins;
    }

    public List<ListenerDefinition> getListeners() {
        return listeners;
    }

    public void setListeners(List<ListenerDefinition> listeners) {
        this.listeners = listeners;
    }
}
