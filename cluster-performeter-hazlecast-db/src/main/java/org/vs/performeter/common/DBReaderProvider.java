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
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "dataProvider")
@Component
public class DBReaderProvider implements DataProvider<Probe> {
    private static Logger LOG = LoggerFactory.getLogger(DBReaderProvider.class);

    private ArrayBlockingQueue<Probe> queue;
    @Resource(name = "DBReader")
    private DBReader<Probe> dbReader;
    private int queueCapacity = 1000;

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public DBReaderProvider() {
        queue = new ArrayBlockingQueue<Probe>(queueCapacity);
    }

    public Boolean offer(Probe probe) {
        try {
//            LOG.error("put:{}", queue.size());
            return queue.offer(probe, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Probe nextData(){
//        LOG.error("nextData:{}", queue.size());
        try {
            return queue.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Probe.ERROR_PROBE;
        }
    }

    @Override
    public void open(int instanceId) {
        dbReader.setFactory(new SimpleProbeFactory());

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
