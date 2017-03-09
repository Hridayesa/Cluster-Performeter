package org.vs.performeter.tester;

import org.apache.ignite.IgniteCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.DefaultStatisticsBuilder;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.locks.Lock;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.ignite")
@Component
public class IgniteCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    private Random random = new Random();
    private Integer maxNumberOfCacheElements;

    @Resource
    IgniteCache<Integer,Integer> counter;


    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        int key = random.nextInt(maxNumberOfCacheElements);

        Lock lock = counter.lock(key);
        lock.lock();
        try {

            Integer limit = counter.get(key);
            limit = (limit == null) ? 1 : limit + 1;
            counter.put(key, limit);

        } finally {
            lock.unlock();
        }
    }
}
