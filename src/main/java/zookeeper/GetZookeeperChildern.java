package zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 打印zookeeper子节点列表
 * 目前 kafka存在主题列表为：dbInfoList, waitingList, dbInfoListMap, __consumer_offsets, test
 */
public class GetZookeeperChildern implements Watcher {

    //这是个同步工具
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper("172.22.3.237:2182", 5000, new GetZookeeperChildern());
        countDownLatch.await();
//        zooKeeper.create("/test", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.create("/test/a1", "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 同步获得结果
        List<String> childrenList = zooKeeper.getChildren("/brokers/topics", true);
        System.out.println("同步getChildren获得数据结果：" + childrenList);
        // 异步获得结果
        zooKeeper.getChildren("/brokers/topics", true, new MyChildren2Callback(), null);
        Thread.sleep(10000);

    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) { // 连接时的监听事件
                countDownLatch.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) { // 子节点变更时的监听
                try {
                    System.out.println("重新获得Children，并注册监听：" + zooKeeper.getChildren(event.getPath(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

class MyChildren2Callback implements AsyncCallback.Children2Callback {

    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("异步获得getChildren结果，rc=" + rc
                + "；path=" + path + "；ctx=" + ctx + "；children=" + children + "；stat=" + stat);
    }
}

