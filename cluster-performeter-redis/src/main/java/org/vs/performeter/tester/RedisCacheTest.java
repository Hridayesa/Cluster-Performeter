package org.vs.performeter.tester;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.DefaultStatisticsBuilder;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.redis")
@Component
public class RedisCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    private Random rn = new Random();
    private Integer maxNumberOfCacheElements;
    HashOperations<String, String, Long> hashOperations;

    @Resource(name = "redisTemplate")
    void setHashOperations(  RedisTemplate template){
        hashOperations = template.opsForHash();
    }

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        int intKey = rn.nextInt(maxNumberOfCacheElements);
        String key = Integer.toString(intKey);

        // atomic operation
        hashOperations.increment("QQQ",key,1);
    }
}
