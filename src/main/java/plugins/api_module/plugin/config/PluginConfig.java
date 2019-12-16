package plugins.api_module.plugin.config;

import com.di.dmas.tag.plugin.context.PluginContext;

import java.util.Set;

public interface PluginConfig {

    String getPluginName();

    PluginContext getPluginContext();

    String getInitParameter(String name);

    Set<String> getInitParameterNames();
}
