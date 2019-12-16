package plugins.plugin.manager;

import com.di.dmas.qc.plugin.config.AppConfig;
import com.di.dmas.qc.plugin.config.DefaultPluginConfig;
import com.di.dmas.qc.plugin.config.elem.PluginDefinition;
import com.di.dmas.qc.plugin.exception.PluginException;
import com.di.dmas.tag.plugin.Plugin;
import com.di.dmas.tag.plugin.config.PluginConfig;
import com.di.dmas.tag.plugin.context.PluginContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件管理类
 */
public abstract class AbstractPluginFactory implements PluginFactory {

    protected final Map<String, PluginDefinition> pluginNameMap = new HashMap<>();    //key为plugin-name

    protected final ConcurrentHashMap<String, Plugin> pluginInstanceMap = new ConcurrentHashMap<>();

    @Override
    public Plugin getPlugin(String name) throws PluginException {

        Plugin plugin = pluginInstanceMap.get(name);
        if (plugin == null) {
            PluginDefinition pd = getPluginDefinition(name);
            if (pd == null) {
                throw new PluginException("not found plugin:" + name + " definition");
            }
            plugin = getPlugin(name, pd);
        }
        return plugin;
    }

    @Override
    public <T extends Plugin> T getPlugin(Class<T> type) throws PluginException {
        String name = type.getName();
        Plugin plugin = pluginInstanceMap.get(name);
        if (plugin == null) {
            PluginDefinition pd = findCandidatePluginDefinition(type);
            if (pd == null) {
                throw new PluginException("not found plugin:" + type.getName() + " definition");
            }
            plugin = getPlugin(name, pd);
        }
        return (T) plugin;
    }

    @Override
    public PluginDefinition getPluginDefinition(String name) {
        return pluginNameMap.get(name);
    }

    @Override
    public List<String> getPluginNames() {
        return new ArrayList<>(pluginNameMap.keySet());
    }

    @Override
    public boolean hasPlugin(String name) {
        return getPluginDefinition(name) != null;
    }

    /**
     * 根据插件名获取插件，如果没有就尝试创建
     *
     * @param name
     * @param pd
     * @return
     */
    private Plugin getPlugin(String name, PluginDefinition pd) {
        Plugin plugin = pluginInstanceMap.get(name);
        if (plugin == null) {
            synchronized (this) {
                plugin = createPluginInstance(pd);
                invokePluginInitMethod(plugin, new DefaultPluginConfig(pd, getPluginContext()));
                Plugin old = pluginInstanceMap.putIfAbsent(name, plugin);
                if (old != null) {
                    plugin = old;
                }
            }
        }
        return plugin;
    }

    /**
     * 根据插件类型获取插件加载信息
     *
     * @param type
     * @return
     */
    private PluginDefinition findCandidatePluginDefinition(Class<? extends Plugin> type) {
        List<PluginDefinition> pds = getAppConfig().getPlugins();
        if (pds != null && pds.size() > 0) {
            for (PluginDefinition pd : pds) {
                try {
                    Class clazz = loadClass(pd.getClazz());
                    if (type.isAssignableFrom(clazz)) {
                        return pd;
                    }
                } catch (ClassNotFoundException e) {
                    throw new PluginException("not found plugin class:" + pd.getClazz(), e);
                }
            }
        }
        return null;
    }

    /**
     * 创建插件
     *
     * @param pd
     * @return
     * @throws PluginException
     */
    private Plugin createPluginInstance(PluginDefinition pd) throws PluginException {
        try {
            Class<?> clazz = loadClass(pd.getClazz());
            return (Plugin) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new PluginException("not found plugin class:" + pd.getClazz(), e);
        } catch (InstantiationException e) {
            throw new PluginException("construct plugin instance error", e);
        } catch (IllegalAccessException e) {
            throw new PluginException("construct plugin instance error", e);
        }
    }

    /**
     * 初始化插件
     *
     * @param plugin
     * @param config
     * @throws PluginException
     */
    private void invokePluginInitMethod(Plugin plugin, PluginConfig config) throws PluginException {
        try {
            plugin.init(config);
        } catch (Exception e) {
            throw new PluginException("invoke plugin init error", e);
        }
    }

    protected abstract AppConfig getAppConfig();

    protected abstract Class<?> loadClass(String clazz) throws ClassNotFoundException;

    protected abstract PluginContext getPluginContext();
}
