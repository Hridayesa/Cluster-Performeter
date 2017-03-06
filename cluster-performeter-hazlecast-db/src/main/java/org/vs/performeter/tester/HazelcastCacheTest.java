package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.Probe;
import org.vs.performeter.data.*;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    @Resource private IMap testMap;
    private Integer maxNumberOfCacheElements;
    @Resource(name = "DataProviderDB")
    DataProvider<Probe> provider;
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
    public void beforeTests() {
        super.beforeTests();
        provider.start();
    }

    @Override
    public void afterTests() {
        try {
            provider.stop();
        }finally {
            super.afterTests();
        }
    }

    @Override
    public void doSingleTest() {
        count = 0;
        Probe probe = provider.nextData();
        Object key = probe.getKey();
        testMap.lock(key);
        try {
            if (testMap.put(key, probe)==null) {
                statisticsBuilder.countPlusPlus();
            }else{
//TODO:                statisticsBuilder.countDupFound();
            }
        } finally {
            testMap.unlock(key);
        }
    }
}
