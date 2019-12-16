package plugins.plugin.manager;

import com.di.dmas.qc.plugin.config.AppConfig;
import com.di.dmas.qc.plugin.config.elem.Library;
import com.di.dmas.qc.plugin.config.elem.ListenerDefinition;
import com.di.dmas.qc.plugin.config.elem.Param;
import com.di.dmas.qc.plugin.config.elem.PluginDefinition;
import com.di.dmas.qc.plugin.context.DefaultPluginContext;
import com.di.dmas.qc.plugin.exception.PluginException;
import com.di.dmas.qc.plugin.filter.LibraryFileFilter;
import com.di.dmas.qc.plugin.util.PluginClassLoader;
import com.di.dmas.qc.plugin.util.PluginContextEventMulticaster;
import com.di.dmas.qc.plugin.util.StringUtils;
import com.di.dmas.qc.plugin.xml.AppConfigParser;
import com.di.dmas.tag.plugin.Plugin;
import com.di.dmas.tag.plugin.context.PluginContext;
import com.di.dmas.tag.plugin.listener.EventListener;
import com.di.dmas.tag.plugin.listener.event.ContextEventType;
import com.di.dmas.tag.plugin.listener.event.PluginContextEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class DefaultPluginFactory extends AbstractPluginFactory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PluginClassLoader classLoader;
    private AppConfig config;

    private PluginContext context;

    private volatile boolean closed = false;

    public DefaultPluginFactory(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path can not be empty!");
        }
        if (path.startsWith("classpath:")) {
            path = path.substring(10);
        }
        this.config = new AppConfigParser().parse(path);
        init(this.config);
    }

    public DefaultPluginFactory(File file) {
        this.config = new AppConfigParser().parse(file);
        init(this.config);
    }

    @Override
    protected AppConfig getAppConfig() {
        return this.config;
    }

    @Override
    protected Class<?> loadClass(String clazz) throws ClassNotFoundException {
        return classLoader.loadClass(clazz);
    }

    @Override
    protected PluginContext getPluginContext() {
        return this.context;
    }

    @Override
    public void close() {

        if (!closed) {
            closed = true;
            for (Object o : pluginInstanceMap.values()) {
                Plugin plugin = (Plugin) o;
                try {
                    plugin.destroy();
                } catch (Exception e) {

                }
            }
            context = null;
            PluginContextEventMulticaster.getInstance().multicastEvent(new PluginContextEvent(context), ContextEventType.CONTEXT_DESTORY);
            pluginInstanceMap.clear();
            pluginNameMap.clear();
        }
    }

    /**
     * 读取全局配置信息，初始化classLoader;，初始化pluginNameMap，加载listerer
     *
     * @param config 全局配置
     */
    private void init(AppConfig config) {
        PluginContextEventMulticaster multicaster = PluginContextEventMulticaster.getInstance();

        //初始化context
        context = new DefaultPluginContext();
        List<Param> list = config.getContextParams();
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                context.setAttribute(param.getName(), param.getValue());
            }
        }

        //加载libs
        List<Library> libs = config.getLibs();
        if (libs != null && libs.size() > 0) {
            for (Library lib : libs) {
                addExternalJar(lib.getDir(), new LibraryFileFilter(lib.getRegex()));
            }
        }

        //加载插件信息
        List<PluginDefinition> pds = config.getPlugins();
        if (pds != null && pds.size() > 0) {
            for (PluginDefinition pd : pds) {
                if (pluginNameMap.containsKey(pd.getName())) {
                    throw new PluginException("Plugin name:" + pd.getName() + " already exists");
                }
                pluginNameMap.put(pd.getName(), pd);
            }
        }

        //加载监听器信息
        List<ListenerDefinition> listeners = config.getListeners();
        if (listeners != null && listeners.size() > 0) {
            for (ListenerDefinition listener : listeners) {
                try {
                    Class<?> clazz = loadClass(listener.getClazz());
                    //clazz.getGenericSuperclass();
                    Object test_class = clazz.newInstance();
                    multicaster.registerListener((EventListener) clazz.newInstance());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }

        multicaster.multicastEvent(new PluginContextEvent(context), ContextEventType.CONTEXT_INIT);

    }

    private void addExternalJar(String basePath, FileFilter filter) {
        if (StringUtils.isEmpty(basePath)) {
            throw new IllegalArgumentException("basePath can not be empty!");
        }
        File dir = new File(basePath);
        if (!dir.exists()) {
            throw new IllegalArgumentException("basePath not exists:" + basePath);
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("basePath must be a directory:" + basePath);
        }
        if (classLoader == null) {
            classLoader = new PluginClassLoader(new File(basePath), null, filter);
        } else {
            classLoader.addToClassLoader(basePath, filter, false);
        }
    }

}
