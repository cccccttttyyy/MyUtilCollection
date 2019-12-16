package plugins.plugin.manager;

import plugins.api_module.plugin.Plugin;
import plugins.plugin.config.elem.PluginDefinition;
import plugins.plugin.exception.PluginException;

import java.util.List;

public interface PluginFactory {

    /**
     * 根据插件名称获取插件
     *
     * @param name
     * @return
     * @throws PluginException
     */
    Plugin getPlugin(String name) throws PluginException;

    /**
     * 根据class类型获取插件
     *
     * @param type
     * @param <T>
     * @return
     * @throws PluginException
     */
    <T extends Plugin> T getPlugin(Class<T> type) throws PluginException;

    /**
     * 根据插件名称获取插件加载信息
     *
     * @param name
     * @return
     */
    PluginDefinition getPluginDefinition(String name);

    /**
     * 获取插件名称列表
     *
     * @return
     */
    List<String> getPluginNames();

    /**
     * 判断是否存在此插件
     *
     * @param name
     * @return
     */
    boolean hasPlugin(String name);

    /**
     * 销毁插件管理类
     */
    void close();
}
