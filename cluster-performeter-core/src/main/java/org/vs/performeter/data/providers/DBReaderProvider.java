package org.vs.performeter.data.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.SimpleProbeFactory;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties(prefix = "dataProvider")
public class DBReaderProvider implements DataProvider<Probe> {
    private static Logger LOG = LoggerFactory.getLogger(DBReaderProvider.class);

    private ArrayBlockingQueue<Probe> queue;
    @Resource(name = "DBReader")
    public DBReader<Probe> dbReader;
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
            return queue.offer(probe, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Probe nextData(){
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
