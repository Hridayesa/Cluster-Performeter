package org.vs.performeter.tester;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.Msg;
import org.vs.performeter.common.RedisCollisionStatistics;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.redis")
@Component
public class RedisCacheTest extends AbstractTester<RedisCollisionStatistics, RedisCollisionStatisticsBuilder> {
    private Random random = new Random();
    private Integer maxNumberOfCacheElements;
    private String blob;

    HashOperations<String, String, Msg> hashOperations;

    @Resource(name = "redisTemplate")
    void setHashOperations(RedisTemplate template) {
        hashOperations = template.opsForHash();
    }

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }

    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        Msg msg = createMsg();

        if (! hashOperations.putIfAbsent("QQQ",msg.getId(), msg)){
            statisticsBuilder.collisionPlusPlus();
        }

    }

    private Msg createMsg() {
        String id = Integer.toString(random.nextInt(maxNumberOfCacheElements));
        return new Msg(id, "name" + id, getBlob());
    }
}
