package elasticsearch;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


public class ESTransportClient {
    private static final Logger log = LoggerFactory.getLogger(ESTransportClient.class);
    private static int clientId = 1;
    private TransportClient client = null;
    private BulkProcessor bulkProcessor = null;

    public ESTransportClient() {
        super();
        if (!init()) {
            log.error("ESTransportClient init() error");
        }
        log.info("ESTransportClient init() success");
    }

    private boolean init() {
        Settings settings = Settings.builder().put("cluster.name", ElasticSearchConstant.CLUSTER_NAME)
                .put("client.transport.sniff", true)
                .build();
        try {
            client = new PreBuiltTransportClient(settings);
            for (String host : ElasticSearchConstant.ES_HOSTS) {
                String ip = host.split("\\:")[0];
                String port = host.split("\\:")[1];
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        if (ElasticSearchConstant.IS_BULK) {
            initBulk();
        }
        return true;
    }

    private boolean initBulk() {
        if (bulkProcessor == null) {
            this.bulkProcessor = BulkProcessor.builder(this.getConnection(), new BulkListener(clientId))
                    .setBulkActions(ElasticSearchConstant.BULK_ACTIONS)
                    .setBulkSize(new ByteSizeValue(ElasticSearchConstant.BULK_SIZE, ByteSizeUnit.MB))
                    .setFlushInterval(TimeValue.timeValueSeconds(ElasticSearchConstant.FLUSH_INTERVAL))
                    .setConcurrentRequests(ElasticSearchConstant.CONCURRENT_REQUESTS)
                    .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();
        }
        return true;
    }

    public TransportClient getConnection() {
        if (client == null) {
            synchronized (ESTransportClient.class) {
                if (client == null) {
                    init();
                }
            }
        }
        return client;
    }

    public BulkProcessor getBulkProcessor() {
        if (!ElasticSearchConstant.IS_BULK) {
            log.error("ElasticSearchConstant.IS_BULK is false no BulkProcessor create");
            return null;
        }
        if (bulkProcessor == null) {
            synchronized (ESTransportClient.class) {
                if (bulkProcessor == null) {
                    if (client == null) {
                        init();
                    } else {
                        initBulk();
                    }
                }
            }
        }
        return bulkProcessor;
    }

    public void close() {
        try {
            if (!bulkProcessor.awaitClose(5, TimeUnit.MINUTES)) {
                log.info("clientId:" + clientId + " bulkProcessor close has waited 5min but failed , the next: force close");
                bulkProcessor.flush();
                bulkProcessor.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close();
    }
}
