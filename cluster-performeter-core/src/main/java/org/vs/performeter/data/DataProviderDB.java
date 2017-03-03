package org.vs.performeter.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DBReader;
import org.vs.performeter.common.Probe;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;

@Component
@ConfigurationProperties(prefix = "dataProvider")
public class DataProviderDB implements DataProvider<Probe> {
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

    public DataProviderDB() {
        dbReader.setConsumer(this::offer);
        queue = new ArrayBlockingQueue<Probe>(queueCapacity);
    }

    public void offer(Probe probe) {
        queue.offer(probe);
    }

    @Override
    public Probe nextData() throws InterruptedException {
        return queue.take();
    }
}
