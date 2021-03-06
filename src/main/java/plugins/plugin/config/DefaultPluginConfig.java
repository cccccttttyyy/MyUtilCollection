package plugins.plugin.config;

import com.di.dmas.qc.plugin.config.elem.Param;
import com.di.dmas.qc.plugin.config.elem.PluginDefinition;
import com.di.dmas.tag.plugin.config.PluginConfig;
import com.di.dmas.tag.plugin.context.PluginContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultPluginConfig implements PluginConfig {
    private String pluginName;
    private Map<String, String> params;
    private PluginContext context;

    public DefaultPluginConfig(PluginDefinition pd, PluginContext context) {
        this.pluginName = pd.getName();
        this.params = getInitParams(pd.getParams());
        this.context = context;
    }

    @Override
    public String getPluginName() {
        return pluginName;
    }

    @Override
    public PluginContext getPluginContext() {
        return context;
    }

    @Override
    public String getInitParameter(String name) {
        return params.get(name);
    }

    @Override
    public Set<String> getInitParameterNames() {
        return params.keySet();
    }

    private Map<String, String> getInitParams(List<Param> list) {
        Map<String, String> params = new HashMap<>(8);
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                params.put(param.getName(), param.getValue());
            }
        }
        return params;
    }

}
