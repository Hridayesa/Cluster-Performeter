package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DBReader;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.Probe;
import org.vs.performeter.common.SimpleProbeFactory;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    @Resource private IMap testMap;
    private Integer maxNumberOfCacheElements;
    @Resource(name = "DBReader")
    private DBReader<Probe> reader;
    private int count;

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    public Boolean addToCache(Probe probe){
        try {
            Object key = probe.getKey();
            testMap.put(key, probe);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        statisticsBuilder.countPlusPlus();
        return ++count<maxNumberOfCacheElements;
    }

    @Override
    public void doSingleTest() {
        count = 0;
        statisticsBuilder.countPlusPlus();

        reader.setFactory(new SimpleProbeFactory());
        reader.setConsumer(this::addToCache);
        long t = System.currentTimeMillis();
        reader.pump();
        System.out.println("Total time = "+t+"msec");
    }

}
