package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.*;
import org.vs.performeter.data.DataProvider;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    @Resource private IMap testMap;
    private Integer maxNumberOfCacheElements;

    @Resource(name = "DBReaderProvider")
    protected DataProvider<Probe> provider;

    public DataProvider<Probe> getProvider() {
        return provider;
    }

    public void setProvider(DBReaderProvider provider) {
        this.provider = provider;
    }

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    @Override
    public void beforeTests() {
        super.beforeTests();
        provider.open(0);
    }

    @Override
    public void afterTests() {
        try {
            provider.close();
        }finally {
            super.afterTests();
        }
    }

    @Override
    public void doSingleTest() {
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
