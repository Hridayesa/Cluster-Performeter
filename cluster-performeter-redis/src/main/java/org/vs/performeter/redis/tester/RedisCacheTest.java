package org.vs.performeter.redis.tester;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.core.common.Tester;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by dekar on 02.12.2016.
 */
@Component
public class RedisCacheTest implements Tester {
    private Random rn = new Random();
    @Value("${maxNamberOfCacheElements}")
    private Integer maxNumber;
    HashOperations<String, String, Long> hashOperations;

    @Resource(name = "redisTemplate")
    void setHashOperations(  RedisTemplate template){
        hashOperations = template.opsForHash();
    }

    @Override
    public void doSingleTest() {
        int intKey = rn.nextInt(maxNumber);
        String key = Integer.toString(intKey);

        hashOperations.increment("QQQ",key,1);
    }
}
