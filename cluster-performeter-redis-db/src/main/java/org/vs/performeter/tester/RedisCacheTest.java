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
import org.vs.performeter.data.providers.DBReaderProvider;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class RedisCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    Logger LOG = LoggerFactory.getLogger("RedisCacheTest");
    @Resource(name = "DBReaderProvider")
    protected DataProvider<Probe> provider;
    private long cnt = 0;
    private volatile boolean started;

    HashOperations<String, String, Probe> hashOperations;

    @Resource(name = "redisTemplate")
    void setHashOperations(RedisTemplate template) {
        hashOperations = template.opsForHash();
    }

    @Override
    public void beforeTests() {
        provider.open(containerManager.getId());
        LOG.info("open done containerManager.getId()=", containerManager.getId());
        super.beforeTests();
    }

    @Override
    public void afterTests() {
        super.afterTests();
        LOG.info("afterTests called.");
        provider.close();
    }

    public void doSingleTest() {
        Probe probe = provider.nextData();
        if (probe == null || probe == Probe.END_PROBE || probe == Probe.ERROR_PROBE) {
            statisticsBuilder.stop();
            containerManager.stop();
            return;
        }

        String key = probe.getKey();

        statisticsBuilder.countPlusPlus();
        try {
            if (!hashOperations.putIfAbsent("QQQ", key, probe)) {
                statisticsBuilder.collisionPlusPlus();
            }
        } catch (Exception ex) {
            LOG.error("Error placing to cache", ex);
        }
        if (cnt++ % 100_000 == 0) {
            LOG.info("size = {}", hashOperations.size("QQQ"));
        }
    }

    public DataProvider<Probe> getProvider() {
        return provider;
    }

    public void setProvider(DBReaderProvider provider) {
        this.provider = provider;
    }

}


