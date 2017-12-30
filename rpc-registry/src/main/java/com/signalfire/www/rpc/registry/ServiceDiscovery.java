package com.signalfire.www.rpc.registry;

import io.netty.util.internal.ThreadLocalRandom;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 本类用于client发现server节点的变化 ，实现负载均衡
 *
 */
public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    /**
     * zk链接
     *
     * @param registryAddress
     */
    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;

        ZooKeeper zk = connectServer();
        if (zk != null) {
            watchNode(zk);
        }
    }

    /**
     * 监听
     * @param zk
     */
    private void watchNode(final ZooKeeper zk) {
        try {
            //获取所有子节点

            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            //节点改变
                            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                                watchNode(zk);

                            }
                        }
                    });

            ArrayList<String> dataList = new ArrayList<String>();


            //循环子节点
            for (String node : nodeList) {
                //获取节点中的服务器地址
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);

                //存储到list
                dataList.add(new String(bytes));

            }

            LOGGER.debug("node data:{}",dataList);

            //将节点信息记录在成员变量中
            this.dataList = dataList;
        } catch (Exception e) {
            LOGGER.error("",e);
        }
    }

    /**
     * 链接
     *
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            if (event.getState() == Event.KeeperState.SyncConnected) {
                                latch.countDown();
                            }
                        }
                    });
            latch.wait();
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return zk;
    }

    /**
     * 发现新节点
     *
     * @return
     */
    public String disCovery() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                LOGGER.debug("using only data :{}",data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("use random data :{}",data);
            }
        }

        return data;
    }
}
