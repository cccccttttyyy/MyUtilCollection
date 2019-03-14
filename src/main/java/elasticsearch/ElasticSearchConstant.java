package elasticsearch;

import properties.PropertiesBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class ElasticSearchConstant {

	public static List<String> ES_HOSTS = new ArrayList<String>();
	
	public static int REQUEST_CONNECT_TIMEOUT = 60000;
	
	public static int REQUEST_SOCKET_TIMEOUT = 60000;
	
	public static int CONNECTION_REQUEST_TIMEOUT = 60000;
	
	public static int HTTPCLIENT_IO_THREADCOUNT = 100;
	
	public static int HTTPCLIENT_CONNECT_TIMEOUT = 60000;
	
	public static int HTTPCLIENT_SO_TIMEOUT = 60000;
	
	public static int MAX_RETRY_TIMEOUT = 300000;

	public static boolean IS_BULK = true;//elasticsearch 是否批量创建

	public static int BULK_ACTIONS = 10000;//elasticsearch 批次数量

	public static int BULK_SIZE = 5;//elastisearch 批次大小单位MB
	
	public static int CONCURRENT_REQUESTS = 1;
	
	static {
		Properties prop = PropertiesBuilder.getConfig("elastic-rest-client.properties");
		ES_HOSTS = Arrays.asList(prop.getProperty("rest.host").split(","));
		REQUEST_CONNECT_TIMEOUT = Integer.parseInt(prop.getProperty("request.connect.timeout"));
		REQUEST_SOCKET_TIMEOUT = Integer.parseInt(prop.getProperty("request.socket.timeout"));
		CONNECTION_REQUEST_TIMEOUT = Integer.parseInt(prop.getProperty("connection.request.timeout"));
		HTTPCLIENT_IO_THREADCOUNT = Integer.parseInt(prop.getProperty("httpclient.io.thread.count"));
		HTTPCLIENT_CONNECT_TIMEOUT = Integer.parseInt(prop.getProperty("httpclient.connect.timeout"));
		HTTPCLIENT_SO_TIMEOUT = Integer.parseInt(prop.getProperty("httpclient.so.timeout"));
		MAX_RETRY_TIMEOUT = Integer.parseInt(prop.getProperty("max.retry.timeout"));
		CONCURRENT_REQUESTS = Integer.parseInt(prop.getProperty("concurrent.requests"));
		String isBulk = prop.get("is.bulk").toString();
		if(isBulk.equals("true")){
			IS_BULK = true;
			BULK_ACTIONS = Integer.parseInt(prop.get("bulk.actions").toString());
			BULK_SIZE = Integer.parseInt(prop.get("bulk.size").toString());
		}else{
			IS_BULK = false;
		}
	}
}
