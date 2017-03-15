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
import java.util.concurrent.locks.Lock;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.ignite")
@Component
public class IgniteCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private static Logger LOG = LoggerFactory.getLogger(IgniteCacheTest.class);

    @Resource(name = "isoFileDataProvider")
    private DataProvider<Probe> dataProvider;

    @Resource
    IgniteCache<String, Probe> counter;

    int cnt = 1;


    @Override
    public void doSingleTest() {
        Probe obj = dataProvider.nextData();
        if (obj == null) {
            containerManager.stop();
            return;
        }
        String key = obj.getKey();

        statisticsBuilder.countPlusPlus();

        try {
            if (!counter.putIfAbsent(key, obj)) {
                statisticsBuilder.collisionPlusPlus();
            }
        } catch (Exception ex) {
            LOG.error("Error plasing to map", ex);
        }

//        Lock lock = counter.lock(key);
//        lock.lock();
//        try {
//
//            Probe limit = counter.get(key);
//            if (limit != null) {
//                statisticsBuilder.collisionPlusPlus();
//            } else {
//                counter.put(key, obj);
//            }
//        }catch (Exception ex){
//            LOG.error("Error plasing to map", ex);
//        } finally {
//            lock.unlock();
//        }
        if (cnt++ % 10000 == 0) {
            LOG.info("Map size = {}/{}", counter.size(), counter.localSize());
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