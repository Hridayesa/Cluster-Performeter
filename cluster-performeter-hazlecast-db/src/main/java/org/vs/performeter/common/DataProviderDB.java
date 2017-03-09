package org.vs.performeter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.iso.Probe;
import org.vs.performeter.tester.HazelcastCacheTest;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;

@Component
@ConfigurationProperties(prefix = "dataProvider")
public class DataProviderDB implements DataProvider<Probe> {
    private static Logger LOG = LoggerFactory.getLogger(DataProviderDB.class);

    private ArrayBlockingQueue<Probe> queue;
    @Resource(name = "DBReader")
    private DBReader<Probe> dbReader;
    private int queueCapacity = 3000;

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public DataProviderDB() {
        queue = new ArrayBlockingQueue<Probe>(queueCapacity);
    }

    public Boolean offer(Probe probe) {
        try {
//            LOG.error("offer: queue.size = {}", queue.size());
            queue.put(probe);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Probe nextData(){
//        LOG.error("nextData: queue.size = {}", queue.size());
       return queue.poll();
    }

    @Override
    public void open(int instanceId) {
        dbReader.setConsumer(this::offer);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbReader.pump();
            }
        }).start();
    }

    @Override
    public void close() {
        dbReader.stop();
    }
}
