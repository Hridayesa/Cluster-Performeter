package org.vs.performeter.tester;

import org.apache.ignite.IgniteCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.ignite")
@Component
public class IgniteCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    @Resource(name = "isoFileDataProvider")
    private DataProvider<Probe> dataProvider;

    @Resource
    IgniteCache<String,Probe> counter;


    @Override
    public void doSingleTest() {
        Probe obj = dataProvider.nextData();
        if (obj == null) {
            containerManager.stop();
            return;
        }
        String key = obj.getKey();

        statisticsBuilder.countPlusPlus();

        Lock lock = counter.lock(key);
        lock.lock();
        try {

            Probe limit = counter.get(key);
            if (limit!=null) {
                statisticsBuilder.collisionPlusPlus();
            }else {
                counter.put(key, limit);
            }

        } finally {
            lock.unlock();
        }
    }

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
}