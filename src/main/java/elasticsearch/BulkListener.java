package elasticsearch;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BulkListener implements BulkProcessor.Listener {
	
	private static final Logger log = LoggerFactory.getLogger(BulkListener.class);
	
	private long start;
	
	private int clientId = 0;
	
	
	
	public BulkListener(int clientId){
		this.clientId = clientId; 
	}
	@Override
	public void beforeBulk(long executionId,
			BulkRequest request) {
		start = System.currentTimeMillis();
		log.info("[elasticsearch client:"+clientId+"]executionId:"+executionId+"-bulk commit:"+request.numberOfActions());	
	}

	@Override
	public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
		if (!response.hasFailures()) {
			log.info("[elasticsearch client:"+clientId+"]executionId:"+executionId+"-bulk complete:"+response.getItems().length+"-timeout:"+(System.currentTimeMillis()-start)+"ms");	
		} else {
			int failnum = 0;
			int successnum = 0;
			for (BulkItemResponse bulkResponse : response) {
				if (!bulkResponse.isFailed()) {
					successnum++;
				} else {
					// 失败消息处理,失败的消息对应kafka的partition-offset写入redis等待容错线程处理

					failnum++;
				}
			}
			log.info("[elasticsearch client:"+clientId+"]executionId:"+executionId+"-success:"+successnum+" fail:"+failnum);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
		log.error(failure.getMessage());
		failure.printStackTrace();
		List<DocWriteRequest> bulkRequestList= request.requests();
		
		log.info("[elasticsearch client:"+clientId+"]executionId:"+executionId+"-bulk fail:" + request.numberOfActions());
	}
	
	
}
