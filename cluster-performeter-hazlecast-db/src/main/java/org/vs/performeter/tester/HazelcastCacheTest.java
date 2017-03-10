package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.providers.DBReaderProvider;

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
    private volatile boolean started;

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
        provider.open(containerManager.getId());
        LOG.info("open done containerManager.getId()=", containerManager.getId());
//        super.beforeTests();
    }

    @Override
    public void afterTests() {
//        super.afterTests();
        LOG.info("afterTests called.");
        provider.close();
    }

    @Override
    public void doSingleTest() {
        Probe probe = provider.nextData();
        if (!started){
            started=true;
            super.beforeTests();
        }
        if (probe == null || probe==Probe.END_PROBE || probe==Probe.ERROR_PROBE) {
            statisticsBuilder.stop();
            containerManager.stop();
            return;
        }

        Object key = probe.getKey();
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
