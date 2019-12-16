package plugins.api_module.plugin;

import com.di.dmas.tag.plugin.config.PluginConfig;

public interface Plugin {

    void init(PluginConfig config);

    void destroy();
}
