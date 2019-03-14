package elasticsearch;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ElasticSearchRestClient {
	
	private static final Logger log = LoggerFactory.getLogger(ElasticSearchRestClient.class);
	
	private RestClientBuilder  builder;
	
	private RestHighLevelClient highLevelRestClient = null;
	
	private BulkProcessor bulkProcessor = null;
	
	private static int clientId = 1;
	
	public ElasticSearchRestClient(){
		log.info("ElasticSearchRestClient clientId:"+clientId+" start init...");
		if(highLevelRestClient==null){  		
    		initClient();
		}	
    	if(ElasticSearchConstant.IS_BULK){
    		initBulk();
    	}
	}
	
	private void initClient(){
		for(String host:ElasticSearchConstant.ES_HOSTS){
			String ip = host.split(":")[0];
			String port = host.split(":")[1];
			builder = RestClient.builder(new HttpHost(ip, Integer.parseInt(port))); 
		}
		
		builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {  
            @Override  
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(ElasticSearchConstant.REQUEST_CONNECT_TIMEOUT);  
                requestConfigBuilder.setSocketTimeout(ElasticSearchConstant.REQUEST_SOCKET_TIMEOUT);  
                requestConfigBuilder.setConnectionRequestTimeout(ElasticSearchConstant.CONNECTION_REQUEST_TIMEOUT);  
                return requestConfigBuilder;  
            }  
        }); 
		builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultIOReactorConfig(
                        IOReactorConfig.custom()
                        .setIoThreadCount(ElasticSearchConstant.HTTPCLIENT_IO_THREADCOUNT)//线程数配置
                    .setConnectTimeout(ElasticSearchConstant.HTTPCLIENT_CONNECT_TIMEOUT)
                    .setSoTimeout(ElasticSearchConstant.HTTPCLIENT_SO_TIMEOUT)
                    .build());
            }
        });
		//设置超时
		builder.setMaxRetryTimeoutMillis(ElasticSearchConstant.MAX_RETRY_TIMEOUT);
		//构建high level client
		highLevelRestClient = new RestHighLevelClient(builder);
		log.info("ElasticSearchRestClient:"+clientId+" init complete.");
		clientId++;
	}
	/**
     * 初始化elasticsearch批量接口
     */
    private void initBulk(){
    	if(bulkProcessor==null){
    		//BulkProcessor.builder(client, listener)
    		bulkProcessor = BulkProcessor.builder(
    				highLevelRestClient::bulkAsync, new BulkListener(clientId))  
		        .setBulkActions(ElasticSearchConstant.BULK_ACTIONS)   
		        .setBulkSize(new ByteSizeValue(ElasticSearchConstant.BULK_SIZE, ByteSizeUnit.MB))   
		        .setFlushInterval(TimeValue.timeValueSeconds(5))   
		        .setConcurrentRequests(ElasticSearchConstant.CONCURRENT_REQUESTS)
		        .setBackoffPolicy(
		            BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) 
		        .build();
    	}
    }
    
    public void closeBulkAndClient(){
    	try {
			//阻塞至所有的请求线程处理完毕后，断开连接资源  
			while (!bulkProcessor.awaitClose(30, TimeUnit.SECONDS)){
				log.debug("bulk processer is closing,wait 30 seconds.");
			}
			highLevelRestClient.close();
		} catch (InterruptedException e) {
			log.error("close bulk processer exception.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("close elasticsearch restclient exception.");
			e.printStackTrace();
		}finally{
			log.info("bulk processer closed.");
		}
    }
    
	public void bulk(Map<String, Object> map, String index, String type, String id){
		IndexRequest indexRequest = new IndexRequest(index, type, id).source(map);
		bulkProcessor.add(indexRequest);	
		bulkProcessor.flush();
	}
	
	public boolean checkClientIsAlive() {
		try {
			// 构造header[]
			Header[] headers = {new BasicHeader("header", "value")};
			if (highLevelRestClient==null||!highLevelRestClient.ping(headers)){
				return false;
			}
		} catch (Exception e){
			log.error(e.getMessage());
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		ElasticSearchRestClient client = new ElasticSearchRestClient();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ceshi", "ceshi3");
		map.put("ceshiA", "ceshi2");
		client.bulk(map, "a", "b", "c");
		client.closeBulkAndClient();
	}
}
