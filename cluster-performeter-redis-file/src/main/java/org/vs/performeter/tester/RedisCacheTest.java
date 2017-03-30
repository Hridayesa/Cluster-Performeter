package org.vs.performeter.tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class RedisCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    Logger LOG = LoggerFactory.getLogger("RedisCacheTest");
    @Resource(name = "isoFileDataProvider")
    private DataProvider<Probe> dataProvider;
    static AtomicLong i = new AtomicLong();

    HashOperations<String, String, Probe> hashOperations;

    @Resource(name = "redisTemplate")
    void setHashOperations(RedisTemplate template) {
        hashOperations = template.opsForHash();
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

    public void doSingleTest() {
        Probe obj = dataProvider.nextData();
        if (obj == null) {
            containerManager.stop();
            return;
        }
        String key = obj.getKey();

        statisticsBuilder.countPlusPlus();
        try {
            if (!hashOperations.putIfAbsent("QQQ", key, obj)) {
                statisticsBuilder.collisionPlusPlus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        if (i.incrementAndGet() % 100_000 == 0) {
            LOG.info("i = {}", i.get());
        }
    }
}
