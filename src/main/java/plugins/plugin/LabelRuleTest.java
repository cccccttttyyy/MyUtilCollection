package plugins.plugin;

import org.junit.Test;
import plugins.api_module.plugin.category.DemoRule;
import plugins.plugin.manager.DefaultPluginFactory;
import plugins.plugin.manager.PluginFactory;

public class LabelRuleTest {

    @Test
    public void test_A() {
        PluginFactory pluginFactory = new DefaultPluginFactory("classpath:plugins.xml");
        //PluginFactory pluginFactory = new DefaultPluginFactory(new File("/home/plugins.xml"));
        DemoRule demo = (DemoRule) pluginFactory.getPlugin("simpleRule");
        String s = demo.compute("human beings");
        System.out.println("计算结果：" + s);
        pluginFactory.close();
    }

    @Test
    public void transform_test() {

    }
}