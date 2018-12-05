package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  核心是一个getConfig方法 用于读取资源文件
 */
public class PropertiesBuilder {

	private static final Logger log = LoggerFactory.getLogger(PropertiesBuilder.class);

	public static Properties getConfig(String configName) {
		Properties prop = new Properties();
		InputStream s = null;
		try {
			File file1 = new File("conf/" + configName);
			File file2 = new File("../conf/" + configName);
			if (file1.exists()) {
				// 优先从conf目录下获取配置
				log.info("从conf目录加载:" + configName);
				s = new FileInputStream(file1);
			} else if (file2.exists()) {
				// 优先从conf目录下获取配置
				log.info("从../conf目录加载:" + configName);
				s = new FileInputStream(file2);
			} else {
				// load a properties file from class path
				log.info("从jar内加载:" + configName);
				s = PropertiesBuilder.class.getResourceAsStream("/" + configName);
			}
			if (null == s) {
				log.error("Cannot find " + configName);
			}
			prop.load(s);
			log.info("config:" + configName + " load complete.");
			return prop;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
}
