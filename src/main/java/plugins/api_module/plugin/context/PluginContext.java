package plugins.api_module.plugin.context;

import java.util.Set;

public interface PluginContext {

    Object getAttribute(String attr);

    Set<String> getAttributeNames();

    void setAttribute(String attr, Object value);

    void removeAttribute(String attr);
}
