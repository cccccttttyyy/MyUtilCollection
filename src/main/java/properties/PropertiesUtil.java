package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);	
	private Properties pro = new Properties();
	
	public PropertiesUtil(String filePath){
		FileInputStream in;
		try {
			if(!new File(filePath).exists()){
				logger.error("..file ["+filePath+"] does not exist..");
			}else{
				in = new FileInputStream(filePath);
				pro.load(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public PropertiesUtil(InputStream in){
		try {
			pro.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getValue(String key){
		return pro.getProperty(key);
	}
}
