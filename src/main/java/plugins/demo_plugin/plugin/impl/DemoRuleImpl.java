package plugins.demo_plugin.plugin.impl;

import com.di.dmas.tag.plugin.category.DemoRule;
import com.di.dmas.tag.plugin.config.PluginConfig;

public class DemoRuleImpl implements DemoRule {
    private PluginConfig config;

    @Override
    public void init(PluginConfig config) {
        this.config = config;
        System.out.println("DemoPlugin init...");
    }

    @Override
    public void destroy() {
        System.out.println("DemoPlugin destroy...");
    }

    @Override
    public String compute(String msg) {
        System.out.println("start DemoRuleImpl compute...............................");

        StringBuffer print = new StringBuffer();
        print.append("pluginname: " + config.getPluginName());

        print.append("\nplugin-context:");
        for (String o : config.getPluginContext().getAttributeNames()) {
            print.append("[" + o + ": " + config.getPluginContext().getAttribute(o) + "]");
        }
        print.append("\ninit-param:");
        for (String o : config.getInitParameterNames()) {
            print.append("[" + o + ": " + config.getInitParameter(o) + "]");
        }
        System.out.println(print);

        config.getPluginContext().setAttribute("key", "a");
        config.getPluginContext().setAttribute("key", "b");
        config.getPluginContext().removeAttribute("key");
        System.out.println("end DemoRuleImpl compute...............................");
        return msg + " has bean processed";
    }
}
