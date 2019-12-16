package plugins.plugin.filter;

import com.di.dmas.qc.plugin.util.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * 插件目录文件过滤器
 */
public class LibraryFileFilter implements FileFilter {
    private String regex;

    public LibraryFileFilter(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean accept(File pathname) {
        if (null == pathname || pathname.isDirectory())
            return false;
        String name = pathname.getName();
        System.out.println(regex + " " + name);
        if (!(name.endsWith(".jar") || name.endsWith(".zip")))
            return false;
        return Pattern.matches(regex, name) || StringUtils.isEmpty(regex);
    }
}
