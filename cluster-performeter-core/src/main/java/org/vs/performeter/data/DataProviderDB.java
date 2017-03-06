package org.vs.performeter.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.vs.performeter.common.*;

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

    public Boolean offer(Probe probe) {
        return queue.offer(probe);
    }

    @Override
    public Probe nextData(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Probe.ERROR_PROBE;
        }
    }

    @Override
    public void start() {
        dbReader.setConsumer(this::offer);
        dbReader.pump();
    }

    @Override
    public void stop() {
        dbReader.stop();
    }
}
