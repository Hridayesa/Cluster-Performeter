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

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private Logger LOG = LoggerFactory.getLogger(HazelcastCacheTest.class);

    @Resource
    private IMap testMap;
    @Resource(name = "isoFileDataProvider")
    private DataProvider<Probe> dataProvider;
    private int cnt;


    @Override
    public void beforeTests() {
        super.beforeTests();
        dataProvider.open(containerManager.getId());
    }

    @Override
    public void afterTests() {
        dataProvider.close();
        super.afterTests();
    }

    public void doSingleTest() {
        Probe obj = dataProvider.nextData();
        if (obj == null) {
            containerManager.stop();
            return;
        }
        String key = obj.getKey();

        statisticsBuilder.countPlusPlus();
        try {

            if (testMap.putIfAbsent(key, obj) != null) {
                statisticsBuilder.collisionPlusPlus();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (cnt++ % 10000 == 0) {
            LOG.info("Map size = {}", testMap.size(), testMap);
        }

    }
}
