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
    private Random rn = new Random();
    private Integer maxNumberOfCacheElements;
    @Resource(name = "DBReader")
    DBReader<Probe> reader;

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    public void addToCache(Probe probe){
        try {
            Object key = probe.getKey();
            testMap.put(key, probe);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        reader.setFactory(new SimpleProbeFactory());
        reader.setConsumer(this::addToCache);
        long t = System.currentTimeMillis();
        reader.pump();
        System.out.println("Total time = "+t+"msec");

//        int intKey = rn.nextInt(maxNumberOfCacheElements);
//        String key = Integer.toString(intKey);
//        testMap.lock(key);
//        try {
//
//            Integer limit = (Integer) testMap.get(key);
//            limit = (limit == null) ? 1 : limit + 1;
//            testMap.set(key, limit);
//
//        } finally {
//            testMap.unlock(key);
//        }
    }

}
