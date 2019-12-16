package plugins.plugin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 加载插件的工具
 */
public class PluginClassLoader {

    private final Logger logger = LoggerFactory.getLogger(PluginClassLoader.class);

    private URLClassLoader classLoader;

    /**
     * 根据jar所在路径，初始化classLoader
     *
     * @param jarfileDir
     */
    public PluginClassLoader(String jarfileDir) {
        this(new File(jarfileDir), null, null);
    }

    /**
     * 初始化classLoader
     *
     * @param jarfileDir
     * @param parent
     * @param filter
     */
    public PluginClassLoader(File jarfileDir, ClassLoader parent, FileFilter filter) {
        this.classLoader = createClassLoader(jarfileDir, parent, filter);
    }

    /**
     * 添加jar到url持有的classloader中
     *
     * @param baseDir jar所在文件夹
     * @param filter  jar包过滤器
     * @param quiet   是否静默
     */
    public void addToClassLoader(final String baseDir, final FileFilter filter,
                                 boolean quiet) {

        File base = new File(baseDir);

        if (base != null && base.exists() && base.isDirectory()) {
            File[] files = base.listFiles(filter);
            if (files == null || files.length == 0) {
                if (!quiet) {
                    logger.error("No files added to classloader from lib: "
                            + baseDir + " (resolved as: "
                            + base.getAbsolutePath() + ").");
                }
            } else {
                this.classLoader = replaceClassLoader(classLoader, base, filter);
            }
        } else {
            if (!quiet) {
                logger.error("Can't find (or read) directory to add to classloader: "
                        + baseDir
                        + " (resolved as: "
                        + base.getAbsolutePath()
                        + ").");
            }
        }
    }

    /**
     * 更新URLClassLoader 中的url
     *
     * @param oldLoader
     * @param base
     * @param filter
     * @return
     */
    private URLClassLoader replaceClassLoader(final URLClassLoader oldLoader,
                                              final File base, final FileFilter filter) {

        if (null != base && base.canRead() && base.isDirectory()) {
            File[] files = base.listFiles(filter);

            if (null == files || 0 == files.length) {
                logger.error("replaceClassLoader base dir:{} is empty", base.getAbsolutePath());
                return oldLoader;
            }

            logger.info("replaceClassLoader base dir: {} ,size: {}", base.getAbsolutePath(), files.length);

            URL[] oldElements = oldLoader.getURLs();
            URL[] elements = new URL[oldElements.length + files.length];
            System.arraycopy(oldElements, 0, elements, 0, oldElements.length);

            for (int j = 0; j < files.length; j++) {
                try {
                    URL element = files[j].toURI().normalize().toURL();
                    elements[oldElements.length + j] = element;

                    logger.info("Adding '{}' to classloader", element.toString());

                } catch (MalformedURLException e) {
                    logger.error("load jar file error", e);
                }
            }
            ClassLoader oldParent = oldLoader.getParent();
            try {
                if (oldLoader != null) {
                    oldLoader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return URLClassLoader.newInstance(elements, oldParent);
        }

        return oldLoader;
    }

    private URLClassLoader createClassLoader(final File libDir,
                                             ClassLoader parent, final FileFilter filter) {
        if (null == parent) {
            parent = Thread.currentThread().getContextClassLoader();
        }
        return replaceClassLoader(
                URLClassLoader.newInstance(new URL[0], parent), libDir, filter);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {

        return classLoader.loadClass(className);
    }
}
