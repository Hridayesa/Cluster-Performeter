package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DBReaderProvider;
import org.vs.performeter.common.DefaultStatisticsBuilder;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilder;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.iso.Probe;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private static Logger LOG = LoggerFactory.getLogger(HazelcastCacheTest.class);

    @Resource private IMap testMap;
    private Integer maxNumberOfCacheElements;

    private long cnt = 0;

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
//        LOG.debug("open done.");
    }

    @Override
    public void afterTests() {
//        LOG.debug("afterTests called.");
        try {
            provider.close();
        }finally {
            super.afterTests();
        }
    }

    @Override
    public void doSingleTest() {
//        LOG.debug("doSingleTest called.");

        Probe probe = provider.nextData();
        Object key = probe.getKey();
//        LOG.error("{}:key={}", ++cnt, key);
        testMap.lock(key);
        try {
            if (testMap.put(key, probe)==null) {
                statisticsBuilder.countPlusPlus();
            }else{
                statisticsBuilder.collisionPlusPlus();
            }
        } finally {
            testMap.unlock(key);
        }
        cnt++;
        if (cnt%10000==0) {
            LOG.info("testMap.size = {}", testMap.size());
        }
    }
}
