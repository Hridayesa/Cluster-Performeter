package org.vs.performeter.tester;

import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.ignite")
@Component
public class IgniteCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private static Logger LOG = LoggerFactory.getLogger(IgniteCacheTest.class);

    @Resource
    IgniteCache<String, Object> counter;

    @Resource(name = "isoFileDataProvider")
    private DataProvider<Probe> probeDataProvider;


    @Override
    public void doSingleTest() {
        Probe probe = probeDataProvider.nextData();
        if (probe == null) {
            containerManager.stop();
            return;
        }
        statisticsBuilder.countPlusPlus();


        try {
            if (!counter.putIfAbsent(probe.getKey(), probe)) {
                statisticsBuilder.collisionPlusPlus();
            }
        } catch (Exception ex) {
            LOG.error("Error plasing to map", ex);
        }
    }

    @Override
    public void beforeTests() {
        super.beforeTests();
        probeDataProvider.open(containerManager.getId());
    }

    @Override
    public void afterTests() {
        probeDataProvider.close();
        super.afterTests();
    }

}
