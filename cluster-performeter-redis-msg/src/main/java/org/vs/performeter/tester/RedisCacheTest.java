package org.vs.performeter.tester;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.dummy.Msg;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.redis")
@Component
public class RedisCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private Random random = new Random();

    @Resource(name = "dummyMsgGenDataProvider")
    private DataProvider<Msg> dataProvider;

    HashOperations<String, String, Msg> hashOperations;
    protected static AtomicLong cnt = new AtomicLong();

    @Resource(name = "redisTemplate")
    void setHashOperations(RedisTemplate template) {
        hashOperations = template.opsForHash();
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();
        if (cnt.incrementAndGet()%100_000==0){
            System.out.println(cnt.get());
        }
        Msg msg = dataProvider.nextData();
        if (!hashOperations.putIfAbsent("QQQ", msg.getId(), msg)) {
            statisticsBuilder.collisionPlusPlus();
        }
    }

}
